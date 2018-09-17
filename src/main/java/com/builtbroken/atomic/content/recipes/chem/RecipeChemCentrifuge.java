package com.builtbroken.atomic.content.recipes.chem;

import com.builtbroken.atomic.content.machines.processing.centrifuge.TileEntityChemCentrifuge;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public class RecipeChemCentrifuge extends RecipeChemMachine<TileEntityChemCentrifuge>
{
    public final FluidStack inputTank;
    public final FluidStack outputTank;

    public RecipeChemCentrifuge(ItemStack output, FluidStack inputTank, FluidStack outputTank)
    {
        super(null, output);
        this.inputTank = inputTank;
        this.outputTank = outputTank;
    }

    @Override
    protected int getInputSlot()
    {
        return -1;
    }

    @Override
    protected int getOutputSlot()
    {
        return TileEntityChemCentrifuge.SLOT_ITEM_OUTPUT;
    }

    @Override
    public boolean matches(TileEntityChemCentrifuge machine)
    {
        return super.matches(machine) && hasInputFluid(machine) && canOutputFluid(machine);
    }

    protected boolean hasInputFluid(TileEntityChemCentrifuge machine)
    {
        if (inputTank != null)
        {
            return machine.hasInputFluid(machine.getInputTank(), inputTank.getFluid(), inputTank.amount);
        }
        return true;
    }

    protected boolean canOutputFluid(TileEntityChemCentrifuge machine)
    {
        if (outputTank != null)
        {
            return machine.canOutputFluid(machine.getOutputTank(), outputTank.getFluid(), outputTank.amount);
        }
        return true;
    }

    @Override
    protected void doRecipe(TileEntityChemCentrifuge machine, IItemHandlerModifiable inventory)
    {
        super.doRecipe(machine, inventory);

        if (inputTank != null)
        {
            machine.getInputTank().drain(inputTank, true);
        }

        if (outputTank != null)
        {
            machine.getOutputTank().fill(outputTank, true);
        }
    }

    @Override
    public boolean isComponent(TileEntityChemCentrifuge machine, Fluid fluid)
    {
        if (inputTank != null)
        {
            return inputTank.getFluid() == fluid;
        }
        return false;
    }
}
