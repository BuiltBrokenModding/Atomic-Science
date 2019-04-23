package com.builtbroken.atomic.content.recipes.loot;

import com.builtbroken.atomic.proxy.ContentProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Handles loot tables for processing machines
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public class RecipeLootTable extends ContentProxy
{
    /** List of loot entries */
    protected final List<RecipeRandomItem> lootItems = new ArrayList();
    /** Random to use for loot drops */
    protected final Random random = new Random();
    /** Total weight of loot items, for use with random generator */
    protected int totalWeight = 0;

    public RecipeLootTable(String name)
    {
        super(name);
    }

    /**
     * Called to recalculate the total weight of random items
     */
    public void calculateTotalWeight()
    {
        totalWeight = 0;
        lootItems.forEach(e -> totalWeight += e.weight);
    }

    /**
     * Loads the configuration
     */
    public void loadConfiguration()
    {
        //TODO load configuration
        //TODO save/load dust to weights

        calculateTotalWeight();
    }


    /**
     * Called to add all entries that use the ore_name
     *
     * @param ore_name - ore dictionary group of the item
     * @param weight   - weight of entries
     * @param function - logic to use for adding the entry
     */
    protected void addEntries(final String ore_name, final int weight, final Function<ItemStack, Boolean> function)
    {
        if (OreDictionary.doesOreNameExist(ore_name))
        {
            for (ItemStack stack : OreDictionary.getOres(ore_name))
            {
                if (stack != null && stack.getItem() != null && function.apply(stack))
                {
                    addEntry(ore_name, stack, weight);
                }
            }
        }
    }

    /**
     * Called to add an entry to the loot table
     *
     * @param ore_name - ore dictionary group of the item
     * @param stack    - item
     * @param weight   - loot table weight
     */
    protected void addEntry(final String ore_name, final ItemStack stack, final int weight)
    {
        //Copy stack to prevent errors
        ItemStack stack1 = stack.copy();
        stack1.setCount(1);

        //Create entry
        RecipeRandomItem recipeRandomItem = new RecipeRandomItem(weight, stack1);
        lootItems.add(recipeRandomItem);
    }

    /**
     * Checks if the stack has a smelting result that is an ingot
     *
     * @param itemStack - stack to check
     * @return true if it turns into an ingot
     */
    protected boolean hasIngot(ItemStack itemStack)
    {
        if (itemStack != null && itemStack.getItem() != null)
        {
            //Confirm that the dust turns into an ingot
            ItemStack smeltingResult = FurnaceRecipes.instance().getSmeltingResult(itemStack);
            if (!smeltingResult.isEmpty() && smeltingResult.getItem() != null)
            {
                for (int id : OreDictionary.getOreIDs(smeltingResult))
                {
                    String name = OreDictionary.getOreName(id);
                    if (name != null && name.toLowerCase().contains("ingot"))
                    {

                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Gets a random ItemStack from the loot table
     *
     * @return random entry from the table, or null if ran out of entries to loop (rare)
     */
    public ItemStack getRandomItemStack()
    {
        //Should never happen
        if (totalWeight <= 0)
        {
            calculateTotalWeight();
        }

        //Generate a random weight value
        int randomWeight = random.nextInt(1 + totalWeight);

        //loop over all items
        Iterator<RecipeRandomItem> iterator = lootItems.iterator();
        RecipeRandomItem itemEntry = null;

        //This works by slowly decaying the random value until we go negative
        //      In order for this to work the loot table needs to be sorted
        //      Value should be sorted with largest values first and lowest last
        //      This will result in lowest values being higher in the drop tables
        do
        {
            if (!iterator.hasNext())
            {
                return ItemStack.EMPTY;
            }

            RecipeRandomItem next = iterator.next();
            if (next.weight > 0)
            {
                randomWeight -= next.weight;
                itemEntry = next;
            }
        }
        while (randomWeight >= 0);

        if(itemEntry != null)
        {
            //Convert the entry to an itemstack
            ItemStack stack = itemEntry.getRandomStack();
            if (stack != null && !stack.isEmpty())
            {
                if (stack.getCount() <= 0)
                {
                    stack.setCount(1);
                }
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
