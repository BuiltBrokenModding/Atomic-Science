package com.builtbroken.atomic.content.machines.processing.extractor.recipe;

import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/24/2018.
 */
public class RecipeRandomItem
{
    public int weight;
    private final ItemStack stack;

    public RecipeRandomItem(int weight, ItemStack stack)
    {
        this.weight = weight;
        this.stack = stack;
    }

    public ItemStack getStack()
    {
        return stack.copy();
    }
}
