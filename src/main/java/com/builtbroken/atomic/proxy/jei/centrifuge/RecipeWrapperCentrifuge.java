package com.builtbroken.atomic.proxy.jei.centrifuge;

import com.builtbroken.atomic.content.recipes.chem.RecipeChemCentrifuge;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/16/2018.
 */
public class RecipeWrapperCentrifuge implements IRecipeWrapper
{
    public final RecipeChemCentrifuge recipe;

    public RecipeWrapperCentrifuge(RecipeChemCentrifuge recipe)
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

        if (recipe.inputTank != null)
        {
            ingredients.setInput(VanillaTypes.FLUID, recipe.inputTank);
        }
        if (recipe.outputTank != null)
        {
            ingredients.setOutput(VanillaTypes.FLUID, recipe.outputTank);
        }
    }
}
