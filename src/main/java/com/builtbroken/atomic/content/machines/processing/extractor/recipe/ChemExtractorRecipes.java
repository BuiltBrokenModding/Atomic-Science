package com.builtbroken.atomic.content.machines.processing.extractor.recipe;

import com.builtbroken.atomic.proxy.ContentProxy;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/24/2018.
 */
public class ChemExtractorRecipes extends ContentProxy
{
    public static int defaultDustWeight = 10;
    public static int defaultRareDustWeight = 20;

    private static List<RecipeRandomItem> oreDictionaryDust = new ArrayList();
    private static int totalWeight = 0;
    private static Random random = new Random();
    private static List<String> dustNames = new ArrayList();

    public static final ChemExtractorRecipes INSTANCE = new ChemExtractorRecipes();

    public ChemExtractorRecipes()
    {
        super("chem.extractor.recipes");
    }

    @Override
    public void loadComplete()
    {
        //TODO limit defaults to what real life would have
        //https://en.wikipedia.org/wiki/Uranium_ore
        oreDictionaryDust.clear();
        oreDictionaryDust.add(new RecipeRandomItem(defaultRareDustWeight, new ItemStack(Items.redstone)));
        oreDictionaryDust.add(new RecipeRandomItem(defaultRareDustWeight, new ItemStack(Items.glowstone_dust)));

        for (ItemStack stack : OreDictionary.getOres("stoneDust"))
        {
            if (stack != null && stack.getItem() != null)
            {
                oreDictionaryDust.add(new RecipeRandomItem(defaultRareDustWeight, stack.copy()));
                break;
            }
        }

        //Search all orenames
        for (String ore_name : OreDictionary.getOreNames())
        {
            //Only get dust
            if (ore_name.toLowerCase().contains("dust") && !name.toLowerCase().equalsIgnoreCase("stoneDust"))
            {
                boolean added = false;
                //Get all subtypes
                for (ItemStack stack : OreDictionary.getOres(ore_name))
                {
                    if (stack != null && stack.getItem() != null)
                    {
                        //Confirm that the dust turns into an ingot
                        ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(stack);
                        if (smeltingResult != null && smeltingResult.getItem() != null)
                        {
                            for (int id : OreDictionary.getOreIDs(smeltingResult))
                            {
                                String name = OreDictionary.getOreName(id);
                                if (name != null && name.toLowerCase().contains("ingot"))
                                {
                                    ItemStack stack1 = stack.copy();
                                    stack1.stackSize = 1;

                                    oreDictionaryDust.add(new RecipeRandomItem(defaultDustWeight, stack1));

                                    added = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (added)
                {
                    dustNames.add(ore_name);
                }
            }
        }

        //Load user settings
        loadConfiguration();

        //Sort lower weights to front of list
        Collections.sort(oreDictionaryDust, Comparator.comparingInt(o -> o.weight));

    }

    public void recalcTotalWeight()
    {
        totalWeight = 0;
        oreDictionaryDust.forEach(e -> totalWeight += e.weight);
    }

    public void loadConfiguration()
    {
        //TODO load configuration
        //TODO save/load dust to weights

        recalcTotalWeight();
    }

    public static ItemStack getRandomDust()
    {
        if(totalWeight <= 0)
        {
            INSTANCE.recalcTotalWeight();
        }
        int j = random.nextInt(1 + totalWeight);
        Iterator<RecipeRandomItem> iterator = oreDictionaryDust.iterator();
        RecipeRandomItem item;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            item = iterator.next();
            j -= item.weight;
        }
        while (j >= 0);

        //Get item
        ItemStack stack = item.getStack();
        if (stack != null)
        {
            if (stack.stackSize <= 0)
            {
                stack.stackSize = 1;
            }
            return stack;
        }
        return new ItemStack(Blocks.dirt);
    }
}
