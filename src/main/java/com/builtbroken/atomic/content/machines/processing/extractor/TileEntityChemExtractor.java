package com.builtbroken.atomic.content.machines.processing.extractor;

import com.builtbroken.atomic.content.machines.TileEntityInventoryMachine;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/19/2018.
 */
public class TileEntityChemExtractor extends TileEntityInventoryMachine implements IFluidHandler
{
    public static final int SLOT_FLUID_INPUT = 0;
    public static final int SLOT_ITEM_INPUT = 1;
    public static final int SLOT_ITEM_OUTPUT = 2;
    public static final int SLOT_BATTERY = 3;

    public static int PROCESSING_TIME = 100;

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
        //TODO move fluid from item to tank
    }

    protected void outputFluids()
    {
        //TODO export fluid from output tank
    }

    protected void drainBattery()
    {
        //TODO drain battery
    }

    protected void doProcess()
    {
        //TODO convert inputs to outputs
    }

    protected boolean canProcess()
    {
        return false; //TODO check recipes
    }

    protected void checkRecipe()
    {
        processing = canProcess();
        if (!processing)
        {
            processTimer = 0;
        }
    }

    @Override
    public int getSizeInventory()
    {
        return 4;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (resource != null && canFill(from, resource.getFluid()))
        {
            Fluid fluid = inputTank.getFluid() != null ? inputTank.getFluid().getFluid() : null;
            int amount = inputTank.getFluidAmount();

            int fill = inputTank.fill(resource, doFill);

            if (doFill && inputTank.getFluid() != null && (fluid != inputTank.getFluid().getFluid()) || inputTank.getFluidAmount() != amount)
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
        if (outputTank.getFluid() != null && resource.getFluid() == outputTank.getFluid().getFluid())
        {
            return drain(from, resource.amount, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return outputTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return fluid == FluidRegistry.WATER;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return fluid == null || outputTank.getFluid() != null && outputTank.getFluid().getFluid() == fluid;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[]{inputTank.getInfo(), outputTank.getInfo()};
    }
}
