package com.builtbroken.atomic.content.recipes;

import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.proxy.ContentProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public class ASRecipes extends ContentProxy
{
    public ASRecipes()
    {
        super("recipes");
    }

    @Override
    public void init()
    {
        //Empty Cell
        GameRegistry.addRecipe(new ItemStack(ASBlocks.blockChemCentrifuge),
                " T ",
                "TGT",
                " T ",
                'G', new ItemStack(Blocks.glass),
                'T', getOreItem("ingotTin", new ItemStack(Items.iron_ingot)));

        //Centrifuge
        GameRegistry.addRecipe(new ItemStack(ASBlocks.blockChemCentrifuge),
                "ICI",
                "TMT",
                "TPT",
                'I', new ItemStack(Items.iron_ingot),
                'T', new ItemStack(ASItems.itemEmptyCell),
                'M', getOreItem("motor", new ItemStack(Items.repeater)),
                'P', getOreItem("plateIron", new ItemStack(Items.gold_ingot)),
                'C', getOreItem("circuitAdvanced", new ItemStack(Items.redstone)));
    }

    protected Object getOreItem(String ore_name, ItemStack alt)
    {
        if (OreDictionary.doesOreNameExist(ore_name))
        {
            for (ItemStack itemStack : OreDictionary.getOres(ore_name))
            {
                if (itemStack != null)
                {
                    return ore_name;
                }
            }
        }
        return alt;
    }

}
