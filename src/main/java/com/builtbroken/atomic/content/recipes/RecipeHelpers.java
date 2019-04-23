package com.builtbroken.atomic.content.recipes;

import com.builtbroken.atomic.content.ASBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 6/17/2018.
 */
public class RecipeHelpers
{
    public static boolean isUraniumOre(ItemStack stack)
    {
        if(Item.getItemFromBlock(ASBlocks.blockUraniumOre) == stack.getItem())
        {
            return true;
        }
        int[] ids = OreDictionary.getOreIDs(stack);
        for(int id : ids)
        {
            if(OreDictionary.getOreName(id).equals("oreUranium"))
            {
                return true;
            }
        }
        return false;
    }
}
