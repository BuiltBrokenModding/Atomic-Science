package com.builtbroken.atomic.proxy.jei.boiler;

import com.builtbroken.atomic.content.recipes.chem.RecipeChemBoiler;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/16/2018.
 */
public class RecipeWrapperBoiler implements IRecipeWrapper
{
    public final RecipeChemBoiler recipe;

    public RecipeWrapperBoiler(RecipeChemBoiler recipe)
    {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        List<ItemStack> inputs = recipe.getPossibleInputs();
        if (inputs != null)
        {
            ingredients.setInputs(VanillaTypes.ITEM, inputs);
        }
        List<ItemStack> outputs = recipe.getPossibleOutputs();
        if (outputs != null)
        {
            ingredients.setOutputs(VanillaTypes.ITEM, outputs);
        }
        if (recipe.inputTankBlue != null)
        {
            ingredients.setInput(VanillaTypes.FLUID, recipe.inputTankBlue);
        }

        if (recipe.outputTankGreen != null && recipe.outputTankYellow != null) //TODO add method to get fluid outputs
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
