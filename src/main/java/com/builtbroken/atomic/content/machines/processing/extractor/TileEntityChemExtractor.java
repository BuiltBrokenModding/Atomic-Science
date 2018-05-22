package com.builtbroken.atomic.content.machines.processing.extractor;

import com.builtbroken.atomic.config.ConfigRecipe;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.machines.TileEntityPowerInvMachine;
import com.builtbroken.atomic.lib.gui.IGuiTile;
import com.builtbroken.atomic.lib.power.PowerSystem;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/19/2018.
 */
public class TileEntityChemExtractor extends TileEntityPowerInvMachine implements IFluidHandler, IGuiTile, ISidedInventory
{
    public static final int SLOT_FLUID_INPUT = 0;
    public static final int SLOT_ITEM_INPUT = 1;
    public static final int SLOT_ITEM_OUTPUT = 2;
    public static final int SLOT_BATTERY = 3;
    public static final int SLOT_FLUID_OUTPUT = 4;
    public static final int INVENTORY_SIZE = 5;
    public static final int[] INPUT_SLOTS = new int[]{SLOT_ITEM_INPUT};
    public static final int[] OUTPUT_SLOTS = new int[]{SLOT_ITEM_OUTPUT};
    public static final int[] ACCESSIBLE_SLOTS = new int[]{SLOT_ITEM_INPUT, SLOT_ITEM_OUTPUT};

    public static int PROCESSING_TIME = 100;
    public static int ENERGY_PER_TICK = 100;

    private final FluidTank inputTank;
    private final FluidTank outputTank;

    boolean processing = false;
    int processTimer = 0;

    ForgeDirection _facingDirectionCache;

    float _processingAnimationRotationPrev = 0;
    float _processingAnimationRotation = 0;

    public TileEntityChemExtractor()
    {
        inputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        outputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
    }

    @Override
    public void update(int ticks)
    {
        super.update(ticks);
        if (isServer())
        {
            fillTank();
            drainBattery();
            if (processing)
            {
                if (processTimer++ >= PROCESSING_TIME)
                {
                    doProcess();
                    checkRecipe();
                }
            }
            else if (ticks % 20 == 0)
            {
                checkRecipe();
            }
            outputFluids();
        }
        else if (processTimer > 0)
        {
            _processingAnimationRotation += 5f; //TODO move to val
            if (_processingAnimationRotation > 360)
            {
                _processingAnimationRotation -= 360;
                _processingAnimationRotationPrev -= 360;
            }
        }
    }

    @Override
    protected void onSlotStackChanged(ItemStack prev, ItemStack stack, int slot)
    {
        if (isServer() && slot == SLOT_ITEM_INPUT)
        {
            checkRecipe();
        }
        super.onSlotStackChanged(prev, stack, slot);
    }

    /**
     * Pulls fluids from container and insert into tank
     */
    protected void fillTank()
    {
        ItemStack itemStack = getStackInSlot(SLOT_FLUID_INPUT);
        if (itemStack != null)
        {
            if (itemStack.getItem() instanceof IFluidContainerItem)
            {
                IFluidContainerItem fluidContainerItem = (IFluidContainerItem) itemStack.getItem();

                FluidStack fluidStack = fluidContainerItem.getFluid(itemStack);
                if (fluidStack != null && fluidStack.getFluid() == FluidRegistry.WATER)
                {
                    fluidStack = fluidContainerItem.drain(itemStack, getInputTank().getCapacity() - getInputTank().getFluidAmount(), false);
                    int amount = getInputTank().fill(fluidStack, true);
                    fluidContainerItem.drain(itemStack, amount, true);
                    setInventorySlotContents(SLOT_FLUID_INPUT, itemStack);
                }
            }
            else if (FluidContainerRegistry.isFilledContainer(itemStack))
            {
                FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
                if (stack != null && stack.getFluid() == FluidRegistry.WATER
                        && canOutputFluid(getInputTank(), FluidRegistry.WATER, stack.amount))
                {
                    getInputTank().fill(stack, true);
                    decrStackSize(SLOT_FLUID_INPUT, 1);

                    ItemStack container = itemStack.getItem().getContainerItem(itemStack);
                    if (container != null)
                    {
                        if (getStackInSlot(SLOT_FLUID_INPUT) == null)
                        {
                            setInventorySlotContents(SLOT_FLUID_INPUT, container);
                        }
                        else
                        {
                            //TODO add fluid container output slot
                            EntityItem item = new EntityItem(worldObj);
                            item.setPosition(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                            item.setEntityItemStack(container);
                            worldObj.spawnEntityInWorld(item);
                        }
                    }
                }
            }
        }
    }

    protected void outputFluids()
    {
        ItemStack itemStack = getStackInSlot(SLOT_FLUID_OUTPUT);
        if (itemStack != null && outputTank.getFluid() != null)
        {
            if (itemStack.getItem() instanceof IFluidContainerItem)
            {
                //Copy stack (fix for containers that can stack when empty)
                final ItemStack fluidContainer = itemStack.copy();
                fluidContainer.stackSize = 1;

                IFluidContainerItem fluidContainerItem = (IFluidContainerItem) fluidContainer.getItem();
                FluidStack fluidStack = fluidContainerItem.getFluid(fluidContainer);
                if (fluidStack == null || fluidStack.getFluid() == outputTank.getFluid().getFluid())
                {
                    int filled = fluidContainerItem.fill(fluidContainer, getOutputTank().getFluid(), true);
                    getOutputTank().drain(filled, true);

                    if(itemStack.stackSize == 1)
                    {
                        setInventorySlotContents(SLOT_FLUID_OUTPUT, fluidContainer);
                    }
                    else
                    {
                        decrStackSize(SLOT_FLUID_OUTPUT, 1);

                        //TODO add fluid container output slot
                        EntityItem item = new EntityItem(worldObj);
                        item.setPosition(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                        item.setEntityItemStack(fluidContainer);
                        worldObj.spawnEntityInWorld(item);
                    }
                }
            }
            else if (FluidContainerRegistry.isEmptyContainer(itemStack))
            {
                ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(getOutputTank().getFluid(), itemStack);
                if (filledContainer != null)
                {
                    FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(filledContainer);
                    if (fluidStack.getFluid() == outputTank.getFluid().getFluid() && fluidStack.amount <= outputTank.getFluidAmount())
                    {
                        outputTank.drain(fluidStack.amount, true);
                        decrStackSize(SLOT_FLUID_OUTPUT, 1);

                        if (getStackInSlot(SLOT_FLUID_OUTPUT) == null)
                        {
                            setInventorySlotContents(SLOT_FLUID_OUTPUT, filledContainer);
                        }
                        else
                        {
                            //TODO add fluid container output slot
                            EntityItem item = new EntityItem(worldObj);
                            item.setPosition(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                            item.setEntityItemStack(filledContainer);
                            worldObj.spawnEntityInWorld(item);
                        }
                    }
                }
            }
        }

        if (getOutputTank().getFluid() != null)
        {
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            {
                int x = xCoord + direction.offsetX;
                int y = yCoord + direction.offsetY;
                int z = zCoord + direction.offsetZ;

                if (worldObj.blockExists(x, y, z))
                {
                    TileEntity tile = worldObj.getTileEntity(x, y, z);
                    if (tile instanceof IFluidHandler && getOutputTank().getFluid() != null && ((IFluidHandler) tile).canFill(direction.getOpposite(), getOutputTank().getFluid().getFluid()))
                    {
                        int fill = ((IFluidHandler) tile).fill(direction.getOpposite(), getOutputTank().getFluid(), true);
                        outputTank.drain(fill, true);
                    }
                }
            }
        }
    }

    protected void drainBattery()
    {
        ItemStack itemStack = getStackInSlot(SLOT_BATTERY);
        int power = PowerSystem.getEnergyStored(itemStack);
        if (power > 0)
        {
            power = PowerSystem.removePower(itemStack, power, false);
            int added = addEnergy(power, true);
            PowerSystem.removePower(itemStack, added, true);
            setInventorySlotContents(SLOT_BATTERY, itemStack);
        }
    }

    protected void doProcess()
    {
        processTimer = 0;

        //Uranium Ore recipe
        ItemStack inputItem = getStackInSlot(SLOT_ITEM_INPUT);
        //TODO move recipe to object

        if (Item.getItemFromBlock(ASBlocks.blockUraniumOre) == inputItem.getItem()
                && hasInputFluid(getInputTank(), FluidRegistry.WATER, ConfigRecipe.WATER_USED_YELLOW_CAKE)
                && canOutputFluid(getOutputTank(), ASFluids.LIQUID_MINERAL_WASTE.fluid, ConfigRecipe.LIQUID_WASTE_PRODUCED_YELLOW_CAKE))

        {
            ItemStack outputStack = new ItemStack(ASItems.itemYellowCake, ConfigRecipe.YELLOW_CAKE_PER_ORE, 0);
            if (hasSpaceInOutput(outputStack))
            {
                decrStackSize(SLOT_ITEM_INPUT, 1);
                getInputTank().drain(ConfigRecipe.WATER_USED_YELLOW_CAKE, true);
                getOutputTank().fill(new FluidStack(ASFluids.LIQUID_MINERAL_WASTE.fluid, ConfigRecipe.LIQUID_WASTE_PRODUCED_YELLOW_CAKE), true);
                addToOutput(outputStack);
            }
        }
    }

    protected boolean hasInputFluid(IFluidTank tank, Fluid fluid, int amount)
    {
        FluidStack inputFluidStack = tank.getFluid();
        return inputFluidStack != null
                && inputFluidStack.getFluid() == fluid
                && inputFluidStack.amount >= amount;
    }

    protected boolean canOutputFluid(IFluidTank tank, Fluid fluid, int amount)
    {
        FluidStack outputFluidStack = tank.getFluid();
        return outputFluidStack == null && tank.getCapacity() >= amount
                || outputFluidStack.getFluid() == fluid
                && (tank.getCapacity() - outputFluidStack.amount) >= amount;
    }

    protected boolean hasSpaceInOutput(ItemStack insertStack)
    {
        ItemStack stackInSlot = getStackInSlot(SLOT_ITEM_OUTPUT);
        if (stackInSlot == null)
        {
            return true;
        }
        else if (stackInSlot.getItem() == insertStack.getItem() && stackInSlot.getItemDamage() == insertStack.getItemDamage())
        {
            return getInventoryStackLimit() - stackInSlot.stackSize >= insertStack.stackSize;
        }
        return false;
    }

    protected void addToOutput(ItemStack insertStack)
    {
        ItemStack stackInSlot = getStackInSlot(SLOT_ITEM_OUTPUT);
        if (stackInSlot == null)
        {
            setInventorySlotContents(SLOT_ITEM_OUTPUT, insertStack);
        }
        else if (stackInSlot.getItem() == insertStack.getItem() && stackInSlot.getItemDamage() == insertStack.getItemDamage())
        {
            stackInSlot.stackSize += insertStack.stackSize;
            stackInSlot.stackSize = Math.min(stackInSlot.stackSize, stackInSlot.getMaxStackSize());
            stackInSlot.stackSize = Math.min(stackInSlot.stackSize, getInventoryStackLimit());
        }
    }

    protected boolean canProcess()
    {
        ItemStack stack = getStackInSlot(SLOT_ITEM_INPUT);
        if (stack != null)
        {
            return Item.getItemFromBlock(ASBlocks.blockUraniumOre) == stack.getItem()
                    && hasInputFluid(getInputTank(), FluidRegistry.WATER, ConfigRecipe.WATER_USED_YELLOW_CAKE);  //TODO move recipe to object
        }
        return false;
    }

    protected void checkRecipe()
    {
        processing = canProcess();
        if (!processing)
        {
            processTimer = 0;
        }
        //Set to 1 for client sync
        else if (processTimer == 0)
        {
            processTimer = 1;
        }
        syncClientNextTick();
    }

    //-----------------------------------------------
    //--------Fluid Tank Handling -------------------
    //-----------------------------------------------

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (resource != null && canFill(from, resource.getFluid()))
        {
            Fluid fluid = getInputTank().getFluid() != null ? getInputTank().getFluid().getFluid() : null;
            int amount = getInputTank().getFluidAmount();

            int fill = getInputTank().fill(resource, doFill);

            if (doFill && getInputTank().getFluid() != null && (fluid != getInputTank().getFluid().getFluid()) || getInputTank().getFluidAmount() != amount)
            {
                checkRecipe();
            }

            return fill;
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (getOutputTank().getFluid() != null && resource.getFluid() == getOutputTank().getFluid().getFluid())
        {
            return drain(from, resource.amount, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return getOutputTank().drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return fluid == FluidRegistry.WATER;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return fluid == null || getOutputTank().getFluid() != null && getOutputTank().getFluid().getFluid() == fluid;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[]{getInputTank().getInfo(), getOutputTank().getInfo()};
    }

    public FluidTank getInputTank()
    {
        return inputTank;
    }

    public FluidTank getOutputTank()
    {
        return outputTank;
    }

    //-----------------------------------------------
    //--------Props ---------------------------------
    //-----------------------------------------------

    @Override
    public int getEnergyUsage()
    {
        return ENERGY_PER_TICK;
    }

    public ForgeDirection getFacingDirection()
    {
        if (_facingDirectionCache == null)
        {
            _facingDirectionCache = ForgeDirection.getOrientation(getBlockMetadata());
        }
        return _facingDirectionCache;
    }

    @Override
    public void markDirty()
    {
        if (isServer())
        {
            _facingDirectionCache = null;
        }
    }

    @SideOnly(Side.CLIENT)
    public float rotate(float delta)
    {
        _processingAnimationRotationPrev = _processingAnimationRotation + (_processingAnimationRotation - _processingAnimationRotationPrev) * delta;
        return _processingAnimationRotationPrev;
    }

    //-----------------------------------------------
    //--------GUI Handler ---------------------------
    //-----------------------------------------------

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player)
    {
        return new ContainerExtractor(player, this);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player)
    {
        return new GuiExtractor(player, this);
    }

    @Override
    protected void writeGuiPacket(List<Object> dataList, EntityPlayer player)
    {
        super.writeGuiPacket(dataList, player);
        dataList.add(processTimer);
        dataList.add(getInputTank());
        dataList.add(getOutputTank());
    }

    @Override
    protected void readGuiPacket(ByteBuf buf, EntityPlayer player)
    {
        super.readGuiPacket(buf, player);
        processTimer = buf.readInt();
        getInputTank().readFromNBT(ByteBufUtils.readTag(buf));
        getOutputTank().readFromNBT(ByteBufUtils.readTag(buf));
    }

    //-----------------------------------------------
    //--------Save/Load -----------------------------
    //-----------------------------------------------

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("processingProgress", processTimer);
        nbt.setTag("outputTank", getOutputTank().writeToNBT(new NBTTagCompound()));
        nbt.setTag("inputTank", getInputTank().writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        processTimer = nbt.getInteger("processingProgress");
        getOutputTank().readFromNBT(nbt.getCompoundTag("outputTank"));
        getInputTank().readFromNBT(nbt.getCompoundTag("inputTank"));
    }

    @Override
    protected void writeDescPacket(List<Object> dataList, EntityPlayer player)
    {
        super.writeGuiPacket(dataList, player);
        dataList.add(processTimer);
    }

    @Override
    protected void readDescPacket(ByteBuf buf, EntityPlayer player)
    {
        super.readGuiPacket(buf, player);
        processTimer = buf.readInt();
    }

    //-----------------------------------------------
    //--------Inventory Code ------------------------
    //-----------------------------------------------

    @Override
    public int getSizeInventory()
    {
        return INVENTORY_SIZE;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return ACCESSIBLE_SLOTS;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {
        return slot == SLOT_ITEM_INPUT;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side)
    {
        return slot == SLOT_ITEM_OUTPUT;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        if (slot == SLOT_ITEM_INPUT)
        {
            return stack.getItem() == Item.getItemFromBlock(ASBlocks.blockUraniumOre);
        }
        return false;
    }
}
