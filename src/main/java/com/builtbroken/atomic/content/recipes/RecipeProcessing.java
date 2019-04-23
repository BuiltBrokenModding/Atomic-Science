package com.builtbroken.atomic.content.recipes;

import com.builtbroken.atomic.content.machines.processing.TileEntityProcessingMachine;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Prefab for recipes used in the processing machine
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public abstract class RecipeProcessing<H extends TileEntityProcessingMachine> //TODO convert to interface for other mods to add recipes
{
    /**
     * Checks if the current state of the machine matches
     * a given recipe.
     * <p>
     * Use the machine to access inventories, fluid tanks,
     * and power as needed.
     *
     * @param machine - machine to apply recipe to
     * @return true if the state matches the recipe
     */
    public abstract boolean matches(H machine);

    /**
     * Called to process the recipe and apply
     * the results to the machine
     * <p>
     * Use the machine to access inventories, fluid tanks,
     * and power as needed.
     *
     * @param machine - machine to apply recipe to
     * @return true when recipe was applied
     */
    public abstract boolean applyRecipe(H machine);

    /**
     * Checks if the fluid is a component in the recipe.
     * <p>
     * This is mainly used to handle fluid input into the machine
     *
     * @param fluid
     * @param machine - machine to apply recipe to
     * @return
     */
    public abstract boolean isComponent(H machine, Fluid fluid);

    /**
     * Checks if the item is a component in the recipe.
     * <p>
     * This is mainly used to handle inventory input into the machine
     *
     * @param stack
     * @param machine - machine to apply recipe to
     * @return
     */
    public abstract boolean isComponent(H machine, ItemStack stack);

    public static boolean doStacksMatch(ItemStack a, ItemStack b)
    {
        return ItemStack.areItemsEqual(a, b) && ItemStack.areItemStackTagsEqual(a, b);
    }

    public static boolean matches(Object input, ItemStack stack)
    {
        if (!stack.isEmpty())
        {
            if (input instanceof String)
            {
                int[] ids = OreDictionary.getOreIDs(stack);
                for (int id : ids)
                {
                    if (OreDictionary.getOreName(id).equals((String) input))
                    {
                        return true;
                    }
                }
            }
            else if (input instanceof ItemStack)
            {
                ItemStack inputStack = ((ItemStack) input);
                if (!inputStack.isEmpty())
                {
                    return doStacksMatch(inputStack, stack);
                }
            }
        }
        return false;
    }
}
