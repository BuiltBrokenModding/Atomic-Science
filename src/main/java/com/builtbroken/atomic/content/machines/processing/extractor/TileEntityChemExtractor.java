package com.builtbroken.atomic.content.machines.processing.extractor;

import com.builtbroken.atomic.config.ConfigRecipe;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.machines.processing.TileEntityProcessingMachine;
import com.builtbroken.atomic.lib.gui.IGuiTile;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/19/2018.
 */
public class TileEntityChemExtractor extends TileEntityProcessingMachine implements IFluidHandler, IGuiTile, ISidedInventory
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

    public TileEntityChemExtractor()
    {
        inputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
        outputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
    }

    @Override
    protected void preProcess(int ticks)
    {
        fillTank(SLOT_FLUID_INPUT, getInputTank());
        drainBattery(SLOT_BATTERY);
    }

    @Override
    protected int getProcessingTime()
    {
        return PROCESSING_TIME;
    }

    @Override
    protected void postProcess(int ticks)
    {
        outputFluids(SLOT_FLUID_OUTPUT, getOutputTank());
        outputFluidToTiles(getOutputTank(), null);
    }

    @Override
    protected void doProcess()
    {
        //TODO move recipe to object

        //Uranium Ore recipe
        ItemStack inputItem = getStackInSlot(SLOT_ITEM_INPUT);
        if(inputItem != null)
        {
            if (Item.getItemFromBlock(ASBlocks.blockUraniumOre) == inputItem.getItem()
                    && hasInputFluid(getInputTank(), FluidRegistry.WATER, ConfigRecipe.WATER_USED_YELLOW_CAKE)
                    && canOutputFluid(getOutputTank(), ASFluids.LIQUID_MINERAL_WASTE.fluid, ConfigRecipe.LIQUID_WASTE_PRODUCED_YELLOW_CAKE))

            {
                ItemStack outputStack = new ItemStack(ASItems.itemYellowCake, ConfigRecipe.YELLOW_CAKE_PER_ORE, 0);
                if (hasSpaceInOutput(outputStack, SLOT_ITEM_OUTPUT))
                {
                    decrStackSize(SLOT_ITEM_INPUT, 1);
                    getInputTank().drain(ConfigRecipe.WATER_USED_YELLOW_CAKE, true);
                    getOutputTank().fill(new FluidStack(ASFluids.LIQUID_MINERAL_WASTE.fluid, ConfigRecipe.LIQUID_WASTE_PRODUCED_YELLOW_CAKE), true);
                    addToOutput(outputStack, SLOT_ITEM_OUTPUT);
                }
            }
            else if (Item.getItemFromBlock(ASBlocks.blockUraniumOre) == inputItem.getItem())
            {

            }
        }
    }

    @Override
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

    @Override
    protected void onSlotStackChanged(ItemStack prev, ItemStack stack, int slot)
    {
        if (isServer() && slot == SLOT_ITEM_INPUT)
        {
            checkRecipe();
        }
        super.onSlotStackChanged(prev, stack, slot);
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
        dataList.add(getInputTank());
        dataList.add(getOutputTank());
    }

    @Override
    protected void readGuiPacket(ByteBuf buf, EntityPlayer player)
    {
        super.readGuiPacket(buf, player);
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
        nbt.setTag("outputTank", getOutputTank().writeToNBT(new NBTTagCompound()));
        nbt.setTag("inputTank", getInputTank().writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        getOutputTank().readFromNBT(nbt.getCompoundTag("outputTank"));
        getInputTank().readFromNBT(nbt.getCompoundTag("inputTank"));
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
