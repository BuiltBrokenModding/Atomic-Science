package com.builtbroken.atomic.content.machines.processing.extractor.recipe;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.processing.recipes.RecipeLootTable;
import com.builtbroken.atomic.content.machines.processing.recipes.RecipeRandomItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
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
    private final HashMap<String, Integer> dustWeights = new HashMap();

    private DustLootTable()
    {
        super("chem.extractor.loot.table.dust");
        dustWeights.put("dustCopper", 5);
        dustWeights.put("dustIron", 5);
        dustWeights.put("dustSteel", -1);
        dustWeights.put("dustBronze", -1);
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
    public void loadConfiguration()
    {
        //Collect dust weights
        dustEntries.keySet().forEach(key -> {
            if (!dustWeights.containsKey(key))
            {
                dustWeights.put(key, dustEntries.get(key).get(0).weight);
            }
        });

        //Load settings
        Configuration configuration = new Configuration(new File(AtomicScience.configFolder, "DustLootTable.cfg"), AtomicScience.VERSION);
        configuration.load();
        configuration.setCategoryComment("loot_weights", "Weight in the loot table, higher the number lower the chance of dropping. Zero and lower will disable the entry.");

        for (String dust_name : dustEntries.keySet())
        {
            int weight = configuration.getInt(dust_name, "loot_weights", dustWeights.get(dust_name), -1, Short.MAX_VALUE, "");
            dustWeights.put(dust_name, weight);
        }

        configuration.save();

        //Reload weight calculations
        calculateTotalWeight();
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
