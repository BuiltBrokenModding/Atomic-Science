package com.builtbroken.atomic.content.recipes.chem;

import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public class RecipeChemBoiler extends RecipeChemMachine<TileEntityChemBoiler>
{
    public final FluidStack inputTankBlue;
    public final FluidStack outputTankGreen;
    public final FluidStack outputTankYellow;

    public RecipeChemBoiler(Object input, ItemStack output, FluidStack inputTankBlue, FluidStack outputTankGreen, FluidStack outputTankYellow)
    {
        super(input, output);
        this.inputTankBlue = inputTankBlue;
        this.outputTankGreen = outputTankGreen;
        this.outputTankYellow = outputTankYellow;
    }

    @Override
    protected int getInputSlot()
    {
        return TileEntityChemBoiler.SLOT_ITEM_INPUT;
    }

    @Override
    protected int getOutputSlot()
    {
        return TileEntityChemBoiler.SLOT_ITEM_OUTPUT;
    }

    @Override
    public boolean matches(TileEntityChemBoiler machine)
    {
        return super.matches(machine) && hasInputFluid(machine) && canOutputGreen(machine) && canOutputYellow(machine);
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
    protected void doRecipe(TileEntityChemBoiler machine, IItemHandlerModifiable inventory)
    {
        super.doRecipe(machine, inventory);

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

    @Override
    public boolean isComponent(TileEntityChemBoiler machine, Fluid fluid)
    {
        if (inputTankBlue != null)
        {
            return inputTankBlue.getFluid() == fluid;
        }
        return false;
    }
}
