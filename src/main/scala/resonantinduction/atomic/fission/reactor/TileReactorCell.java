package resonantinduction.atomic.fission.reactor;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import resonant.api.IReactor;
import resonant.api.IReactorComponent;
import resonant.api.event.PlasmaEvent.SpawnPlasmaEvent;
import resonant.lib.content.module.prefab.TileInventory;
import resonant.lib.multiblock.IMultiBlockStructure;
import resonant.lib.multiblock.MultiBlockHandler;
import resonant.lib.network.PacketHandler;
import resonant.lib.network.Synced;
import resonant.lib.network.Synced.SyncedInput;
import resonant.lib.network.Synced.SyncedOutput;
import resonant.lib.prefab.poison.PoisonRadiation;
import resonant.lib.thermal.ThermalGrid;
import resonant.lib.thermal.ThermalPhysics;
import resonant.lib.utility.inventory.InventoryUtility;
import resonantinduction.atomic.Atomic;
import resonantinduction.atomic.ReactorExplosion;
import resonantinduction.atomic.fusion.TilePlasma;
import resonantinduction.core.ResonantInduction;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** The primary reactor component cell used to build reactors with.
 * 
 * @author Calclavia */
public class TileReactorCell extends TileInventory implements IMultiBlockStructure<TileReactorCell>, IInventory, IReactor, IFluidHandler, ISidedInventory
{
    public static final int RADIUS = 2;
    public static final int MELTING_POINT = 2000;

    private final int specificHeatCapacity = 1000;
    private final float mass = ThermalPhysics.getMass(1000, 7);
    public FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 15);

    @Synced
    public float temperature = 295;

    private float previousTemperature = 295;

    private boolean shouldUpdate = false;

    private long prevInternalEnergy = 0;
    private long internalEnergy = 0;
    private int meltdownCounter = 0;
    private int meltdownCounterMaximum = 1000;

    /** Multiblock Methods. */
    private MultiBlockHandler<TileReactorCell> multiBlock;

    public TileReactorCell()
    {
        super(UniversalElectricity.machine);
        textureName = "machine";
        isOpaqueCube = false;
        normalRender = false;
        customItemRender = true;
    }

    @Override
    protected void onWorldJoin()
    {
        updatePositionStatus();
    }

    @Override
    protected void onNeighborChanged()
    {
        updatePositionStatus();
    }

    @Override
    public void initiate()
    {
        updatePositionStatus();
        super.initiate();
    }

    /** Called when the block is right clicked by the player */
    @Override
    protected boolean use(EntityPlayer player, int side, Vector3 hit)
    {
        if (!world().isRemote)
        {
            TileReactorCell tile = getMultiBlock().get();

            if (!player.isSneaking())
            {
                if (tile.getStackInSlot(0) != null)
                {
                    InventoryUtility.dropItemStack(world(), new Vector3(player), tile.getStackInSlot(0), 0);
                    tile.setInventorySlotContents(0, null);
                    return true;
                }
                else if (player.inventory.getCurrentItem() != null)
                {
                    if (player.inventory.getCurrentItem().getItem() instanceof IReactorComponent)
                    {
                        ItemStack itemStack = player.inventory.getCurrentItem().copy();
                        itemStack.stackSize = 1;
                        tile.setInventorySlotContents(0, itemStack);
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                        return true;
                    }
                }
            }

            player.openGui(Atomic.INSTANCE, 0, world(), tile.xCoord, tile.yCoord, tile.zCoord);
        }

        return true;
    }

    @Override
    protected void markUpdate()
    {
        super.markUpdate();
        shouldUpdate = true;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        /** Move fuel rod down into the primary cell block if possible */
        if (!getMultiBlock().isPrimary())
        {
            if (getStackInSlot(0) != null)
            {
                if (getMultiBlock().get().getStackInSlot(0) == null)
                {
                    getMultiBlock().get().setInventorySlotContents(0, getStackInSlot(0));
                    setInventorySlotContents(0, null);
                }
            }

            if (tank.getFluidAmount() > 0)
            {
                getMultiBlock().get().tank.fill(tank.drain(tank.getCapacity(), true), true);
            }
        }

        if (!worldObj.isRemote)
        {
            if (getMultiBlock().isPrimary() && tank.getFluid() != null && tank.getFluid().fluidID == Atomic.FLUID_PLASMA.getID())
            {
                /** Spawn plasma */
                FluidStack drain = tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false);

                if (drain != null && drain.amount >= FluidContainerRegistry.BUCKET_VOLUME)
                {
                    ForgeDirection spawnDir = ForgeDirection.getOrientation(worldObj.rand.nextInt(3) + 2);
                    Vector3 spawnPos = new Vector3(this).translate(spawnDir, 2);
                    spawnPos.translate(0, Math.max(worldObj.rand.nextInt(getHeight()) - 1, 0), 0);

                    if (worldObj.isAirBlock(spawnPos.intX(), spawnPos.intY(), spawnPos.intZ()))
                    {
                        MinecraftForge.EVENT_BUS.post(new SpawnPlasmaEvent(worldObj, spawnPos.intX(), spawnPos.intY(), spawnPos.intZ(), TilePlasma.plasmaMaxTemperature));
                        tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                    }
                }
            }
            else
            {
                prevInternalEnergy = internalEnergy;

                /** Handle cell rod interactions. */
                ItemStack fuelRod = getMultiBlock().get().getStackInSlot(0);

                if (fuelRod != null)
                {
                    if (fuelRod.getItem() instanceof IReactorComponent)
                    {
                        // Activate rods.
                        ((IReactorComponent) fuelRod.getItem()).onReact(fuelRod, this);

                        if (!worldObj.isRemote)
                        {
                            if (fuelRod.getItemDamage() >= fuelRod.getMaxDamage())
                            {
                                getMultiBlock().get().setInventorySlotContents(0, null);
                            }
                        }

                        // Emit Radiations
                        if (ticks % 20 == 0)
                        {
                            if (worldObj.rand.nextFloat() > 0.65)
                            {
                                List<EntityLiving> entities = worldObj.getEntitiesWithinAABB(EntityLiving.class,
                                        AxisAlignedBB.getBoundingBox(xCoord - RADIUS * 2, yCoord - RADIUS * 2, zCoord - RADIUS * 2, xCoord + RADIUS * 2, yCoord + RADIUS * 2, zCoord + RADIUS * 2));

                                for (EntityLiving entity : entities)
                                {
                                    PoisonRadiation.INSTANCE.poisonEntity(new Vector3(this), entity);
                                }
                            }
                        }
                    }
                }

                // Update the temperature from the thermal grid.
                temperature = ThermalGrid.getTemperature(new VectorWorld(this));

                /** Only a small percentage of the internal energy is used for temperature. */
                if (internalEnergy - prevInternalEnergy > 0)
                {
                    float deltaT = ThermalPhysics.getTemperatureForEnergy(mass, specificHeatCapacity, (long) ((internalEnergy - prevInternalEnergy) * 0.15));

                    /** Check control rods */
                    int rods = 0;

                    for (int i = 2; i < 6; i++)
                    {
                        Vector3 checkAdjacent = new Vector3(this).translate(ForgeDirection.getOrientation(i));

                        if (checkAdjacent.getBlockID(worldObj) == Atomic.blockControlRod.blockID)
                        {
                            deltaT /= 1.1;
                            rods++;
                        }
                    }

                    // Add heat to surrounding blocks in the thermal grid.
                    ThermalGrid.addTemperature(new VectorWorld(this), deltaT);

                    // Sound of lava flowing randomly plays when above temperature to boil water.
                    if (worldObj.rand.nextInt(80) == 0 && this.getTemperature() >= 373)
                    {
                        worldObj.playSoundEffect(this.xCoord + 0.5F, this.yCoord + 0.5F, this.zCoord + 0.5F, "Fluid.lava", 0.5F, 2.1F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.85F);
                    }

                    // Sounds of lava popping randomly plays when above temperature to boil water.
                    if (worldObj.rand.nextInt(40) == 0 && this.getTemperature() >= 373)
                    {
                        worldObj.playSoundEffect(this.xCoord + 0.5F, this.yCoord + 0.5F, this.zCoord + 0.5F, "Fluid.lavapop", 0.5F, 2.6F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.8F);
                    }

                    // Reactor cell plays random idle noises while operating and above temperature to boil water.
                    if (worldObj.getWorldTime() % (Atomic.SECOND_IN_TICKS * 5.0F) == 0 && this.getTemperature() >= 373)
                    {
                        float percentage = Math.min(this.getTemperature() / TileReactorCell.MELTING_POINT, 1.0F);
                        worldObj.playSoundEffect(this.xCoord + 0.5F, this.yCoord + 0.5F, this.zCoord + 0.5F, "atomicscience:reactorcell", percentage, 1.0F);
                        // AtomicScience.LOGGER.info("REACTOR SOUND");
                    }

                    if (previousTemperature != temperature && !shouldUpdate)
                    {
                        shouldUpdate = true;
                        previousTemperature = temperature;
                        // System.out.println("[Atomic Science] [Thermal Grid] Temperature: " + String.valueOf(previousTemperature));
                    }

                    if (previousTemperature >= MELTING_POINT && meltdownCounter < meltdownCounterMaximum)
                    {
                        shouldUpdate = true;
                        meltdownCounter++;
                        // System.out.println("[Atomic Science] [Reactor Cell] Meltdown Ticker: " + String.valueOf(temperature) + " @ " + String.valueOf(meltdownCounter) + "/" + String.valueOf(meltdownCounterMaximum));
                    }

                    if (previousTemperature >= MELTING_POINT && meltdownCounter >= meltdownCounterMaximum)
                    {
                        // System.out.println("[Atomic Science] [Reactor Cell] Meltdown Ticker: REACTOR MELTDOWN!");
                        meltdownCounter = 0;
                        meltDown();
                        return;
                    }
                    else
                    {
                        // Reset meltdown ticker to give the reactor more of a 'goldilocks zone'.
                        meltdownCounter = 0;
                    }
                }

                internalEnergy = 0;

                if (isOverToxic())
                {
                    /** Randomly leak toxic waste when it is too toxic */
                    VectorWorld leakPos = new VectorWorld(this).translate(worldObj.rand.nextInt(20) - 10, worldObj.rand.nextInt(20) - 10, worldObj.rand.nextInt(20) - 10);

                    int blockID = leakPos.getBlockID();

                    if (blockID == Block.grass.blockID)
                    {
                        leakPos.setBlock(worldObj, Atomic.blockRadioactive.blockID);
                        tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                    }
                    else if (blockID == 0 || Block.blocksList[blockID].isBlockReplaceable(worldObj, leakPos.intX(), leakPos.intY(), leakPos.intZ()))
                    {
                        if (tank.getFluid() != null)
                        {
                            leakPos.setBlock(worldObj, tank.getFluid().getFluid().getBlockID());
                            tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                        }
                    }
                }

            }

            if (ticks % 60 == 0 || shouldUpdate)
            {
                shouldUpdate = false;
                notifyChange();
                PacketHandler.sendPacketToClients(getDescriptionPacket(), worldObj, new Vector3(this), 50);
            }
        }
        else
        {

            // Particles of white smoke will rise from above the reactor chamber when above water boiling temperature.
            if (worldObj.rand.nextInt(5) == 0 && this.getTemperature() >= 373)
            {
                worldObj.spawnParticle("cloud", this.xCoord + worldObj.rand.nextInt(2), this.yCoord + 1.0F, this.zCoord + worldObj.rand.nextInt(2), 0, 0.1D, 0);
                worldObj.spawnParticle("bubble", this.xCoord + worldObj.rand.nextInt(5), this.yCoord, this.zCoord + worldObj.rand.nextInt(5), 0, 0, 0);
            }
        }
    }

    @Override
    public boolean isOverToxic()
    {
        return tank.getFluid() != null && tank.getFluid().fluidID == Atomic.FLUID_TOXIC_WASTE.getID() && tank.getFluid().amount >= tank.getCapacity();
    }

    /** Multiblock Methods */
    public void updatePositionStatus()
    {
        TileReactorCell mainTile = getLowest();
        mainTile.getMultiBlock().deconstruct();
        mainTile.getMultiBlock().construct();

        boolean top = new Vector3(this).add(new Vector3(0, 1, 0)).getTileEntity(worldObj) instanceof TileReactorCell;
        boolean bottom = new Vector3(this).add(new Vector3(0, -1, 0)).getTileEntity(worldObj) instanceof TileReactorCell;

        if (top && bottom)
        {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
        }
        else if (top)
        {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
        }
        else
        {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 2, 3);
        }
    }

    @Override
    public void onMultiBlockChanged()
    {

    }

    @Override
    public Vector3[] getMultiBlockVectors()
    {
        List<Vector3> vectors = new ArrayList<Vector3>();

        Vector3 checkPosition = new Vector3(this);

        while (true)
        {
            TileEntity t = checkPosition.getTileEntity(this.worldObj);

            if (t instanceof TileReactorCell)
            {
                vectors.add(checkPosition.clone().subtract(getPosition()));
            }
            else
            {
                break;
            }

            checkPosition.y++;
        }

        return vectors.toArray(new Vector3[0]);
    }

    public TileReactorCell getLowest()
    {
        TileReactorCell lowest = this;
        Vector3 checkPosition = new Vector3(this);

        while (true)
        {
            TileEntity t = checkPosition.getTileEntity(this.worldObj);

            if (t instanceof TileReactorCell)
            {
                lowest = (TileReactorCell) t;
            }
            else
            {
                break;
            }

            checkPosition.y--;
        }

        return lowest;
    }

    @Override
    public World getWorld()
    {
        return worldObj;
    }

    @Override
    public Vector3 getPosition()
    {
        return new Vector3(this);
    }

    @Override
    public MultiBlockHandler<TileReactorCell> getMultiBlock()
    {
        if (multiBlock == null)
        {
            multiBlock = new MultiBlockHandler<TileReactorCell>(this);
        }

        return multiBlock;
    }

    public int getHeight()
    {
        int height = 0;
        Vector3 checkPosition = new Vector3(this);
        TileEntity tile = this;

        while (tile instanceof TileReactorCell)
        {
            checkPosition.y++;
            height++;
            tile = checkPosition.getTileEntity(worldObj);
        }

        return height;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return ResonantInduction.PACKET_ANNOTATION.getPacket(this);
    }

    private void meltDown()
    {
        if (!worldObj.isRemote)
        {
            // No need to destroy reactor cell since explosion will do that for us.
            ReactorExplosion reactorExplosion = new ReactorExplosion(worldObj, null, xCoord, yCoord, zCoord, 9f);
            reactorExplosion.doExplosionA();
            reactorExplosion.doExplosionB(true);
        }
    }

    /** Reads a tile entity from NBT. */
    @SyncedInput
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        temperature = nbt.getFloat("temperature");
        tank.readFromNBT(nbt);
        getMultiBlock().load(nbt);
    }

    /** Writes a tile entity to NBT. */
    @SyncedOutput
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setFloat("temperature", temperature);
        tank.writeToNBT(nbt);
        getMultiBlock().save(nbt);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    /** Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item, side */
    @Override
    public boolean canInsertItem(int slot, ItemStack items, int side)
    {
        return this.isItemValidForSlot(slot, items);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this ? false : par1EntityPlayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public String getInvName()
    {
        return getBlockType().getLocalizedName();
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        if (getMultiBlock().isPrimary() && getMultiBlock().get().getStackInSlot(0) == null)
        {
            return itemStack.getItem() instanceof IReactorComponent;
        }

        return false;
    }

    /** Fluid Functions. */
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        return getMultiBlock().get().tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (resource == null || !resource.isFluidEqual(tank.getFluid()))
        {
            return null;
        }

        return tank.drain(resource.amount, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return fluid == Atomic.FLUID_PLASMA;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return fluid == Atomic.FLUID_TOXIC_WASTE;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[]
        { tank.getInfo() };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (getMultiBlock().isPrimary() && getMultiBlock().isConstructed())
        {
            return INFINITE_EXTENT_AABB;
        }

        return super.getRenderBoundingBox();
    }

    @Override
    public void heat(long energy)
    {
        internalEnergy = Math.max(internalEnergy + energy, 0);
    }

    @Override
    public float getTemperature()
    {
        return temperature;
    }
}
