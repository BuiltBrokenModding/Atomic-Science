package com.builtbroken.atomic.content.machines.processing.boiler.recipe;

import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import com.builtbroken.atomic.content.machines.processing.recipes.ProcessingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public class RecipeChemBoiler extends ProcessingRecipe<TileEntityChemBoiler>
{
    public final ItemStack input;
    public final ItemStack output;

    public final FluidStack inputTankBlue;
    public final FluidStack outputTankGreen;
    public final FluidStack outputTankYellow;

    public RecipeChemBoiler(ItemStack input, ItemStack output, FluidStack inputTankBlue, FluidStack outputTankGreen, FluidStack outputTankYellow)
    {
        this.input = input;
        this.output = output;
        this.inputTankBlue = inputTankBlue;
        this.outputTankGreen = outputTankGreen;
        this.outputTankYellow = outputTankYellow;
    }

    @Override
    public boolean matches(TileEntityChemBoiler machine)
    {
        return hasInput(machine) && canOutput(machine) && hasInputFluid(machine) && canOutputGreen(machine) && canOutputYellow(machine);
    }

    private boolean hasInput(TileEntityChemBoiler machine)
    {
        if (!input.isEmpty())
        {
            return !machine.getInventory().extractItem(TileEntityChemBoiler.SLOT_ITEM_INPUT, 1, true).isEmpty();
        }
        return true;
    }

    private boolean canOutput(TileEntityChemBoiler machine)
    {
        if (!output.isEmpty())
        {
            return machine.getInventory().insertItem(TileEntityChemBoiler.SLOT_ITEM_OUTPUT, output, true).isEmpty();
        }
        return true;
    }

    private boolean hasInputFluid(TileEntityChemBoiler machine)
    {
        if (inputTankBlue != null)
        {
            return machine.hasInputFluid(machine.getBlueTank(), inputTankBlue.getFluid(), inputTankBlue.amount);
        }
        return true;
    }

    private boolean canOutputGreen(TileEntityChemBoiler machine)
    {
        if (outputTankGreen != null)
        {
            return machine.canOutputFluid(machine.getGreenTank(), outputTankGreen.getFluid(), outputTankGreen.amount);
        }
        return true;
    }

    private boolean canOutputYellow(TileEntityChemBoiler machine)
    {
        if (outputTankYellow != null)
        {
            return machine.canOutputFluid(machine.getYellowTank(), outputTankYellow.getFluid(), outputTankYellow.amount);
        }
        return true;
    }

    @Override
    public boolean applyRecipe(TileEntityChemBoiler machine)
    {
        if (matches(machine))
        {
            if (input != null)
            {
                machine.getInventory().extractItem(TileEntityChemBoiler.SLOT_ITEM_INPUT, 1, false);
            }
            if (output != null)
            {
                machine.getInventory().insertItem(TileEntityChemBoiler.SLOT_ITEM_OUTPUT, output.copy(), false);
            }

            if (inputTankBlue != null)
            {
                machine.getBlueTank().drain(inputTankBlue, true);
            }

            if (outputTankGreen != null)
            {
                machine.getGreenTank().fill(outputTankGreen, true);
            }

            if (outputTankYellow != null)
            {
                machine.getYellowTank().fill(outputTankYellow, true);
            }
        }
        return false;
    }

    @Override
    public boolean isComponent(TileEntityChemBoiler machine, Fluid fluid)
    {
        if (inputTankBlue != null)
        {
            return inputTankBlue.getFluid() == fluid;
        }
        return false;
    }

    @Override
    public boolean isComponent(TileEntityChemBoiler machine, ItemStack stack)
    {
        if (input != null)
        {
            return ItemStack.areItemsEqual(input, stack) && ItemStack.areItemStackTagsEqual(input, stack);
        }
        return false;
    }
}
