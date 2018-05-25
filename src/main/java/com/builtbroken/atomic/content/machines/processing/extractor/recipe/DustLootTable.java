package com.builtbroken.atomic.content.machines.processing.extractor.recipe;

import com.builtbroken.atomic.content.machines.processing.recipes.RecipeLootTable;
import com.builtbroken.atomic.content.machines.processing.recipes.RecipeRandomItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

/**
 * Loot table for dust entries in the extractor
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public class DustLootTable extends RecipeLootTable
{
    public static final DustLootTable INSTANCE = new DustLootTable();

    public final int defaultDustWeight = 10;
    public final int defaultRareDustWeight = 20;

    //Used for config
    private final HashMap<String, List<RecipeRandomItem>> dustEntries = new HashMap();

    public DustLootTable()
    {
        super("chem.extractor.loot.table.dust");
    }

    @Override
    public void loadComplete()
    {
        //Clear old data, loadComplete() will run each time a world is switched
        lootItems.clear();

        //Add vanilla dusts
        lootItems.add(new RecipeRandomItem(defaultRareDustWeight, new ItemStack(Items.redstone)));
        lootItems.add(new RecipeRandomItem(defaultRareDustWeight, new ItemStack(Items.glowstone_dust)));

        //Add non-ingot dust entries
        addEntries("stoneDust", 1, i -> true);

        //Search all orenames
        for (String ore_name : OreDictionary.getOreNames())
        {
            //Only get dust
            if (ore_name.toLowerCase().contains("dust") && !name.toLowerCase().equalsIgnoreCase("stoneDust"))
            {
                //TODO merge subtypes into same loot entry and add settings to disable duplications (VE dust vs TE dust, pick one)
                addEntries(ore_name, defaultDustWeight, item -> hasIngot(item));
            }
        }

        //Load user settings
        loadConfiguration();

        //Sort lower weights to front of list
        Collections.sort(lootItems, Comparator.comparingInt(o -> -o.weight));
    }

    @Override
    protected void addEntry(String ore_name, ItemStack dustStack, int weight)
    {
        //Copy stack to prevent errors
        ItemStack stack1 = dustStack.copy();
        stack1.stackSize = 1;

        //Create entry
        RecipeRandomItem recipeRandomItem = new RecipeRandomItem(weight, stack1);
        lootItems.add(recipeRandomItem);

        //Add entry to config map
        if (!dustEntries.containsKey(ore_name))
        {
            dustEntries.put(ore_name, new ArrayList());
        }
        dustEntries.get(ore_name).add(recipeRandomItem);
    }

}
