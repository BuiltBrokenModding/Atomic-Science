package com.builtbroken.atomic.content.machines.processing.extractor.recipe;

import com.builtbroken.atomic.proxy.ProxyLoader;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/24/2018.
 */
public class ChemExtractorRecipes extends ProxyLoader
{
    public static final ChemExtractorRecipes INSTANCE = new ChemExtractorRecipes();

    public ChemExtractorRecipes()
    {
        super("chem.extractor.recipes");
        add(DustLootTable.INSTANCE);
    }


    /**
     * Gets a random dust from the loot table
     *
     * @return random entry from the table, or null if ran out of entries to loop (rare)
     */
    public static ItemStack getRandomDust()
    {
        return DustLootTable.INSTANCE.getRandomItemStack();
    }
}
