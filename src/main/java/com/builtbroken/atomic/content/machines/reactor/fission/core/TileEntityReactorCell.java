package com.builtbroken.atomic.content.machines.reactor.fission.core;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.api.item.IFuelRodItem;
import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.api.reactor.IFissionReactor;
import com.builtbroken.atomic.api.thermal.IThermalSource;
import com.builtbroken.atomic.client.EffectRefs;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.machines.TileEntityInventoryMachine;
import com.builtbroken.atomic.content.machines.reactor.fission.controller.TileEntityReactorController;
import com.builtbroken.atomic.lib.inventory.ItemStackHandlerWrapper;
import com.builtbroken.atomic.map.data.node.MapDataSources;
import com.builtbroken.atomic.map.exposure.node.RadSourceTile;
import com.builtbroken.atomic.map.thermal.node.ThermalSource;
import com.builtbroken.atomic.map.thermal.node.ThermalSourceTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public class TileEntityReactorCell extends TileEntityInventoryMachine<IItemHandlerModifiable> implements IFissionReactor
{
    public static final int SLOT_FUEL_ROD = 0;
    public static final int[] ACCESSIBLE_SIDES = new int[]{SLOT_FUEL_ROD};
    /** Client side */
    private boolean _running = false;
    private boolean _renderFuel = false;
    private float _renderFuelLevel = 0f;

    public boolean enabled = true; ///TODO add a spin up and down time, prevent instant enable/disable of reactors

    private final RadSourceTile<TileEntityReactorCell> radiationSource = new RadSourceTile(this, () -> getRadioactiveMaterial());
    private final ThermalSource<TileEntityReactorCell> thermalSource = new ThermalSourceTile(this, () -> getHeatGenerated());

    @Override
    protected void firstTick(boolean isClient)
    {
        super.firstTick(isClient);
        updateStructureType();
        if (!isClient)
        {
            MapDataSources.addSource(getRadiationSource());
            MapDataSources.addSource(getHeatSource());
        }
    }

    @Override
    protected IItemHandlerModifiable createInternalInventory()
    {
        return new ItemStackHandler(inventorySize())
        {
            @Override
            public void setStackInSlot(int slot, @Nonnull ItemStack stack)
            {
                validateSlotIndex(slot);
                ItemStack prev = getStackInSlot(slot);
                this.stacks.set(slot, stack);

                if (!ItemStack.areItemStacksEqual(prev, stack))
                {
                    onSlotStackChanged(slot, prev, stack);
                }

                onContentsChanged(slot);
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                return stack.getItem() instanceof IFuelRodItem;
            }

            @Override
            public int getSlotLimit(int slot)
            {
                return 1;
            }
        };
    }

    @Nonnull
    @Override
    protected IItemHandlerModifiable createInventory()
    {
        return new ItemStackHandlerWrapper(getInventory())
        {
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate)
            {
                if (slot == SLOT_FUEL_ROD)
                {
                    ItemStack slot_stack = getStackInSlot(SLOT_FUEL_ROD);
                    if (!(slot_stack.getItem() instanceof IFuelRodItem) || ((IFuelRodItem) slot_stack.getItem()).getFuelRodRuntime(slot_stack, TileEntityReactorCell.this) <= 0)
                    {
                        return inventory.extractItem(slot, amount, simulate);
                    }
                }
                return inventory.extractItem(slot, amount, simulate);
            }
        };
    }

    @Override
    protected int inventorySize()
    {
        return 1;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == AtomicScienceAPI.THERMAL_CAPABILITY || capability == AtomicScienceAPI.RADIATION_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == AtomicScienceAPI.THERMAL_CAPABILITY)
        {
            return (T) thermalSource;
        }
        else if (capability == AtomicScienceAPI.RADIATION_CAPABILITY)
        {
            return (T) radiationSource;
        }
        return super.getCapability(capability, facing);
    }

    //-----------------------------------------------
    //--------Runtime logic -------------------------
    //-----------------------------------------------

    @Override
    public void update(int ticks, boolean isClient)
    {
        super.update(ticks, isClient);
        if (!isClient)
        {
            doRodMovement(ticks);
            doRunChecks(ticks);
        }
        else if (_running)
        {
            AtomicScience.sideProxy.spawnParticle(EffectRefs.REACTOR_RUNNING, xi() + 0.5, yi() + 0.5, zi() + 0.5, 0, 0, 0);
        }
    }

    protected void doRunChecks(int ticks)
    {
        //Track previous state
        final boolean prev_running = _running;

        //Check if we can operate
        if (canOperate())
        {
            //Set run status
            _running = true;

            //Consume fuel
            consumeFuel(ticks);

            //Every 1 second, do operation tick
            if (ticks % 20 == 0)
            {
                doOperationTick();
            }
        }
        //if not in operation status, set running to false
        else
        {
            _running = false;
        }

        //If state changed cycle grid
        if (prev_running != _running)
        {
            onRunStateChanged();
        }

        //If state changes or every so often sync data to client
        if (prev_running != _running || ticks % 20 == 0) //TODO see if %20 is needed
        {
            syncClientNextTick();
        }
    }

    protected void onRunStateChanged()
    {

    }

    /**
     * Do logic for rod movement between tiles
     * <p>
     * Does not always run every tick but calls logic to check if can/should run
     *
     * @param ticks - current tile ticks
     */
    protected void doRodMovement(int ticks)
    {
        //Every 5 seconds, Check if we need to move rods (works like a hopper)
        if (ticks % 100 == 0)
        {
            IFuelRodItem fuelRod = getFuelRod();
            if (fuelRod != null)
            {
                int runtime = fuelRod.getFuelRodRuntime(getFuelRodStack(), this);
                //Try to move rod to cell below reactor
                TileEntity tile = world.getTileEntity(getPos().down());
                if (tile instanceof TileEntityReactorCell)
                {
                    tryToMoveRod((TileEntityReactorCell) tile);
                }
                //Skip over reactor trying to insert into next reactor
                else if (tile instanceof TileEntityReactorController && runtime > 0)
                {
                    tile = world.getTileEntity(getPos().down(2));
                    if (tile instanceof TileEntityReactorCell)
                    {
                        tryToMoveRod((TileEntityReactorCell) tile);
                    }
                }
                //Rod is dead try to eject to inventory
                else if (tile != null && runtime <= 0)
                {
                    if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP))
                    {
                        IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
                        if (inventory != null)
                        {
                            for (int slot = 0; slot < inventory.getSlots() && !getFuelRodStack().isEmpty(); slot++)
                            {
                                if (inventory.isItemValid(slot, getFuelRodStack()))
                                {
                                    setFuelRod(inventory.insertItem(slot, getFuelRodStack(), false));
                                }
                            }
                        }
                    }

                    if (getFuelRod() == null && AtomicScience.runningAsDev)
                    {
                        AtomicScience.logger.info(this + " ejected spent rod");
                    }
                }
            }
        }
    }

    /**
     * Do reactor cell movement
     *
     * @param cell - reactor cell to move item into
     */
    protected void tryToMoveRod(TileEntityReactorCell cell)
    {
        //always move lowest rod to bottom of stack (ensures dead rods exit core)
        if (cell.getFuelRod() != null)
        {
            int runTime = getFuelRuntime();
            int otherRunTime = cell.getFuelRuntime();

            if (runTime < otherRunTime)
            {
                ItemStack stack = cell.getFuelRodStack();
                cell.setFuelRod(getFuelRodStack());
                setFuelRod(stack);
                if (AtomicScience.runningAsDev)
                {
                    AtomicScience.logger.info(this + " switched rods with lower reactor");
                }
            }
        }
        //If not rod in lower core, move cell
        else
        {
            cell.setFuelRod(getFuelRodStack());
            setFuelRod(ItemStack.EMPTY);
            if (AtomicScience.runningAsDev)
            {
                AtomicScience.logger.info(this + " moved rod to lower reactor");
            }
        }
    }

    protected void doOperationTick()
    {
        //TODO calculate radioactive material leaking
        //TODO dump radioactive material to area or drains
    }

    protected int getActualHeat(int heat)
    {
        //TODO figure out bonus and negative to heat generation (control rods decrease, reactors nearby increase)
        return heat;
    }

    protected void consumeFuel(int ticks)
    {
        IFuelRodItem fuelRodItem = getFuelRod();
        if (fuelRodItem != null)
        {
            getInventory().setStackInSlot(0, fuelRodItem.onReactorTick(this, getFuelRodStack(), ticks, getFuelRuntime()));
        }
    }

    protected boolean canOperate()
    {
        //TODO check for safety (water, temp, etc)
        //TODO check if can generate neutrons (controls rods can force off)
        return enabled && hasFuel() && getFuelRuntime() > 0;
    }

    protected void onSlotStackChanged(int slot, ItemStack prev_stack, ItemStack new_stack)
    {
        this.markDirty();
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        if (isServer())
        {
            MapDataSources.removeSource(getRadiationSource());
            MapDataSources.removeSource(getHeatSource());
        }
    }

    //-----------------------------------------------
    //--------Accessors -----------------------------
    //-----------------------------------------------

    public boolean hasFuel()
    {
        return getFuelRod() != null;
    }

    public int getFuelRuntime()
    {
        IFuelRodItem fuelRod = getFuelRod();
        if (fuelRod != null)
        {
            return fuelRod.getFuelRodRuntime(getFuelRodStack(), this);
        }
        return 0;
    }

    public int getMaxFuelRuntime()
    {
        IFuelRodItem fuelRod = getFuelRod();
        if (fuelRod != null)
        {
            return fuelRod.getMaxFuelRodRuntime(getFuelRodStack(), this);
        }
        return 0;
    }

    public IFuelRodItem getFuelRod()
    {
        ItemStack stack = getFuelRodStack();
        return !stack.isEmpty() && stack.getItem() instanceof IFuelRodItem ? (IFuelRodItem) stack.getItem() : null;
    }

    public void setFuelRod(ItemStack stack)
    {
        getInventory().setStackInSlot(SLOT_FUEL_ROD, stack);
    }

    @Override
    public ItemStack getFuelRodStack()
    {
        return getInventory().getStackInSlot(SLOT_FUEL_ROD);
    }

    public int getRadioactiveMaterial()
    {
        IFuelRodItem fuelRod = getFuelRod();
        if (fuelRod != null)
        {
            return fuelRod.getRadioactiveMaterial(getFuelRodStack(), this);
        }
        return 0;
    }

    public int getHeatGenerated()
    {
        IFuelRodItem fuelRodItem = getFuelRod();
        if (fuelRodItem != null)
        {
            return getActualHeat(fuelRodItem.getHeatOutput(getFuelRodStack(), this));
        }
        return 0;
    }

    //@Override TODO
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return slot == SLOT_FUEL_ROD && stack.getItem() instanceof IFuelRodItem;
    }

    //@Override
    public boolean canExtractItem(int slot, ItemStack stack, int side)
    {
        if (SLOT_FUEL_ROD == slot)
        {
            if (stack.getItem() instanceof IFuelRodItem)
            {
                return ((IFuelRodItem) stack.getItem()).getFuelRodRuntime(stack, this) <= 0;
            }
            return true;
        }
        return false;
    }

    //-----------------------------------------------
    //--------Rendering props -----------------------
    //-----------------------------------------------

    public float getFuelRenderLevel()
    {
        if (isServer())
        {
            return (float) getFuelRuntime() / (float) getMaxFuelRuntime();
        }
        return _renderFuelLevel;
    }

    //-----------------------------------------------
    //--------Network code -------------------------
    //-----------------------------------------------

    @Override
    protected void writeDescPacket(List<Object> dataList, EntityPlayer player)
    {
        super.writeDescPacket(dataList, player);
        dataList.add(_running || canOperate());
        dataList.add(hasFuel());
        dataList.add(getFuelRenderLevel());
    }

    @Override
    protected void readDescPacket(ByteBuf buf, EntityPlayer player)
    {
        super.readDescPacket(buf, player);
        _running = buf.readBoolean();
        _renderFuel = buf.readBoolean();
        _renderFuelLevel = buf.readFloat();
    }

    //-----------------------------------------------
    //--------Structure code -------------------------
    //-----------------------------------------------

    public void updateStructureType()
    {
        IBlockState blockAbove = world.getBlockState(getPos().up());
        IBlockState blockBelow = world.getBlockState(getPos().down());

        if (canConnect(blockAbove) && canConnect(blockBelow))
        {
            setStructureType(ReactorStructureType.MIDDLE);
        }
        else if (canConnect(blockBelow))
        {
            setStructureType(ReactorStructureType.TOP);
        }
        else if (canConnect(blockAbove))
        {
            setStructureType(ReactorStructureType.BOTTOM);
        }
        else
        {
            setStructureType(ReactorStructureType.NORMAL);
        }
    }

    public void setStructureType(ReactorStructureType structureType)
    {
        IBlockState blockState = world.getBlockState(getPos());
        if (blockState.getProperties().containsKey(BlockReactorCell.REACTOR_STRUCTURE_TYPE))
        {
            ReactorStructureType type = blockState.getValue(BlockReactorCell.REACTOR_STRUCTURE_TYPE);
            if (type != structureType)
            {
                world.setBlockState(pos, blockState.withProperty(BlockReactorCell.REACTOR_STRUCTURE_TYPE, structureType));
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    private boolean canConnect(IBlockState block)
    {
        return block.getBlock() == ASBlocks.blockReactorCell || block.getBlock() == ASBlocks.blockReactorController;
    }

    @Override
    public IRadiationSource getRadiationSource()
    {
        return radiationSource;
    }

    @Override
    public IThermalSource getHeatSource()
    {
        return thermalSource;
    }

    @Override
    public String toString()
    {
        return "ReactorCell[W: " + worldName() + " | D: " + dim() + " | Pos(" + xi() + ", " + yi() + ", " + zi() + ")]@" + hashCode();
    }
}
