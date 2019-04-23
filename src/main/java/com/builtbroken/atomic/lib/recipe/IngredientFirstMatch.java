package com.builtbroken.atomic.lib.recipe;

import com.builtbroken.atomic.AtomicScience;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nonnull;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/16/2018.
 */
public class IngredientFirstMatch implements IIngredientFactory
{
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json)
    {
        //Pull in an array of possible items, select first valid
        JsonArray array = JsonUtils.getJsonArray(json, "items");
        for (JsonElement element : array)
        {
            //Try to get ingredient from inputs
            Ingredient ingredient = get(context, element);
            if (ingredient != null)
            {
                //Make sure we have a valid item
                for (ItemStack stack : ingredient.getMatchingStacks())
                {
                    if (stack != null && !stack.isEmpty())
                    {
                        return ingredient;
                    }
                }
            }
        }

        //Failed to find valid input, set to empty
        AtomicScience.logger.error("IngredientBackup: Failed to match any items to existing items, setting slot to empty. " + json);
        return Ingredient.EMPTY;
    }

    Ingredient get(JsonContext context, JsonElement element)
    {
        //Simple string
        if (element.isJsonPrimitive())
        {
            return get(element.getAsString());
        }
        //Normal input
        else
        {
            return CraftingHelper.getIngredient(element, context);
        }
    }

    Ingredient get(String key)
    {
        //Ore input simple
        if (key.startsWith("ore:"))
        {
            String name = key.substring(4);
            NonNullList<ItemStack> list = OreDictionary.getOres(name);
            for (ItemStack stack : list)
            {
                if (!stack.isEmpty())
                {
                    return new OreIngredient(name);
                }
            }
            return null;
        }
        //Simple input
        else
        {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(key));
            return Ingredient.fromStacks(new ItemStack(item));
        }
    }
}
