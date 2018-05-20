package com.builtbroken.atomic.content.machines.processing.extractor;

import com.builtbroken.atomic.config.ConfigRecipe;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.machines.TileEntityPowerInvMachine;
import com.builtbroken.atomic.lib.gui.IGuiTile;
import com.builtbroken.atomic.lib.power.PowerSystem;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/19/2018.
 */
public class TileEntityChemExtractor extends TileEntityPowerInvMachine implements IFluidHandler, IGuiTile
{
    public static final int SLOT_FLUID_INPUT = 0;
    public static final int SLOT_ITEM_INPUT = 1;
    public static final int SLOT_ITEM_OUTPUT = 2;
    public static final int SLOT_BATTERY = 3;
    public static final int SLOT_FLUID_OUTPUT = 4;
    public static final int INVENTORY_SIZE = 5;
    public static final int[] INPUT_SLOTS = new int[]{SLOT_ITEM_INPUT};
    public static final int[] OUTPUT_SLOTS = new int[]{SLOT_ITEM_OUTPUT};

    public static int PROCESSING_TIME = 100;
    public static int ENERGY_PER_TICK = 100;

    private final FluidTank inputTank;
    private final FluidTank outputTank;

    boolean processing = false;
    int processTimer = 0;

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
            if (processing && processTimer++ >= PROCESSING_TIME)
            {
                doProcess();
                checkRecipe();
            }
            outputFluids();
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
        //TODO export fluid from output tank
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
                && inputFluidStack.amount > amount;
    }

    protected boolean canOutputFluid(IFluidTank tank, Fluid fluid, int amount)
    {
        FluidStack outputFluidStack = tank.getFluid();
        return outputFluidStack == null && tank.getCapacity() >= amount
                || outputFluidStack.getFluid() == fluid
                && (tank.getCapacity() - outputFluidStack.amount) >= amount;
    }

    protected boolean hasSpaceInOutput(ItemStack stack)
    {
        ItemStack outputSlot = getStackInSlot(SLOT_ITEM_OUTPUT);
        if (outputSlot == null)
        {
            return true;
        }
        else if (ItemStack.areItemStacksEqual(outputSlot, stack))
        {
            return getInventoryStackLimit() - outputSlot.stackSize >= stack.stackSize;
        }
        return false;
    }

    protected void addToOutput(ItemStack stack)
    {
        ItemStack outputSlot = getStackInSlot(SLOT_ITEM_OUTPUT);
        if (outputSlot == null)
        {
            setInventorySlotContents(SLOT_ITEM_OUTPUT, stack);
        }
        else if (ItemStack.areItemStacksEqual(outputSlot, stack))
        {
            outputSlot.stackSize += stack.stackSize;
            outputSlot.stackSize = Math.min(outputSlot.stackSize, outputSlot.getMaxStackSize());
            outputSlot.stackSize = Math.min(outputSlot.stackSize, getInventoryStackLimit());
        }
    }


    protected boolean canProcess()
    {
        ItemStack stack = getStackInSlot(SLOT_ITEM_INPUT);
        if (stack != null)
        {
            return Item.getItemFromBlock(ASBlocks.blockUraniumOre) == stack.getItem();  //TODO move recipe to object
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

    @Override
    public int getSizeInventory()
    {
        return INVENTORY_SIZE;
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
}
