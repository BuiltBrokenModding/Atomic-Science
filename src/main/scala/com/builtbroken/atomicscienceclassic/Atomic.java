package com.builtbroken.atomicscienceclassic;

import com.builtbroken.atomicscienceclassic.content.BlockUraniumOre;
import com.builtbroken.atomicscienceclassic.content.items.*;
import com.builtbroken.atomicscienceclassic.content.BlockToxicWaste;
import com.builtbroken.atomicscienceclassic.content.reactor.fission.TileControlRod;
import com.builtbroken.atomicscienceclassic.content.reactor.fission.TileReactorCell;
import com.builtbroken.atomicscienceclassic.content.reactor.fusion.BlockPlasmaHeater;
import com.builtbroken.atomicscienceclassic.content.reactor.TileElectromagnet;
import com.builtbroken.atomicscienceclassic.content.reactor.fusion.TilePlasma;
import com.builtbroken.atomicscienceclassic.content.reactor.fusion.TilePlasmaHeater;
import com.builtbroken.atomicscienceclassic.content.accelerator.BlockAccelerator;
import com.builtbroken.atomicscienceclassic.content.accelerator.EntityParticle;
import com.builtbroken.atomicscienceclassic.content.accelerator.ItemDarkMatter;
import com.builtbroken.atomicscienceclassic.content.accelerator.TileAccelerator;
import com.builtbroken.atomicscienceclassic.content.fulmination.FulminationHandler;
import com.builtbroken.atomicscienceclassic.content.items.ItemAntimatter;
import com.builtbroken.atomicscienceclassic.content.fulmination.TileFulmination;
import com.builtbroken.atomicscienceclassic.content.quantum.TileQuantumAssembler;
import com.builtbroken.atomicscienceclassic.content.extractor.BlockChemicalExtractor;
import com.builtbroken.atomicscienceclassic.content.items.ItemHazmat;
import com.builtbroken.atomicscienceclassic.content.extractor.TileChemicalExtractor;
import com.builtbroken.atomicscienceclassic.content.centrifuge.BlockCentrifuge;
import com.builtbroken.atomicscienceclassic.content.boiler.BlockNuclearBoiler;
import com.builtbroken.atomicscienceclassic.content.centrifuge.TileCentrifuge;
import com.builtbroken.atomicscienceclassic.content.boiler.TileNuclearBoiler;
import com.builtbroken.atomicscienceclassic.content.sensor.TileSiren;
import com.builtbroken.atomicscienceclassic.content.sensor.TileThermometer;
import com.builtbroken.atomicscienceclassic.content.turbine.BlockElectricTurbine;
import com.builtbroken.atomicscienceclassic.content.turbine.TileElectricTurbine;
import com.builtbroken.atomicscienceclassic.content.turbine.TileFunnel;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.EngineCoreMod;
import com.builtbroken.mc.lib.helper.NBTUtility;
import com.builtbroken.mc.lib.helper.recipe.UniversalRecipe;
import com.builtbroken.mc.framework.mod.AbstractMod;
import com.builtbroken.mc.lib.render.RenderUtility;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Iterator;
import java.util.Map;

@Mod(modid = Atomic.DOMAIN, name = "Atomic Science: Classic", version = "@MAJOR@.@MINOR@.@REVIS@.@BUILD@", dependencies = "required-after:VoltzEngine")
public final class Atomic extends AbstractMod
{
    public static final String DOMAIN = "atomicscienceclassic";
    public static final String PREFIX = DOMAIN + ":";

    public static final String ASSETS_PATH = "assets/atomicscienceclassic";
    public static final String MODEL_PATH = "models/";
    public static final String MODEL_DIRECTORY = ASSETS_PATH + MODEL_PATH;

    public static final int ENTITY_ID_PREFIX = 49;
    public static final int SECOND_IN_TICKS = 20;

    public static final ItemArmor.ArmorMaterial hazmatArmorMaterial = EnumHelper.addArmorMaterial("HAZMAT", 0, new int[] { 0, 0, 0, 0 }, 0);


    @Mod.Instance(DOMAIN)
    public static Atomic INSTANCE;

    @SidedProxy(clientSide = "com.builtbroken.atomic.ClientProxy", serverSide = "com.builtbroken.atomic.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Metadata(DOMAIN)
    public static ModMetadata metadata;

    /** Block and Items */
    public static Block blockRadioactive;
    public static Block blockCentrifuge;
    public static Block blockElectricTurbine;
    public static Block blockNuclearBoiler;
    public static Block blockControlRod;
    public static Block blockThermometer;
    public static Block blockFusionCore;
    public static Block blockPlasma;
    public static Block blockElectromagnet;
    public static Block blockChemicalExtractor;
    public static Block blockSiren;
    public static Block blockSteamFunnel;
    public static Block blockAccelerator;
    public static Block blockFulmination;
    public static Block blockQuantumAssembler;
    public static Block blockReactorCell;

    /** Cells */
    public static Item itemCell, itemFissileFuel, itemBreedingRod, itemDarkMatter, itemAntimatter, itemDeuteriumCell, itemTritiumCell, itemWaterCell;
    public static Item itemBucketToxic;

    /** Uranium Related Items */
    public static Block blockUraniumOre;
    public static Item itemYellowCake;
    public static Item itemUranium;
    public static Item itemHazmatTop;
    public static Item itemHazmatBody;
    public static Item itemHazmatLeggings;
    public static Item itemHazmatBoots;

    /** Fluids */
    public static Block blockToxicWaste;

    /** Water, Uranium Hexafluoride, Steam, Deuterium, Toxic waste */
    public static FluidStack FLUIDSTACK_WATER, FLUIDSTACK_URANIUM_HEXAFLOURIDE, FLUIDSTACK_STEAM, FLUIDSTACK_DEUTERIUM, FLUIDSTACK_TRITIUM, FLUIDSTACK_TOXIC_WASTE;
    public static Fluid FLUID_URANIUM_HEXAFLOURIDE, FLUID_PLASMA, FLUID_STEAM, FLUID_DEUTERIUM, FLUID_TRITIUM, FLUID_TOXIC_WASTE;

    public Atomic(String domain) {
        super(domain);
    }

    /** Is this ItemStack a cell?
     *
     * @param itemStack
     * @return */
    public static boolean isItemStackEmptyCell(ItemStack itemStack)
    {
        return isItemStackOreDictionaryCompatible(itemStack, "cellEmpty");
    }

    public static boolean isItemStackWaterCell(ItemStack itemStack)
    {
        return isItemStackOreDictionaryCompatible(itemStack, "cellWater");
    }

    public static boolean isItemStackUraniumOre(ItemStack itemStack)
    {
        return isItemStackOreDictionaryCompatible(itemStack, "dropUranium", "oreUranium");
    }

    public static boolean isItemStackDeuteriumCell(ItemStack itemStack)
    {
        return isItemStackOreDictionaryCompatible(itemStack, "molecule_1d", "molecule_1h2", "cellDeuterium");
    }

    public static boolean isItemStackTritiumCell(ItemStack itemStack)
    {
        return isItemStackOreDictionaryCompatible(itemStack, "molecule_h3", "cellTritium");
    }

    /** Compare to Ore Dict
     *
     * @param itemStack
     * @return */
    public static boolean isItemStackOreDictionaryCompatible(ItemStack itemStack, String... names)
    {
        if (itemStack != null && names != null && names.length > 0)
        {
            String name = OreDictionary.getOreName(OreDictionary.getOreID(itemStack));

            for (String compareName : names)
            {
                if (name.equals(compareName))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static int getFluidAmount(FluidStack fluid)
    {
        if (fluid != null)
        {
            return fluid.amount;
        }
        return 0;
    }

    @Override
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);

        //PacketAnnotation.register(TileElectricTurbine.class);
        //PacketAnnotation.register(TileReactorCell.class);
        //PacketAnnotation.register(TileThermometer.class);

       // BlockCreativeBuilder.register(new SchematicAccelerator());
        //BlockCreativeBuilder.register(new SchematicBreedingReactor());
       // BlockCreativeBuilder.register(new SchematicFissionReactor());
        //BlockCreativeBuilder.register(new SchematicFusionReactor());
        
        /** Register Packets */
        //PacketAnnotation.register(TileAccelerator.class);
        //PacketAnnotation.register(TileChemicalExtractor.class);
        //PacketAnnotation.register(TileNuclearBoiler.class);
        //PacketAnnotation.register(TileElectricTurbine.class);

        /** Registers Gases & Fluids */
        FLUID_URANIUM_HEXAFLOURIDE = new Fluid("uraniumhexafluoride").setGaseous(true);
        FLUID_STEAM = new Fluid("steam").setGaseous(true);
        FLUID_DEUTERIUM = new Fluid("deuterium").setGaseous(true);
        FLUID_TRITIUM = new Fluid("tritium").setGaseous(true);
        FLUID_TOXIC_WASTE = new Fluid("toxicwaste");
        FLUID_PLASMA = new Fluid("plasma").setGaseous(true);

        FluidRegistry.registerFluid(FLUID_URANIUM_HEXAFLOURIDE);
        FluidRegistry.registerFluid(FLUID_STEAM);
        FluidRegistry.registerFluid(FLUID_TRITIUM);
        FluidRegistry.registerFluid(FLUID_DEUTERIUM);
        FluidRegistry.registerFluid(FLUID_TOXIC_WASTE);
        FluidRegistry.registerFluid(FLUID_PLASMA);

        /** Fluid Stack Reference Initialization */
        FLUIDSTACK_WATER = new FluidStack(FluidRegistry.WATER, 0);
        FLUIDSTACK_URANIUM_HEXAFLOURIDE = new FluidStack(FLUID_URANIUM_HEXAFLOURIDE, 0);
        FLUIDSTACK_STEAM = new FluidStack(FluidRegistry.getFluidID("steam"), 0);
        FLUIDSTACK_DEUTERIUM = new FluidStack(FluidRegistry.getFluidID("deuterium"), 0);
        FLUIDSTACK_TRITIUM = new FluidStack(FluidRegistry.getFluidID("tritium"), 0);
        FLUIDSTACK_TOXIC_WASTE = new FluidStack(FluidRegistry.getFluidID("toxicwaste"), 0);

        /** Block Initiation */
        blockRadioactive = getManager().newBlock(BlockRadioactive.class).setUnlocalizedName(PREFIX + "radioactive").setTextureName(PREFIX + "radioactive").setCreativeTab(TabRI.DEFAULT);
        blockUraniumOre = getManager().newBlock(BlockUraniumOre.class);

        blockElectricTurbine = getManager().newBlock(BlockElectricTurbine.class, TileElectricTurbine.class);
        blockCentrifuge = getManager().newBlock(BlockCentrifuge.class, TileCentrifuge.class);
        blockReactorCell = getManager().newBlock(TileReactorCell.class);
        blockNuclearBoiler = getManager().newBlock(BlockNuclearBoiler.class, TileNuclearBoiler.class);
        blockChemicalExtractor = getManager().newBlock(BlockChemicalExtractor.class, TileChemicalExtractor.class);
        blockFusionCore = getManager().newBlock(BlockPlasmaHeater.class, TilePlasmaHeater.class);
        blockControlRod = getManager().newBlock(TileControlRod.class);
        blockThermometer = getManager().newBlock(TileThermometer.class);
        blockPlasma = getManager().newBlock(TilePlasma.class);
        blockElectromagnet = getManager().newBlock(TileElectromagnet.class);
        blockSiren = getManager().newBlock(TileSiren.class);
        blockSteamFunnel = getManager().newBlock(TileFunnel.class);
        blockAccelerator = getManager().newBlock(BlockAccelerator.class, TileAccelerator.class);
        blockFulmination = getManager().newBlock(TileFulmination.class);
        blockQuantumAssembler = getManager().newBlock(TileQuantumAssembler.class);
        blockToxicWaste = getManager().newBlock(BlockToxicWaste.class).setCreativeTab(null);

        /** Items */
        itemHazmatTop = new ItemHazmat(Settings.CONFIGURATION.getItem("HazmatTop", Settings.getNextItemID()).getInt(), hazmatArmorMaterial, proxy.getArmorIndex("hazmat"), 0).setUnlocalizedName(PREFIX + "hazmatMask");
        itemHazmatBody = new ItemHazmat(Settings.CONFIGURATION.getItem("HazmatBody", Settings.getNextItemID()).getInt(), hazmatArmorMaterial, proxy.getArmorIndex("hazmat"), 1).setUnlocalizedName(PREFIX + "hazmatBody");
        itemHazmatLeggings = new ItemHazmat(Settings.CONFIGURATION.getItem("HazmatBottom", Settings.getNextItemID()).getInt(), hazmatArmorMaterial, proxy.getArmorIndex("hazmat"), 2).setUnlocalizedName(PREFIX + "hazmatLeggings");
        itemHazmatBoots = new ItemHazmat(Settings.CONFIGURATION.getItem("HazmatBoots", Settings.getNextItemID()).getInt(), hazmatArmorMaterial, proxy.getArmorIndex("hazmat"), 3).setUnlocalizedName(PREFIX + "hazmatBoots");

        itemCell = getManager().newItem("cellEmpty", Item.class);
        itemFissileFuel = getManager().newItem("rodFissileFuel", ItemFissileFuel.class);
        itemDeuteriumCell = getManager().newItem("cellDeuterium", ItemCell.class);
        itemTritiumCell = getManager().newItem("cellTritium", ItemCell.class);
        itemWaterCell = getManager().newItem("cellWater", ItemCell.class);
        itemDarkMatter = getManager().newItem("darkMatter", ItemDarkMatter.class);
        itemAntimatter = getManager().newItem("antimatter", ItemAntimatter.class);
        itemBreedingRod = getManager().newItem("rodBreederFuel", ItemBreederFuel.class);

        itemYellowCake = getManager().newItem("yellowcake", ItemRadioactive.class);
        itemUranium = getManager().newItem(ItemUranium.class);

        /** Fluid Item Initialization */
        FLUID_PLASMA.setBlockID(blockPlasma);

        int bucketID = Settings.getNextItemID();
        itemBucketToxic = (new ItemBucket(Settings.CONFIGURATION.getItem("Toxic Waste Bucket", bucketID).getInt(bucketID), blockToxicWaste.blockID)).setCreativeTab(TabRI.DEFAULT).setUnlocalizedName(PREFIX + "bucketToxicWaste")
                .setContainerItem(Items.bucket).setTextureName(PREFIX + "bucketToxicWaste");

        FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluid("toxicwaste"), new ItemStack(itemBucketToxic), new ItemStack(Item.bucketEmpty));
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.WATER, new ItemStack(itemWaterCell), new ItemStack(itemCell));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("deuterium"), 200), new ItemStack(itemDeuteriumCell), new ItemStack(itemCell));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("tritium"), 200), new ItemStack(itemTritiumCell), new ItemStack(itemCell));

        if (OreDictionary.getOres("oreUranium").size() > 1 && Settings.CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Auto Disable Uranium If Exist", false).getBoolean(false))
        {
            Engine.logger().info("Disabled Uranium Generation. Detected another uranium being generated: " + OreDictionary.getOres("oreUranium").size());
        }
        else
        {
            uraniumOreGeneration = new OreGenReplaceStone("Uranium Ore", "oreUranium", new ItemStack(blockUraniumOre), 0, 25, 9, 3, "pickaxe", 2);
            uraniumOreGeneration.enable(Settings.CONFIGURATION);
            OreGenerator.addOre(uraniumOreGeneration);
            Engine.logger().info("Added Atomic Science uranium to ore generator.");
        }

        Settings.CONFIGURATION.save();

        MinecraftForge.EVENT_BUS.register(itemAntimatter);
        MinecraftForge.EVENT_BUS.register(FulminationHandler.INSTANCE);

        /** Cell registry. */
        if (Settings.allowOreDictionaryCompatibility)
        {
            OreDictionary.registerOre("ingotUranium", itemUranium);
            OreDictionary.registerOre("dustUranium", itemYellowCake);
        }

        OreDictionary.registerOre("breederUranium", new ItemStack(itemUranium, 1, 1));
        OreDictionary.registerOre("blockRadioactive", blockRadioactive);

        OreDictionary.registerOre("cellEmpty", itemCell);
        OreDictionary.registerOre("cellUranium", itemFissileFuel);
        OreDictionary.registerOre("cellTritium", itemTritiumCell);
        OreDictionary.registerOre("cellDeuterium", itemDeuteriumCell);
        OreDictionary.registerOre("cellWater", itemWaterCell);
        OreDictionary.registerOre("strangeMatter", itemDarkMatter);
        OreDictionary.registerOre("antimatterMilligram", new ItemStack(itemAntimatter, 1, 0));
        OreDictionary.registerOre("antimatterGram", new ItemStack(itemAntimatter, 1, 1));

        Settings.CONFIGURATION.save();
        TabRI.ITEMSTACK = new ItemStack(blockReactorCell);
    }

    @Override
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }

    @Override
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        /** IC2 Recipes */
        if (Loader.isModLoaded("IC2") && Settings.allowAlternateRecipes)
        {
            OreDictionary.registerOre("cellEmpty", Items.getItem("cell"));

            // Check to make sure we have actually registered the Ore, otherwise tell the user about
            // it.
            String cellEmptyName = OreDictionary.getOreName(OreDictionary.getOreID("cellEmpty"));
            if (cellEmptyName == "Unknown")
            {
                Engine.logger().info("Unable to register cellEmpty in OreDictionary!");
            }

            // IC2 exchangeable recipes
            GameRegistry.addRecipe(new ShapelessOreRecipe(itemYellowCake, Items.getItem("reactorUraniumSimple")));
            GameRegistry.addRecipe(new ShapelessOreRecipe(Items.getItem("cell"), itemCell));
            GameRegistry.addRecipe(new ShapelessOreRecipe(itemCell, "cellEmpty"));
        }

        // Antimatter
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemAntimatter, 1, 1), new Object[]
        { itemAntimatter, itemAntimatter, itemAntimatter, itemAntimatter, itemAntimatter, itemAntimatter, itemAntimatter, itemAntimatter }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemAntimatter, 8, 0), new Object[]
        { new ItemStack(itemAntimatter, 1, 1) }));

        // Steam Funnel
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSteamFunnel, 2), new Object[]
        { " B ", "B B", "B B", 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes) }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSteamFunnel, 2), new Object[]
        { " B ", "B B", "B B", 'B', "ingotIron" }));

        // Atomic Assembler
        GameRegistry.addRecipe(new ShapedOreRecipe(blockQuantumAssembler, new Object[]
        { "CCC", "SXS", "SSS", 'X', blockCentrifuge, 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes) }));

        // Fulmination Generator
        GameRegistry.addRecipe(new ShapedOreRecipe(blockFulmination, new Object[]
        { "OSO", "SCS", "OSO", 'O', Blocks.obsidian, 'C', UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes) }));

        // Particle Accelerator
        GameRegistry.addRecipe(new ShapedOreRecipe(blockAccelerator, new Object[]
        { "SCS", "CMC", "SCS", 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes), 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes) }));

        // Centrifuge
        GameRegistry.addRecipe(new ShapedOreRecipe(blockCentrifuge, new Object[]
        { "BSB", "MCM", "BSB", 'C', UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), 'M',
                UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes) }));

        // Nuclear Boiler
        GameRegistry.addRecipe(new ShapedOreRecipe(blockNuclearBoiler, new Object[]
        { "S S", "FBF", "SMS", 'F', Blocks.furnace, 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'B', Items.bucket, 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes) }));

        // Chemical Extractor
        GameRegistry.addRecipe(new ShapedOreRecipe(blockChemicalExtractor, new Object[]
        { "BSB", "MCM", "BSB", 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), 'M',
                UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes) }));

        // Siren
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSiren, 2), new Object[]
        { "NPN", 'N', Blocks.jukebox, 'P', UniversalRecipe.SECONDARY_PLATE.get(Settings.allowAlternateRecipes) }));

        // Fission Reactor
        GameRegistry
                .addRecipe(new ShapedOreRecipe(blockReactorCell, new Object[]
                { "SCS", "MEM", "SCS", 'E', "cellEmpty", 'C', UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'M',
                        UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes) }));

        // Fusion Reactor
        GameRegistry.addRecipe(new ShapedOreRecipe(blockFusionCore, new Object[]
        { "CPC", "PFP", "CPC", 'P', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'F', blockReactorCell, 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes) }));

        // Turbine
        GameRegistry.addRecipe(new ShapedOreRecipe(blockElectricTurbine, new Object[]
        { " B ", "BMB", " B ", 'B', UniversalRecipe.SECONDARY_PLATE.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes) }));

        // Empty Cell
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemCell, 16), new Object[]
        { " T ", "TGT", " T ", 'T', "ingotTin", 'G', Blocks.glass }));

        // Water Cell
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemWaterCell), new Object[]
        { "cellEmpty", Items.water_bucket }));

        // Thermometer
        GameRegistry.addRecipe(new ShapedOreRecipe(blockThermometer, new Object[]
        { "SSS", "GCG", "GSG", 'S', UniversalRecipe.PRIMARY_METAL.get(Settings.allowAlternateRecipes), 'G', Blocks.glass, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes) }));

        // Control Rod
        GameRegistry.addRecipe(new ShapedOreRecipe(blockControlRod, new Object[]
        { "I", "I", "I", 'I', Items.iron_ingot }));

        // Fuel Rod
        GameRegistry.addRecipe(new ShapedOreRecipe(itemFissileFuel, new Object[]
        { "CUC", "CUC", "CUC", 'U', "ingotUranium", 'C', "cellEmpty" }));

        // Breeder Rod
        GameRegistry.addRecipe(new ShapedOreRecipe(itemBreedingRod, new Object[]
        { "CUC", "CUC", "CUC", 'U', "breederUranium", 'C', "cellEmpty" }));

        // Electromagnet
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockElectromagnet, 2, 0), new Object[]
        { "BBB", "BMB", "BBB", 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes) }));

        // Electromagnet Glass
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(blockElectromagnet, 1, 1), new Object[]
        { blockElectromagnet, Blocks.glass }));

        // Hazmat Suit
        GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatTop, new Object[]
        { "SSS", "BAB", "SCS", 'A', Items.leather_helmet, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Block.cloth }));
        GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatBody, new Object[]
        { "SSS", "BAB", "SCS", 'A', Items.leather_chestplate, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Block.cloth }));
        GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatLeggings, new Object[]
        { "SSS", "BAB", "SCS", 'A', Items.leather_leggings, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Block.cloth }));
        GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatBoots, new Object[]
        { "SSS", "BAB", "SCS", 'A', Items.leather_boots, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Block.cloth }));

        EntityRegistry.registerGlobalEntityID(EntityParticle.class, "ASParticle", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityParticle.class, "ASParticle", ENTITY_ID_PREFIX, this, 80, 3, true);

        Settings.CONFIGURATION.load();

        if (Loader.isModLoaded("IC2") && Settings.allowAlternateRecipes)
        {
            if (Settings.allowIC2UraniumCompression)
            {
                try
                {
                    if (Recipes.compressor != null)
                    {
                        Map<IRecipeInput, RecipeOutput> compressorRecipes = Recipes.compressor.getRecipes();
                        Iterator<Map.Entry<IRecipeInput, RecipeOutput>> it = compressorRecipes.entrySet().iterator();
                        int i = 0;

                        while (it.hasNext())
                        {
                            Map.Entry<IRecipeInput, RecipeOutput> entry = it.next();

                            for (ItemStack checkStack : entry.getKey().getInputs())
                            {
                                if (isItemStackUraniumOre(checkStack))
                                {
                                    i++;
                                    it.remove();
                                }
                            }
                        }

                        Engine.logger().info("Removed " + i + " IC2 uranium compression recipe, use centrifuge instead.");
                    }
                }
                catch (Exception e)
                {
                    Engine.logger().info("Failed to remove IC2 compressor recipes.");
                    e.printStackTrace();
                }
            }
        }

        /** Atomic Assembler Recipes */
        if (Settings.quantumAssemblerGenerateMode > 0)
        {
            for (Item item : Item.itemsList)
            {
                if (item != null)
                {
                    if (item.itemID > 256 || Settings.quantumAssemblerGenerateMode == 2)
                    {
                        ItemStack itemStack = new ItemStack(item);

                        if (itemStack != null)
                        {
                            QuantumAssemblerRecipes.addRecipe(itemStack);
                        }
                    }
                }
            }

            if (Settings.quantumAssemblerGenerateMode == 2)
            {
                for (Block block : Block.blocksList)
                {
                    if (block != null)
                    {
                        ItemStack itemStack = new ItemStack(block);
                        if (itemStack != null)
                        {
                            QuantumAssemblerRecipes.addRecipe(itemStack);
                        }
                    }
                }
            }

            for (String oreName : OreDictionary.getOreNames())
            {
                if (oreName.startsWith("ingot"))
                {
                    for (ItemStack itemStack : OreDictionary.getOres(oreName))
                    {
                        if (itemStack != null)
                        {
                            QuantumAssemblerRecipes.addRecipe(itemStack);
                        }
                    }
                }
            }
        }

        Settings.CONFIGURATION.save();
    }

    @Override
    public CommonProxy getProxy() {
        return null;
    }

    @SubscribeEvent
    public void thermalEventHandler(EventThermalUpdate evt)
    {
        VectorWorld pos = evt.position;
        Block block = Block.blocksList[pos.getBlockID()];

        if (block == blockElectromagnet)
        {
            evt.heatLoss = evt.deltaTemperature * 0.6f;
        }
    }

    @ForgeSubscribe
    public void plasmaEvent(SpawnPlasmaEvent evt)
    {
        World world = evt.world;
        Vec3 position = new Vec3(evt.x, evt.y, evt.z);
        int blockID = position.getBlockID(world);

        Block block = Block.blocksList[blockID];

        if (block != null)
        {
            if (block.blockID == Blocks.bedrock|| block.blockID == Blocks.iron_block)
            {
                return;
            }

            TileEntity tile = position.getTileEntity(world);

            if (tile instanceof TilePlasma)
            {
                ((TilePlasma) tile).setTemperature(evt.temperature);
                return;
            }

            if (tile instanceof IElectromagnet)
            {
                return;
            }
        }

        position.setBlock(world, blockPlasma.blockID);

        TileEntity tile = position.getTileEntity(world);

        if (tile instanceof TilePlasma)
        {
            ((TilePlasma) tile).setTemperature(evt.temperature);
        }
    }

    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void preTextureHook(TextureStitchEvent.Pre event)
    {
        if (event.map.getTextureType() == 0)
        {
            RenderUtility.registerIcon(PREFIX + "uraniumHexafluoride", event.map);
            RenderUtility.registerIcon(PREFIX + "steam", event.map);
            RenderUtility.registerIcon(PREFIX + "deuterium", event.map);
            RenderUtility.registerIcon(PREFIX + "tritium", event.map);
            RenderUtility.registerIcon(PREFIX + "atomic_edge", event.map);
            RenderUtility.registerIcon(PREFIX + "funnel_edge", event.map);
            RenderUtility.registerIcon(PREFIX + "glass", event.map);
        }
    }

    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void postTextureHook(TextureStitchEvent.Post event)
    {
        FLUID_URANIUM_HEXAFLOURIDE.setIcons(RenderUtility.loadedIconMap.get(PREFIX + "uraniumHexafluoride"));
        FLUID_STEAM.setIcons(RenderUtility.loadedIconMap.get(PREFIX + "steam"));
        FLUID_DEUTERIUM.setIcons(RenderUtility.loadedIconMap.get(PREFIX + "deuterium"));
        FLUID_TRITIUM.setIcons(RenderUtility.loadedIconMap.get(PREFIX + "tritium"));
        FLUID_TOXIC_WASTE.setIcons(blockToxicWaste.getIcon(0, 0));
        FLUID_PLASMA.setIcons(blockPlasma.getIcon(0, 0));
    }

    @ForgeSubscribe
    public void worldSave(WorldEvent.Save evt)
    {
        if (!evt.world.isRemote)
        {
            if (FlagRegistry.getModFlag(FlagRegistry.DEFAULT_NAME) != null)
            {
                NBTUtility.saveData(FlagRegistry.DEFAULT_NAME, FlagRegistry.getModFlag(FlagRegistry.DEFAULT_NAME).getNBT());
            }
        }
    }

    @ForgeSubscribe
    public void fillBucketEvent(FillBucketEvent evt)
    {
        if (!evt.world.isRemote && evt.target != null && evt.target.typeOfHit == EnumMovingObjectType.TILE)
        {
            Vec3 blockPos = new Vec3(evt.target);
            int blockID = blockPos.getBlockID(evt.world);

            if (blockID == blockToxicWaste.blockID)
            {
                blockPos.setBlock(evt.world, 0);
                evt.result = new ItemStack(itemBucketToxic);
                evt.setResult(Event.Result.ALLOW);
            }
        }
    }

    /** Recipes */
    public static enum RecipeType {
        CHEMICAL_EXTRACTOR;
    }
}
