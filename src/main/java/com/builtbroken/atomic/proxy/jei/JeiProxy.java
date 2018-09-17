package com.builtbroken.atomic.proxy.jei;

import com.builtbroken.atomic.content.machines.processing.ProcessorRecipeHandler;
import com.builtbroken.atomic.content.machines.processing.boiler.gui.GuiChemBoiler;
import com.builtbroken.atomic.content.machines.processing.centrifuge.gui.GuiChemCentrifuge;
import com.builtbroken.atomic.content.machines.processing.extractor.gui.GuiChemExtractor;
import com.builtbroken.atomic.content.recipes.chem.RecipeChemBoiler;
import com.builtbroken.atomic.content.recipes.chem.RecipeChemCentrifuge;
import com.builtbroken.atomic.content.recipes.chem.RecipeChemExtractor;
import com.builtbroken.atomic.proxy.jei.boiler.RecipeCategoryBoiler;
import com.builtbroken.atomic.proxy.jei.boiler.RecipeWrapperBoiler;
import com.builtbroken.atomic.proxy.jei.centrifuge.RecipeCategoryCentrifuge;
import com.builtbroken.atomic.proxy.jei.centrifuge.RecipeWrapperCentrifuge;
import com.builtbroken.atomic.proxy.jei.extractor.RecipeCategoryExtractor;
import com.builtbroken.atomic.proxy.jei.extractor.RecipeWrapperExtractor;
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
        registry.addRecipeCategories(new RecipeCategoryExtractor(registry.getJeiHelpers()));
        registry.addRecipeCategories(new RecipeCategoryCentrifuge(registry.getJeiHelpers()));
    }

    @Override
    public void register(IModRegistry registry)
    {
        registry.addRecipes(
                ProcessorRecipeHandler.INSTANCE.chemBoilerProcessingRecipe.recipes,
                RecipeCategoryBoiler.ID);

        registry.addRecipes(
                ProcessorRecipeHandler.INSTANCE.chemExtractorProcessingRecipe.recipes,
                RecipeCategoryExtractor.ID);

        registry.addRecipes(
                ProcessorRecipeHandler.INSTANCE.chemCentrifugeProcessingRecipe.recipes,
                RecipeCategoryCentrifuge.ID);

        registry.handleRecipes(RecipeChemBoiler.class, recipe -> new RecipeWrapperBoiler(recipe), RecipeCategoryBoiler.ID);
        registry.handleRecipes(RecipeChemExtractor.class, recipe -> new RecipeWrapperExtractor(recipe), RecipeCategoryExtractor.ID);
        registry.handleRecipes(RecipeChemCentrifuge.class, recipe -> new RecipeWrapperCentrifuge(recipe), RecipeCategoryCentrifuge.ID);

        registry.addRecipeClickArea(GuiChemBoiler.class, 73, 30, 22, 15, RecipeCategoryBoiler.ID);
        registry.addRecipeClickArea(GuiChemExtractor.class, 73, 30, 22, 15, RecipeCategoryExtractor.ID);
        registry.addRecipeClickArea(GuiChemCentrifuge.class, 73, 30, 22, 15, RecipeCategoryCentrifuge.ID);
    }
}
