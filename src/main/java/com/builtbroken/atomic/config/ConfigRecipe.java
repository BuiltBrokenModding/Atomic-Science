package com.builtbroken.atomic.config;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.proxy.ContentProxy;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public class ConfigRecipe extends ContentProxy
{
    //-----------------------------------
    //-----Extractor
    //-----------------------------------
    public static int WATER_USED_YELLOW_CAKE = 1000;
    public static int LIQUID_WASTE_PRODUCED_YELLOW_CAKE = 1000;
    public static int YELLOW_CAKE_PER_ORE = 3;

    //-----------------------------------
    //-----Boiler
    //-----------------------------------
    public static int WATER_BOIL_YELLOWCAKE = 1000;
    public static int WATER_BOIL_URANIUM_ORE = 1000;

    public static int HEX_OUT_YELLOWCAKE = 250;
    public static int HEX_OUT_URANIUM_ORE = 600;

    public static int CON_WATER_YELLOWCAKE = 1000;
    public static int CON_WATER_URANIUM_ORE = 1000;

    public static int LIQUID_WASTE_PRODUCED_TO_WATER = 1;
    public static int LIQUID_WASTE_CONSUMED_PER_BOIL = 1000;
    public static int LIQUID_WASTE_SOLID_WASTE = 1;

    public static int SOLID_WASTE_YELLOWCAKE = 1; //TODO maybe make a % of an item, with progress bar to full item
    public static int SOLID_WASTE_URANIUM_ORE = 3; //TODO maybe make a % of an item, with progress bar to full item

    //-----------------------------------
    //-----Centrifuge
    //-----------------------------------
    public static int URANIUM_HEX_PER_CENTRIFUGE = 200;
    public static int MINERAL_WASTE_WATER_PER_CENTRIFUGE = 1000;
    public static int MINERAL_WASTE_WATER_PER_WATER = 1;
    public static int SOLID_WASTE_PER_CENTRIFUGE = 1;

    public ConfigRecipe()
    {
        super("config.recipe");
    }

    @Override
    public void preInit()
    {
        Configuration configuration = new Configuration(new File(AtomicScience.configFolder, "Recipes.cfg"), AtomicScience.VERSION);
        configuration.load();

        final String cat_extractor = "extractor";
        WATER_USED_YELLOW_CAKE = configuration.getInt("water_used_yellowcake", cat_extractor, WATER_USED_YELLOW_CAKE, 0, 10000,
                "Amount of water (mb) used when extracting yellowcake");
        LIQUID_WASTE_PRODUCED_YELLOW_CAKE = configuration.getInt("liquid_waste_produced_yellowcake", cat_extractor, LIQUID_WASTE_PRODUCED_YELLOW_CAKE, 0, 10000,
                "Amount of mineral waste (mb) produced when processing uranium ore into yellowcake");
        YELLOW_CAKE_PER_ORE = configuration.getInt("uranium_ore_to_yellowcake", cat_extractor, YELLOW_CAKE_PER_ORE, 1, 64,
                "Number of items to output per uranium ore processed");

        configuration.save();
    }
}
