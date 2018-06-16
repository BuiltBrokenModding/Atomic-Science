package com.builtbroken.atomic.content.recipes;

import com.builtbroken.atomic.config.ConfigRecipe;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.proxy.ContentProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public class ASRecipes extends ContentProxy
{
    public static boolean disableRecipes = false;
    public static boolean allowAltItems = true;

    public ASRecipes()
    {
        super("recipes");
    }

    @Override
    public void init()
    {
        if (!ConfigRecipe.DISABLE_BASE_RECIPES && !disableRecipes)
        {
            itemRecipes();
            machineRecipes();
            reactorRecipes();
            armorRecipes();
            toolRecipes();
        }
    }

    private void itemRecipes()
    {
        //Empty Cell
        addRecipe(new ItemStack(ASItems.itemEmptyCell),
                " T ",
                "TGT",
                " T ",
                'G', Blocks.glass,
                'T', getOreItem("ingotTin", Items.iron_ingot));

        //Fuel Rod
        addRecipe(new ItemStack(ASItems.itemFissileFuelCell),
                "PPP",
                "PTP",
                "PPP",
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
                'C', getOreItem("circuitAdvanced", Blocks.gold_block),
                'B', getOreItem("battery", Blocks.redstone_block),
                'T', ASItems.itemEmptyCell);
    }

    private void machineRecipes()
    {
        //Centrifuge
        addRecipe(new ItemStack(ASBlocks.blockChemCentrifuge),
                "ICI",
                "TMT",
                "TPT",
                'I', getOreItem("ingotSteel", Items.iron_ingot),
                'T', ASItems.itemEmptyCell,
                'M', getOreItem("motor", Items.repeater),
                'P', getOreItem("plateIron", Items.gold_ingot),
                'C', getOreItem("circuitAdvanced", Items.redstone));

        //Boiler
        addRecipe(new ItemStack(ASBlocks.blockChemBoiler),
                "PCP",
                "TFT",
                "PMP",
                'F', Blocks.furnace,
                'T', ASItems.itemEmptyCell,
                'M', getOreItem("motor", Items.repeater),
                'P', getOreItem("plateIron", Items.gold_ingot),
                'C', getOreItem("circuitAdvanced", Items.redstone));

        //Extractor
        addRecipe(new ItemStack(ASBlocks.blockChemExtractor),
                "IPI",
                "MCM",
                "IPI",
                'I', getOreItem("ingotSteel", Items.iron_ingot),
                'M', getOreItem("motor", Items.repeater),
                'P', getOreItem("plateIron", Items.gold_ingot),
                'C', getOreItem("circuitElite", Blocks.gold_block));
    }

    private void reactorRecipes()
    {
        //Reactor Core
        addRecipe(new ItemStack(ASBlocks.blockReactorCell),
                "PCP",
                "MTM",
                "PCP",
                'M', getOreItem("motor", Items.repeater),
                'P', getOreItem("plateIron", Blocks.iron_block),
                'C', getOreItem("circuitAdvanced", Blocks.dispenser),
                'C', ASItems.itemFluidCell);

        //Turbine
        addRecipe(new ItemStack(ASBlocks.blockSteamTurbine),
                "IPI",
                "PMP",
                "IPI",
                'M', getOreItem("motor", Items.repeater),
                'P', getOreItem("plateIron", Blocks.iron_block),
                'I', getOreItem("rodIron", Blocks.iron_bars));

        //Turbine
        addRecipe(new ItemStack(ASBlocks.blockSteamFunnel),
                "IPI",
                "PHP",
                "IPI",
                'H', Blocks.hopper,
                'P', getOreItem("plateIron", Blocks.iron_block),
                'I', getOreItem("rodIron", Blocks.iron_bars));
    }

    private void armorRecipes()
    {
        //Hazmat helm
        addRecipe(new ItemStack(ASItems.itemArmorHazmatHelm),
                "CCC",
                "ILI",
                "GHG",
                'I', getOreItem("ingotLead", Items.iron_ingot),
                'C', Blocks.wool,
                'L', Items.leather_helmet,
                'G', Blocks.glass_pane,
                'H', getOreItem("circuitBasic", Blocks.gold_block));

        //Hazmat Body
        addRecipe(new ItemStack(ASItems.itemArmorHazmatChest),
                "CCC",
                "ILI",
                "THT",
                'I', getOreItem("ingotLead", Items.iron_ingot),
                'C', Blocks.wool,
                'L', Items.leather_chestplate,
                'T', ASItems.itemEmptyCell,
                'H', getOreItem("circuitBasic", Blocks.gold_block));

        //Hazmat Legs
        addRecipe(new ItemStack(ASItems.itemArmorHazmatLegs),
                "CCC",
                "ILI",
                "CHC",
                'I', getOreItem("ingotLead", Items.iron_ingot),
                'C', Blocks.wool,
                'L', Items.leather_leggings,
                'H', getOreItem("circuitBasic", Blocks.gold_block));

        //Hazmat boots
        addRecipe(new ItemStack(ASItems.itemArmorHazmatBoots),
                "CCC",
                "ILI",
                "CHC",
                'I', getOreItem("ingotLead", Items.iron_ingot),
                'C', Blocks.wool,
                'L', Items.leather_boots,
                'H', getOreItem("circuitBasic", Blocks.gold_block));
    }

    private void toolRecipes()
    {
        //Heat probe
        addRecipe(new ItemStack(ASItems.itemHeatProbe),
                "ICI",
                "ICI",
                "WBW",
                'I', getOreItem("ingotSteel", Items.iron_ingot),
                'W', getOreItem("wireCopper", Items.gold_ingot),
                'B', Items.gold_nugget,
                'G', Blocks.glass_pane,
                'C', getOreItem("circuitBasic", Blocks.gold_block));

        //Wrench
        addRecipe(new ItemStack(ASItems.itemWrench),
                "ICI",
                "GIG",
                "GIG",
                'I', getOreItem("ingotSteel", Items.iron_ingot),
                'G', Blocks.wool,
                'C', getOreItem("circuitBasic", Items.redstone));
    }

    public static void addRecipe(ItemStack output, Object... params)
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(output, params));
    }

    public static Item getItem(String id)
    {
        return (Item) Item.itemRegistry.getObject(id);
    }

    public static ItemStack getItem(String id, int meta)
    {
        Item item = getItem(id);
        if(item != null)
        {
            return new ItemStack(item, 1, meta);
        }
        else
        {
            Block block = getBlock(id);
            if(block != null && block != Blocks.air)
            {
                return new ItemStack(block, 1, meta);
            }
        }
        return null;
    }

    public static Block getBlock(String id)
    {
        return (Block) Block.blockRegistry.getObject(id);
    }

    public static Object getOreItem(String ore_name, ItemStack alt)
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
        return allowAltItems && alt != null ? alt : ore_name;
    }

    public static Object getOreItem(String ore_name, Item alt)
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
        return allowAltItems && alt != null ? alt : ore_name;
    }

    public static Object getOreItem(String ore_name, Block alt)
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
        return allowAltItems && alt != null ? alt : ore_name;
    }
}
