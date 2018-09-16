package com.builtbroken.atomic.proxy.jei;

import com.builtbroken.atomic.content.machines.processing.ProcessorRecipeHandler;
import com.builtbroken.atomic.content.machines.processing.boiler.gui.GuiChemBoiler;
import com.builtbroken.atomic.content.machines.processing.boiler.RecipeChemBoiler;
import com.builtbroken.atomic.proxy.jei.boiler.RecipeCategoryBoiler;
import com.builtbroken.atomic.proxy.jei.boiler.RecipeWrapperBoiler;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/16/2018.
 */
@JEIPlugin
public class JeiProxy implements IModPlugin
{
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        registry.addRecipeCategories(new RecipeCategoryBoiler(registry.getJeiHelpers()));
    }

    @Override
    public void register(IModRegistry registry)
    {
        registry.addRecipes(
                ProcessorRecipeHandler.INSTANCE.chemBoilerProcessingRecipe.recipes,
                RecipeCategoryBoiler.ID);


        registry.handleRecipes(RecipeChemBoiler.class, recipe -> new RecipeWrapperBoiler(recipe),RecipeCategoryBoiler.ID);
        registry.addRecipeClickArea(GuiChemBoiler.class, 73, 30, 22, 15, RecipeCategoryBoiler.ID);
    }
}
