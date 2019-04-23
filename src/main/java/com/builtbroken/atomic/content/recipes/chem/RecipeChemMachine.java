package com.builtbroken.atomic.content.recipes.chem;

import com.builtbroken.atomic.content.machines.processing.TileEntityProcessingMachine;
import com.builtbroken.atomic.content.recipes.RecipeProcessing;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/17/2018.
 */
public abstract class RecipeChemMachine<H extends TileEntityProcessingMachine> extends RecipeProcessing<H>
{
    private final Object _input;
    private final ItemStack _output;

    public RecipeChemMachine(Object input, ItemStack output)
    {
        this._input = input;
        this._output = output;
    }

    protected abstract int getInputSlot();

    protected abstract int getOutputSlot();

    /**
     * Gets the current expected input
     * <p>
     * If you plan to randomize the inputs. Make sure to
     * cache your randomization so it doesn't change between
     * recipe cycles. Only when the recipe cycles over
     * should the value change.
     *
     * @param machine - current machine, or null for general checks
     * @return current input
     */
    @Nullable
    public Object getInput(@Nullable H machine)
    {
        return _input;
    }

    /**
     * Gets the current expected output
     * <p>
     * If you plan to randomize the inputs. Make sure to
     * cache your randomization so it doesn't change between
     * recipe cycles. Only when the recipe cycles over
     * should the value change.
     *
     * @param machine - current machine, or null for general checks
     * @return current output
     */
    @Nullable
    public ItemStack getOutput(@Nullable H machine)
    {
        return _output;
    }

    /**
     * Gets all possible recipe inputs.
     * <p>
     * Used purely for recipe displays like JEI
     *
     * @return list of inputs, or null
     */
    @Nullable
    public List<ItemStack> getPossibleInputs()
    {
        Object input = getInput(null);
        if (input instanceof ItemStack)
        {
            if (!((ItemStack) input).isEmpty())
            {
                return Lists.newArrayList((ItemStack) input);
            }
        }
        else if (input instanceof String)
        {
            return OreDictionary.getOres((String) input);
        }
        return null;
    }

    /**
     * Gets all possible recipe outputs
     * <p>
     * Used purely for recipe displays like JEI
     *
     * @return list of outputs, or null
     */
    @Nullable
    public List<ItemStack> getPossibleOutputs()
    {
        ItemStack output = getOutput(null);
        if (output != null && !output.isEmpty())
        {
            return Lists.newArrayList(output);
        }
        return null;
    }

    @Override
    public boolean matches(H machine)
    {
        return hasInput(machine) && canOutput(machine);
    }

    protected boolean hasInput(H machine)
    {
        //No slot, no need for input
        if(getInputSlot() < 0)
        {
            return true;
        }
        //Empty input, no need for input
        Object input = getInput(machine);
        if(input == null || input instanceof ItemStack && ((ItemStack) input).isEmpty())
        {
            return true;
        }

        //Match normal
        return matches(input, machine.getInventory().getStackInSlot(getInputSlot()));
    }

    protected boolean canOutput(H machine)
    {
        return getOutputSlot() < 0 || getOutput(machine).isEmpty() || machine.getInventory().insertItem(getOutputSlot(), getOutput(machine), true).isEmpty();
    }

    @Override
    public boolean applyRecipe(H machine)
    {
        if (matches(machine))
        {
            doRecipe(machine, machine.getInventory());
        }
        return false;
    }

    protected void doRecipe(H machine, IItemHandlerModifiable inventory)
    {
        if (getInput(machine) != null)
        {
            inventory.extractItem(getInputSlot(), 1, false);
        }

        ItemStack outputStack = getOutput(machine);
        if (outputStack != null && !outputStack.isEmpty())
        {
            inventory.insertItem(getOutputSlot(), getOutput(machine).copy(), false);
        }
    }

    @Override
    public boolean isComponent(H machine, ItemStack stack)
    {
        if (getInput(machine) != null)
        {
            return matches(getInput(machine), stack);
        }
        return false;
    }
}
