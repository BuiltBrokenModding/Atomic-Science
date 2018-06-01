package com.builtbroken.atomic.proxy.rf;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.machines.power.BlockPowerBus;
import com.builtbroken.atomic.content.machines.steam.generator.BlockSteamGenerator;
import com.builtbroken.atomic.content.machines.steam.generator.TileEntitySteamGenRF;
import com.builtbroken.atomic.content.recipes.ASRecipes;
import com.builtbroken.atomic.lib.power.PowerSystem;
import com.builtbroken.atomic.proxy.ContentProxy;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Proxy for Redstone Flux API
 * <p>
 * Handles seperate from mod interaction due to the API being broadly used by several mods.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class ProxyRedstoneFlux extends ContentProxy
{

    public static int conversationRateToRF = 1;

    public ProxyRedstoneFlux()
    {
        super("RF Power API");
    }

    @Override
    public boolean shouldLoad()
    {
        return super.shouldLoad();
    }

    @Override
    public void preInit()
    {
        AtomicScience.logger.info(this + " Loaded");
        PowerSystem.register(new PowerHandlerRFTile());

        if (doesClassExist("cofh.api.energy.IEnergyContainerItem"))
        {
            PowerSystem.register(new PowerHandlerRFItem());
        }

        BlockSteamGenerator.rfFactory = () -> new TileEntitySteamGenRF();
        GameRegistry.registerTileEntity(TileEntitySteamGenRF.class, AtomicScience.PREFIX + "steam_turbine_rf");

        BlockPowerBus.rfFactory = () -> new TileEntityPowerBusRF();
        GameRegistry.registerTileEntity(TileEntityPowerBusRF.class, AtomicScience.PREFIX + "power_bus_rf");

        //Disable vanilla side of recipes
        ASRecipes.allowAltItems = false;
    }

    @Override
    public void postInit()
    {
        ASRecipes.allowAltItems = true;
        itemRecipes();
        machineRecipes();
        reactorRecipes();
        armorRecipes();
        toolRecipes();
        ASRecipes.allowAltItems = false;
    }

    private void itemRecipes()
    {
        ItemStack hardened_cap = ASRecipes.getItem("ThermalExpansion:capacitor", 3);
        //Powered Cell
        addRecipe(new ItemStack(ASItems.itemPoweredCell),
                "TC",
                "CB",
                'C', ASRecipes.getItem("ThermalExpansion:material", 0),
                'B', hardened_cap != null ? hardened_cap : ASRecipes.getItem("ThermalExpansion:material", 3),
                'T', ASItems.itemEmptyCell);
    }

    private void machineRecipes()
    {
        //Centrifuge
        addRecipe(new ItemStack(ASBlocks.blockChemCentrifuge),
                "ICI",
                "TMT",
                "TPT",
                'I', ASRecipes.getOreItem("ingotInvar", Items.iron_ingot),
                'T', ASItems.itemEmptyCell,
                'M', ASRecipes.getItem("ThermalExpansion:Frame", 2),
                'P', ASRecipes.getOreItem("gearIron", Items.gold_ingot),
                'C', ASRecipes.getItem("ThermalExpansion:material", 3));

        //Boiler
        addRecipe(new ItemStack(ASBlocks.blockChemBoiler),
                "PCP",
                "TFT",
                "PMP",
                'F', Blocks.furnace,
                'T', ASItems.itemEmptyCell,
                'M', ASRecipes.getItem("ThermalExpansion:material", 3),
                'P', ASRecipes.getOreItem("gearIron", Items.gold_ingot),
                'C', ASRecipes.getItem("ThermalExpansion:Frame", 2));

        //Extractor
        addRecipe(new ItemStack(ASBlocks.blockChemExtractor),
                "IPI",
                "MCM",
                "IPI",
                'I', ASRecipes.getOreItem("gearIron", Blocks.iron_block),
                'M', ASRecipes.getItem("ThermalExpansion:material", 3),
                'P', ASRecipes.getOreItem("gearIron", Items.gold_ingot),
                'C', ASRecipes.getItem("ThermalExpansion:Frame", 2));
    }

    private void reactorRecipes()
    {
        //Reactor Core
        addRecipe(new ItemStack(ASBlocks.blockReactorCell),
                "PCP",
                "MTM",
                "PCP",
                'M', ASRecipes.getItem("ThermalExpansion:Frame", 2),
                'P', ASRecipes.getOreItem("gearIron", Blocks.iron_block),
                'C', ASRecipes.getItem("ThermalExpansion:material", 3),
                'C', ASItems.itemPoweredCell);

        //Turbine
        addRecipe(new ItemStack(ASBlocks.blockSteamTurbine, 1, 1), //RF turbine is meta value 1
                "IPI",
                "PMP",
                "IPI",
                'M', ASRecipes.getOreItem("gearInvar", Items.repeater),
                'P', ASRecipes.getOreItem("gearIron", Blocks.iron_block),
                'I', ASRecipes.getOreItem("ingotInvar", Blocks.iron_bars));

        //Turbine
        addRecipe(new ItemStack(ASBlocks.blockSteamFunnel),
                "IPI",
                "PHP",
                "IPI",
                'H', Blocks.hopper,
                'P', ASRecipes.getOreItem("gearIron", Blocks.iron_block),
                'I', ASRecipes.getOreItem("ingotInvar", Blocks.iron_bars));
    }

    private void armorRecipes()
    {
        //Hazmat helm
        addRecipe(new ItemStack(ASItems.itemArmorHazmatHelm),
                "CCC",
                "ILI",
                "GHG",
                'I', ASRecipes.getOreItem("ingotLead", Items.iron_ingot),
                'C', Blocks.wool,
                'L', Items.leather_helmet,
                'G', Blocks.glass_pane,
                'H', ASRecipes.getItem("ThermalExpansion:material", 3));

        //Hazmat Body
        addRecipe(new ItemStack(ASItems.itemArmorHazmatChest),
                "CCC",
                "ILI",
                "THT",
                'I', ASRecipes.getOreItem("ingotLead", Items.iron_ingot),
                'C', Blocks.wool,
                'L', Items.leather_chestplate,
                'T', ASItems.itemEmptyCell,
                'H', ASRecipes.getItem("ThermalExpansion:material", 3));

        //Hazmat Legs
        addRecipe(new ItemStack(ASItems.itemArmorHazmatLegs),
                "CCC",
                "ILI",
                "CHC",
                'I', ASRecipes.getOreItem("ingotLead", Items.iron_ingot),
                'C', Blocks.wool,
                'L', Items.leather_leggings,
                'H', ASRecipes.getItem("ThermalExpansion:material", 3));

        //Hazmat boots
        addRecipe(new ItemStack(ASItems.itemArmorHazmatBoots),
                "CCC",
                "ILI",
                "CHC",
                'I', ASRecipes.getOreItem("ingotLead", Items.iron_ingot),
                'C', Blocks.wool,
                'L', Items.leather_boots,
                'H', ASRecipes.getItem("ThermalExpansion:material", 3));
    }

    private void toolRecipes()
    {
        //Heat probe
        addRecipe(new ItemStack(ASItems.itemHeatProbe),
                "ICI",
                "ICI",
                "WBW",
                'I', ASRecipes.getOreItem("ingotInvar", Items.iron_ingot),
                'W', ASRecipes.getOreItem("gearCopper", Items.gold_ingot),
                'B', Items.gold_nugget,
                'G', Blocks.glass_pane,
                'C', ASRecipes.getItem("ThermalExpansion:material", 3));

        //Wrench
        addRecipe(new ItemStack(ASItems.itemWrench),
                "ICI",
                "GIG",
                "GIG",
                'I', ASRecipes.getOreItem("ingotInvar", Items.iron_ingot),
                'G', Blocks.wool,
                'C', ASRecipes.getItem("ThermalExpansion:material", 3));
    }

    protected void addRecipe(ItemStack output, Object... params)
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(output, params));
    }

    public static int toRF(int fromUE)
    {
        return fromUE * conversationRateToRF;
    }

    public static int toUE(int fromPower)
    {
        return fromPower / conversationRateToRF;
    }
}
