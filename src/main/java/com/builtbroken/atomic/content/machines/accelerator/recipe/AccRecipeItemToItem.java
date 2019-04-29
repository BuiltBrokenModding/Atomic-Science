package com.builtbroken.atomic.content.machines.accelerator.recipe;

import net.minecraft.item.ItemStack;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-29.
 */
public class AccRecipeItemToItem extends AcceleratorRecipe<ItemStack, ItemStack, ItemStack>
{
    public final ItemStack output;

    public AccRecipeItemToItem(ItemStack output)
    {
        this.output = output;
    }

    @Override
    public ItemStack getOutput(ItemStack containerStack, ItemStack particleStack, float energy)
    {
        return output;
    }
}
