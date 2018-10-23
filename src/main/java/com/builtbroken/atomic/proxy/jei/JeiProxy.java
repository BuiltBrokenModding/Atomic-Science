package com.builtbroken.atomic.proxy.jei;

import com.builtbroken.atomic.content.ASItems;
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
import com.builtbroken.atomic.proxy.jei.grid.RecipeWrapperHazmatDye;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/16/2018.
 */
@JEIPlugin
public class JeiProxy implements IModPlugin
{
    private static final String[] dyeOredicts = new String[]
            {
                    "dyeWhite",
                    "dyeOrange",
                    "dyeMagenta",
                    "dyeLightBlue",
                    "dyeYellow",
                    "dyeLime",
                    "dyePink",
                    "dyeGray",
                    "dyeLightGray",
                    "dyeCyan",
                    "dyePurple",
                    "dyeBlue",
                    "dyeBrown",
                    "dyeGreen",
                    "dyeRed",
                    "dyeBlack"
            };

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

        List<RecipeWrapperHazmatDye> dyeArmor = new ArrayList(dyeOredicts.length);
        for (String dyeName : dyeOredicts)
        {
            dyeArmor.add(new RecipeWrapperHazmatDye(dyeName, ASItems.itemArmorHazmatHelmColor));
            dyeArmor.add(new RecipeWrapperHazmatDye(dyeName, ASItems.itemArmorHazmatChestColor));
            dyeArmor.add(new RecipeWrapperHazmatDye(dyeName, ASItems.itemArmorHazmatLegsColor));
            dyeArmor.add(new RecipeWrapperHazmatDye(dyeName, ASItems.itemArmorHazmatBootsColor));
        }
        registry.addRecipes(dyeArmor, VanillaRecipeCategoryUid.CRAFTING);

        registry.handleRecipes(RecipeChemBoiler.class, recipe -> new RecipeWrapperBoiler(recipe), RecipeCategoryBoiler.ID);
        registry.handleRecipes(RecipeChemExtractor.class, recipe -> new RecipeWrapperExtractor(recipe), RecipeCategoryExtractor.ID);
        registry.handleRecipes(RecipeChemCentrifuge.class, recipe -> new RecipeWrapperCentrifuge(recipe), RecipeCategoryCentrifuge.ID);



        registry.addRecipeClickArea(GuiChemBoiler.class, 73, 30, 22, 15, RecipeCategoryBoiler.ID);
        registry.addRecipeClickArea(GuiChemExtractor.class, 73, 30, 22, 15, RecipeCategoryExtractor.ID);
        registry.addRecipeClickArea(GuiChemCentrifuge.class, 73, 30, 22, 15, RecipeCategoryCentrifuge.ID);
    }
}
