package com.builtbroken.atomic.proxy.jei.boiler;

import com.builtbroken.atomic.content.machines.processing.boiler.RecipeChemBoiler;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/16/2018.
 */
public class RecipeWrapperBoiler implements IRecipeWrapper  //TODO rework boiler recipe to use a common class to pull data from
{
    public final RecipeChemBoiler recipe;

    public RecipeWrapperBoiler(RecipeChemBoiler recipe)
    {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        if (recipe.input != null)
        {
            if (recipe.input instanceof ItemStack)
            {
                ingredients.setInput(VanillaTypes.ITEM, (ItemStack) recipe.input);
            }
            else if (recipe.input instanceof String)
            {
                ingredients.setInputs(VanillaTypes.ITEM, OreDictionary.getOres((String) recipe.input));
            }
        }
        if (recipe.output != null)
        {
            ingredients.setOutput(VanillaTypes.ITEM, recipe.output);
        }
        if (recipe.inputTankBlue != null)
        {
            ingredients.setInput(VanillaTypes.FLUID, recipe.inputTankBlue);
        }
        if (recipe.outputTankGreen != null && recipe.outputTankYellow != null)
        {
            ingredients.setOutputs(VanillaTypes.FLUID, Lists.newArrayList(recipe.outputTankGreen, recipe.outputTankYellow));
        }
        else if (recipe.outputTankGreen != null)
        {
            ingredients.setOutput(VanillaTypes.FLUID, recipe.outputTankGreen);
        }
        else if (recipe.outputTankYellow != null)
        {
            ingredients.setOutput(VanillaTypes.FLUID, recipe.outputTankYellow);
        }
    }
}
