package com.builtbroken.atomic.proxy.jei.grid;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 10/23/2018.
 */
public class RecipeWrapperHazmatDye implements ICraftingRecipeWrapper
{
    public final String dyeName;
    public final ItemArmor armor;

    public RecipeWrapperHazmatDye(String dyeName, ItemArmor armor)
    {
        this.dyeName = dyeName;
        this.armor = armor;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        List<ItemStack> inputs = OreDictionary.getOres(dyeName);
        if (inputs != null)
        {
            List<List<ItemStack>> list = new ArrayList(2);
            list.add(inputs);

            List<ItemStack> list2 = new ArrayList();
            list2.add(new ItemStack(armor));
            list.add(list2);

            ingredients.setInputLists(VanillaTypes.ITEM, list);
        }

        List<ItemStack> outputs = new ArrayList(inputs.size());
        for(ItemStack input : inputs)
        {
            int color = net.minecraftforge.oredict.DyeUtils.colorFromStack(input).get().getColorValue();
            ItemStack stack = new ItemStack(armor, 1, 0);
            armor.setColor(stack, color);
            outputs.add(stack);
        }
        if (outputs != null)
        {
            List<List<ItemStack>> list = new ArrayList(1);
            list.add(outputs);
            ingredients.setOutputLists(VanillaTypes.ITEM, list);
        }
    }
}
