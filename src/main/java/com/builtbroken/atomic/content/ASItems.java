package com.builtbroken.atomic.content;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.config.content.ConfigContent;
import com.builtbroken.atomic.config.logic.ConfigRadiation;
import com.builtbroken.atomic.content.items.ItemFuelRod;
import com.builtbroken.atomic.content.items.ItemHazmat;
import com.builtbroken.atomic.content.items.ItemHeatProbe;
import com.builtbroken.atomic.content.items.ItemRadioactive;
import com.builtbroken.atomic.content.items.cell.CreativeTabCells;
import com.builtbroken.atomic.content.items.cell.ItemFluidCell;
import com.builtbroken.atomic.content.items.cell.ItemPoweredCell;
import com.builtbroken.atomic.content.items.wrench.ItemWrench;
import net.minecraft.block.Block;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
@Mod.EventBusSubscriber(modid = AtomicScience.DOMAIN)
public final class ASItems
{
    //Armor
    public static ItemHazmat itemArmorHazmatHelm;
    public static ItemHazmat itemArmorHazmatChest;
    public static ItemHazmat itemArmorHazmatLegs;
    public static ItemHazmat itemArmorHazmatBoots;

    //Fluid Cells
    public static ItemFluidCell itemFluidCell; //Generic fluid cell (replaces water cell, toxic waste bucket)
    public static ItemPoweredCell itemPoweredCell; //Generic fluid cell (replaces Anti-matter and strange-matter cells)

    //Machine inputs
    public static Item itemFissileFuelCell;
    public static Item itemBreederFuelCell;

    //Crafting items
    public static Item itemEmptyCell; //crafting item (replaces empty cell)
    public static Item itemYellowCake;
    public static Item itemUranium235;
    public static Item itemUranium238;

    //Waste items
    public static Item itemProcessingWaste;
    public static Item itemToxicWaste;

    //Tools
    public static Item itemHeatProbe;
    public static ItemWrench itemWrench;


    public static ItemBlock blockReactorCell;
    public static ItemBlock blockReactorController;
    public static ItemBlock blockSteamFunnel;
    public static ItemBlock blockSteamTurbine;

    public static ItemBlock blockUraniumOre;

    public static ItemBlock blockChemExtractor;
    public static ItemBlock blockChemBoiler;
    public static ItemBlock blockChemCentrifuge;


    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        //Armor
        ItemHazmat.hazmatArmorMaterial = EnumHelper.addArmorMaterial("HAZMAT", "HAZMAT", 5, new int[]{0, 0, 0, 0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
        event.getRegistry().register(itemArmorHazmatHelm = new ItemHazmat(EntityEquipmentSlot.HEAD, "mask"));
        event.getRegistry().register(itemArmorHazmatChest = new ItemHazmat(EntityEquipmentSlot.CHEST, "body"));
        event.getRegistry().register(itemArmorHazmatLegs = new ItemHazmat(EntityEquipmentSlot.LEGS, "leggings"));
        event.getRegistry().register(itemArmorHazmatBoots = new ItemHazmat(EntityEquipmentSlot.FEET, "boots"));

        //Cells
        event.getRegistry().register(itemFluidCell = (ItemFluidCell) new ItemFluidCell(Fluid.BUCKET_VOLUME).setRegistryName(AtomicScience.PREFIX + "fluid_cell"));
        itemFluidCell.addSupportedFluid(FluidRegistry.WATER, AtomicScience.PREFIX + "items/cell_water", itemFluidCell.getTranslationKey() + ".water");
        itemFluidCell.addSupportedFluid(ASFluids.DEUTERIUM.fluid, AtomicScience.PREFIX + "items/cell_deuterium", itemFluidCell.getTranslationKey() + ".deuterium");


        event.getRegistry().register(itemPoweredCell = (ItemPoweredCell) new ItemPoweredCell().setRegistryName(AtomicScience.PREFIX + "powered_cell"));
        itemPoweredCell.addSupportedFluid(ASFluids.ANTIMATTER.fluid, AtomicScience.PREFIX + "items/cell_antimatter", itemPoweredCell.getTranslationKey() + ".antimatter");
        itemPoweredCell.addSupportedFluid(ASFluids.STRANGE_MATTER.fluid, AtomicScience.PREFIX + "items/cell_strange_matter", itemPoweredCell.getTranslationKey() + ".strange_matter");


        //Machine inputs
        event.getRegistry().register(itemFissileFuelCell = new ItemFuelRod("cell.fuel.fissile",
                () -> ConfigContent.REACTOR.FUEL_ROD_RUNTIME,
                () -> ConfigRadiation.RADIOACTIVE_MAT_VALUE_FUEL_ROD
                , () -> ConfigRadiation.RADIOACTIVE_REACTOR_VALUE_FUEL_ROD,
                () -> ConfigContent.REACTOR.HEAT_REACTOR_FUEL_ROD)
                .setRegistryName(AtomicScience.PREFIX + "fissile_fuel_cell"));
        event.getRegistry().register(itemBreederFuelCell = new ItemFuelRod("cell.fuel.breeder",
                () -> ConfigContent.REACTOR.BREEDER_ROD_RUNTIME,
                () -> ConfigRadiation.RADIOACTIVE_MAT_VALUE_BREEDER_ROD
                , () -> ConfigRadiation.RADIOACTIVE_REACTOR_VALUE_BREEDER_ROD,
                () -> ConfigContent.REACTOR.HEAT_REACTOR_BREEDER_ROD)
                .setRegistryName(AtomicScience.PREFIX + "breeder_fuel_cell"));

        //Crafting items
        event.getRegistry().register(itemEmptyCell = new Item()
                .setTranslationKey(AtomicScience.PREFIX + "cell.empty")
                .setCreativeTab(AtomicScience.creativeTab)
                .setRegistryName(AtomicScience.PREFIX + "cell_empty"));

        event.getRegistry().register(itemYellowCake = new ItemRadioactive("cake.yellow", () -> ConfigRadiation.RADIOACTIVE_MAT_VALUE_YELLOW_CAKE).setRegistryName(AtomicScience.PREFIX + "yellow_cake"));
        event.getRegistry().register(itemUranium235 = new ItemRadioactive("uranium.235", () -> ConfigRadiation.RADIOACTIVE_MAT_VALUE_U235).setRegistryName(AtomicScience.PREFIX + "uranium_235"));
        event.getRegistry().register(itemUranium238 = new ItemRadioactive("uranium.238", () -> ConfigRadiation.RADIOACTIVE_MAT_VALUE_U238).setRegistryName(AtomicScience.PREFIX + "uranium_238"));

        //Waste items
        event.getRegistry().register(itemProcessingWaste = new ItemRadioactive("processing.waste", () -> ConfigRadiation.RADIOACTIVE_MAT_VALUE_YELLOW_CAKE).setRegistryName(AtomicScience.PREFIX + "processing_waste"));
        event.getRegistry().register(itemToxicWaste = new ItemRadioactive("toxic.waste", () -> ConfigRadiation.RADIOACTIVE_MAT_VALUE_YELLOW_CAKE).setRegistryName(AtomicScience.PREFIX + "toxic_waste"));

        //Tools
        event.getRegistry().register(itemHeatProbe = new ItemHeatProbe());
        event.getRegistry().register(itemWrench = new ItemWrench());

        //Register item blocks
        blockReactorCell = addItemBlock(event.getRegistry(), ASBlocks.blockReactorCell);
        blockReactorController = addItemBlock(event.getRegistry(), ASBlocks.blockReactorController);
        blockSteamFunnel = addItemBlock(event.getRegistry(), ASBlocks.blockSteamFunnel);
        blockSteamTurbine = addItemBlock(event.getRegistry(), ASBlocks.blockSteamTurbine);

        blockUraniumOre = addItemBlock(event.getRegistry(), ASBlocks.blockUraniumOre);
        OreDictionary.registerOre("oreUranium", blockUraniumOre);

        blockChemExtractor = addItemBlock(event.getRegistry(), ASBlocks.blockChemExtractor);
        blockChemBoiler = addItemBlock(event.getRegistry(), ASBlocks.blockChemBoiler);
        blockChemCentrifuge = addItemBlock(event.getRegistry(), ASBlocks.blockChemCentrifuge);

        if (AtomicScience.runningAsDev)
        {
            new CreativeTabCells();
        }
    }

    static ItemBlock addItemBlock(IForgeRegistry<Item> reg, Block block)
    {
        ItemBlock item = new ItemBlock(block);
        item.setRegistryName(block.getRegistryName());
        reg.register(item);
        return item;
    }
}
