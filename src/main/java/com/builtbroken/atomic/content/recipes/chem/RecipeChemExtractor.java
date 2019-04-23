package com.builtbroken.atomic.content.recipes.chem;

import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public class RecipeChemExtractor extends RecipeChemMachine<TileEntityChemExtractor>
{
    public final FluidStack inputTank;
    public final FluidStack outputTank;

    public RecipeChemExtractor(Object input, ItemStack output, FluidStack inputTank, FluidStack outputTank)
    {
        super(input, output);
        this.inputTank = inputTank;
        this.outputTank = outputTank;
    }

    @Override
    protected int getInputSlot()
    {
        return TileEntityChemExtractor.SLOT_ITEM_INPUT;
    }

    @Override
    protected int getOutputSlot()
    {
        return TileEntityChemExtractor.SLOT_ITEM_OUTPUT;
    }

    @Override
    public boolean matches(TileEntityChemExtractor machine)
    {
        return super.matches(machine) && hasInputFluid(machine) && canOutputFluid(machine);
    }

    protected boolean hasInputFluid(TileEntityChemExtractor machine)
    {
        if (inputTank != null)
        {
            return machine.hasInputFluid(machine.getInputTank(), inputTank.getFluid(), inputTank.amount);
        }
        return true;
    }

    protected boolean canOutputFluid(TileEntityChemExtractor machine)
    {
        if (outputTank != null)
        {
            return machine.canOutputFluid(machine.getOutputTank(), outputTank.getFluid(), outputTank.amount);
        }
        return true;
    }

    @Override
    protected void doRecipe(TileEntityChemExtractor machine, IItemHandlerModifiable inventory)
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
    public boolean isComponent(TileEntityChemExtractor machine, Fluid fluid)
    {
        if (inputTank != null)
        {
            return inputTank.getFluid() == fluid;
        }
        return false;
    }
}
