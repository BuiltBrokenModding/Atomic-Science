package com.builtbroken.atomic.content.recipes;

import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.proxy.ContentProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

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
        itemRecipes();
        machineRecipes();
        reactorRecipes();
        armorRecipes();
        toolRecipes();
    }

    private void itemRecipes()
    {
        //Empty Cell
        addRecipe(new ItemStack(ASItems.itemEmptyCell),
                " T ",
                "TGT",
                " T ",
                'G', new ItemStack(Blocks.glass),
                'T', getOreItem("ingotTin", new ItemStack(Items.iron_ingot)));

        //Fuel Rod
        addRecipe(new ItemStack(ASItems.itemFissileFuelCell),
                "PPP",
                "PTP",
                "PTP",
                'P', ASItems.itemUranium235,
                'T', ASItems.itemEmptyCell);

        //Fluid Cell
        addRecipe(new ItemStack(ASItems.itemFluidCell),
                "TS",
                "ST",
                'S', Items.slime_ball,
                'T', ASItems.itemEmptyCell);

        //Powered Cell
        addRecipe(new ItemStack(ASItems.itemPoweredCell),
                "TC",
                "CB",
                'C', getOreItem("circuitAdvanced", new ItemStack(Blocks.gold_block)),
                'B', getOreItem("battery", new ItemStack(Blocks.redstone_block)),
                'T', ASItems.itemEmptyCell);
    }

    private void machineRecipes()
    {
        //Centrifuge
        addRecipe(new ItemStack(ASBlocks.blockChemCentrifuge),
                "ICI",
                "TMT",
                "TPT",
                'I', getOreItem("ingotSteel", new ItemStack(Items.iron_ingot)),
                'T', new ItemStack(ASItems.itemEmptyCell),
                'M', getOreItem("motor", new ItemStack(Items.repeater)),
                'P', getOreItem("plateIron", new ItemStack(Items.gold_ingot)),
                'C', getOreItem("circuitAdvanced", new ItemStack(Items.redstone)));

        //Boiler
        addRecipe(new ItemStack(ASBlocks.blockChemBoiler),
                "PCP",
                "TFT",
                "PMP",
                'F', new ItemStack(Blocks.furnace),
                'T', new ItemStack(ASItems.itemEmptyCell),
                'M', getOreItem("motor", new ItemStack(Items.repeater)),
                'P', getOreItem("plateIron", new ItemStack(Items.gold_ingot)),
                'C', getOreItem("circuitAdvanced", new ItemStack(Items.redstone)));

        //Extractor
        addRecipe(new ItemStack(ASBlocks.blockChemExtractor),
                "IPI",
                "MCM",
                "IPI",
                'I', getOreItem("ingotSteel", new ItemStack(Items.iron_ingot)),
                'M', getOreItem("motor", new ItemStack(Items.repeater)),
                'P', getOreItem("plateIron", new ItemStack(Items.gold_ingot)),
                'C', getOreItem("circuitElite", new ItemStack(Blocks.gold_block)));
    }

    private void reactorRecipes()
    {
        //Reactor Core
        addRecipe(new ItemStack(ASBlocks.blockReactorCell),
                "PCP",
                "MCM",
                "PCP",
                'M', getOreItem("motor", new ItemStack(Items.repeater)),
                'P', getOreItem("plateIron", new ItemStack(Blocks.iron_block)),
                'C', getOreItem("circuitAdvanced", new ItemStack(Blocks.dispenser)));

        //Turbine
        addRecipe(new ItemStack(ASBlocks.blockSteamTurbine),
                "IPI",
                "PMP",
                "IPI",
                'M', getOreItem("motor", new ItemStack(Items.repeater)),
                'P', getOreItem("plateIron", new ItemStack(Blocks.iron_block)),
                'I', getOreItem("rodIron", new ItemStack(Blocks.iron_bars)));

        //Turbine
        addRecipe(new ItemStack(ASBlocks.blockSteamFunnel),
                "IPI",
                "PHP",
                "IPI",
                'H', Blocks.hopper,
                'P', getOreItem("plateIron", new ItemStack(Blocks.iron_block)),
                'I', getOreItem("rodIron", new ItemStack(Blocks.iron_bars)));
    }

    private void armorRecipes()
    {
        //Hazmat helm
        addRecipe(new ItemStack(ASItems.itemArmorHazmatHelm),
                "CCC",
                "ILI",
                "GHG",
                'I', getOreItem("ingotLead", new ItemStack(Items.iron_ingot)),
                'C', Blocks.wool,
                'L', Items.leather_helmet,
                'G', Blocks.glass_pane,
                'H', getOreItem("circuitBasic", new ItemStack(Blocks.gold_block)));

        //Hazmat Body
        addRecipe(new ItemStack(ASItems.itemArmorHazmatChest),
                "CCC",
                "ILI",
                "THT",
                'I', getOreItem("ingotLead", new ItemStack(Items.iron_ingot)),
                'C', Blocks.wool,
                'L', Items.leather_chestplate,
                'T', ASItems.itemEmptyCell,
                'H', getOreItem("circuitBasic", new ItemStack(Blocks.gold_block)));

        //Hazmat Legs
        addRecipe(new ItemStack(ASItems.itemArmorHazmatLegs),
                "CCC",
                "ILI",
                "CHC",
                'I', getOreItem("ingotLead", new ItemStack(Items.iron_ingot)),
                'C', Blocks.wool,
                'L', Items.leather_leggings,
                'H', getOreItem("circuitBasic", new ItemStack(Blocks.gold_block)));

        //Hazmat boots
        addRecipe(new ItemStack(ASItems.itemArmorHazmatBoots),
                "CCC",
                "ILI",
                "CHC",
                'I', getOreItem("ingotLead", new ItemStack(Items.iron_ingot)),
                'C', Blocks.wool,
                'L', Items.leather_boots,
                'H', getOreItem("circuitBasic", new ItemStack(Blocks.gold_block)));
    }

    private void toolRecipes()
    {
        //Hazmat helm
        addRecipe(new ItemStack(ASItems.itemArmorHazmatHelm),
                "ICI",
                "ICI",
                "WBW",
                'I', getOreItem("ingotSteel", new ItemStack(Items.iron_ingot)),
                'W', getOreItem("wireCopper", new ItemStack(Items.gold_ingot)),
                'B', Items.gold_nugget,
                'G', Blocks.glass_pane,
                'C', getOreItem("circuitBasic", new ItemStack(Blocks.gold_block)));
    }

    protected void addRecipe(ItemStack output, Object... params)
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(output, params));
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
