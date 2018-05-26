package com.builtbroken.atomic.content.machines.processing;

import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import com.builtbroken.atomic.content.machines.processing.boiler.recipe.RecipeMineralWaste;
import com.builtbroken.atomic.content.machines.processing.boiler.recipe.RecipeUraniumHex;
import com.builtbroken.atomic.content.machines.processing.boiler.recipe.RecipeYellowcakeHex;
import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import com.builtbroken.atomic.content.machines.processing.extractor.recipe.DustLootTable;
import com.builtbroken.atomic.content.machines.processing.extractor.recipe.RecipeWasteExtracting;
import com.builtbroken.atomic.content.machines.processing.extractor.recipe.RecipeYellowcake;
import com.builtbroken.atomic.content.machines.processing.recipes.ProcessingRecipeList;
import com.builtbroken.atomic.proxy.ProxyLoader;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public final class ProcessorRecipeHandler extends ProxyLoader
{
    public final ProcessingRecipeList<TileEntityChemExtractor> chemExtractorProcessingRecipe;
    public final ProcessingRecipeList<TileEntityChemBoiler> chemBoilerProcessingRecipe;
    public final ProcessingRecipeList<TileEntityChemExtractor> chemCentrifugeProcessingRecipe;

    public static final ProcessorRecipeHandler INSTANCE = new ProcessorRecipeHandler();

    private ProcessorRecipeHandler()
    {
        super("processing.machines");

        //Extractor
        add(chemExtractorProcessingRecipe = new ProcessingRecipeList("chem.extractor.recipes"));
        add(DustLootTable.INSTANCE);
        chemExtractorProcessingRecipe.add(new RecipeWasteExtracting());
        chemExtractorProcessingRecipe.add(new RecipeYellowcake());

        //Boiler
        add(chemBoilerProcessingRecipe = new ProcessingRecipeList("chem.boiler.recipes"));
        chemBoilerProcessingRecipe.add(new RecipeMineralWaste());
        chemBoilerProcessingRecipe.add(new RecipeUraniumHex());
        chemBoilerProcessingRecipe.add(new RecipeYellowcakeHex());

        //Centrifuge
        add(chemCentrifugeProcessingRecipe = new ProcessingRecipeList("chem.centrifuge.recipes"));
    }
}
