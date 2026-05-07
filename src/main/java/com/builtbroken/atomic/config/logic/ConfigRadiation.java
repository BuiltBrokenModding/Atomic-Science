package com.builtbroken.atomic.config.logic;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 * Main config for the radiation system.
 *
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
@Config(modid = AtomicScience.DOMAIN, name = AtomicScience.DOMAIN + "/radiation")
@Config.LangKey("config.atomicscience:radiation.title")
public class ConfigRadiation
{
    //-------------------------------------------------------------------------------
    //--------- Constants used to compute default config values ---------------------
    //-------------------------------------------------------------------------------
    // Value used to convert MeV to 1 RAD (Mev / 624151 * 100)
    // 1 erg = 624151 MeV, 1 rad = 100 erg
    @Config.Ignore
    public static final double MeV_per_RAD = 6.24E7;

    // Mega eV per gram of material
    @Config.Ignore
    public static final float MEV_GRAM_U238 = 4.267f; //https://en.wikipedia.org/wiki/Uranium-238
    @Config.Ignore
    public static final float MEV_GRAM_U235 = 4.679f; //https://en.wikipedia.org/wiki/Uranium-235
    @Config.Ignore
    public static final float MEV_GRAM_U234 = 4.679f;

    // Mass in grams of sample items % of material
    @Config.Ignore
    public static final float MASS_U238_SAMPLE = 500;
    @Config.Ignore
    public static final float MASS_U235_SAMPLE = 500;
    @Config.Ignore
    public static final float MASS_U234_SAMPLE = 500;

    // Activity (number of decays per tick)
    @Config.Ignore
    public static final float ACTIVITY_U238 = 12445; //TODO get actual numbers, then scale
    @Config.Ignore
    public static final float ACTIVITY_U235 = 80011; //TODO get actual numbers, then scale
    @Config.Ignore
    public static final float ACTIVITY_U234 = 80011;

    // Radiation per hour (mass * energy * decay_rate / MeV to rad)
    @Config.Ignore
    public static final float RAD_U234 = (float) (MASS_U234_SAMPLE * MEV_GRAM_U234 * ACTIVITY_U234 / MeV_per_RAD); //alpha radiation
    @Config.Ignore
    public static final float RAD_U235 = (float) (MASS_U235_SAMPLE * MEV_GRAM_U235 * ACTIVITY_U235 / MeV_per_RAD); //alpha radiation
    @Config.Ignore
    public static final float RAD_U238 = (float) (MASS_U238_SAMPLE * MEV_GRAM_U238 * ACTIVITY_U238 / MeV_per_RAD); //alpha radiation
    //-------------------------------------------------------------------------------


    //-------------------------------------------------------------------------------
    // Enable / disable toggles
    //-------------------------------------------------------------------------------

    @Config.Name("enable_exposure")
    @Config.Comment("(true -> on, false -> off) Enables event handling used to apply radiation to entities and update damage effects.")
    @Config.LangKey("config.atomicscience:radiation.enable_exposure")
    public static boolean ENABLE_EXPOSURE = true;

    @Config.Name("enable_map")
    @Config.Comment("(true -> on, false -> off) Enables events used to update the radiation map. If disabled other radiation systems will stop working as well.")
    @Config.LangKey("config.atomicscience:radiation.enable_map")
    public static boolean ENABLE_MAP = true;

    // TODO not yet implemented
    @Config.Name("enable_inventory")
    @Config.Comment("(true -> on, false -> off) Enables tracking of inventories for radiation.")
    @Config.LangKey("config.atomicscience:radiation.enable_inventory")
    public static boolean ENABLE_INVENTORY = true;

    @Config.Name("enable_entity_items")
    @Config.Comment("(true -> on, false -> off) Enables tracking of entity items (dropped items) for radiation.")
    @Config.LangKey("config.atomicscience:radiation.enable_entity_items")
    public static boolean ENABLE_ENTITY_ITEMS = true;

    // TODO not yet implemented
    @Config.Name("enable_entity")
    @Config.Comment("(true -> on, false -> off) Enables tracking of entities for radiation.")
    @Config.LangKey("config.atomicscience:radiation.enable_entity")
    public static boolean ENABLE_ENTITY = true;


    //-------------------------------------------------------------------------------
    // Map settings
    //-------------------------------------------------------------------------------

    @Config.Name("map_update_range")
    @Config.Comment("Max range to update radiation values when a radiation source has changed.")
    @Config.LangKey("config.atomicscience:radiation.map_update_range")
    @Config.RangeInt(min = 10, max = 100)
    public static int MAX_UPDATE_RANGE = 50;

    @Config.Name("material_to_radiation")
    @Config.Comment("Conversion rate of material on the map to radiation values produced. Value is material -> milli-rad.\n"
            + "Keep value low as map is limited to ~2.7 billion. Example (good): 0.01 * 10000 = 100")
    @Config.LangKey("config.atomicscience:radiation.material_to_radiation")
    @Config.RangeDouble(min = 0.0001, max = 100)
    public static float MAP_VALUE_TO_MILI_RAD = 0.01f;


    //-------------------------------------------------------------------------------
    // Entity / player settings
    //-------------------------------------------------------------------------------

    @Config.Name("rad_remove_timer")
    @Config.Comment("Amount of time in ticks (20 ticks a second) to wait before removing radiation.")
    @Config.LangKey("config.atomicscience:radiation.rad_remove_timer")
    @Config.RangeInt(min = 1)
    public static int RAD_REMOVE_TIMER = 5 * 60 * 20; //5 mins

    @Config.Name("rad_remove_percentage")
    @Config.Comment("Percentage of radiation to remove each removal cycle.")
    @Config.LangKey("config.atomicscience:radiation.rad_remove_percentage")
    @Config.RangeDouble(min = 0, max = 1)
    public static float RAD_REMOVE_PERCENTAGE = 0.05f;

    @Config.Name("rad_remove_lower_limit")
    @Config.Comment("Amount that once below, radiation is set to zero.")
    @Config.LangKey("config.atomicscience:radiation.rad_remove_lower_limit")
    @Config.RangeDouble(min = 0, max = 100000)
    public static float RAD_REMOVE_LOWER_LIMIT = 1f;

    @Config.Name("death_radiation_point")
    @Config.Comment("Amount of rads required to kill the player.")
    @Config.LangKey("config.atomicscience:radiation.death_radiation_point")
    @Config.RangeDouble(min = 1)
    public static float RADIATION_DEATH_POINT = 10000;

    @Config.Name("sickness_radiation_point")
    @Config.Comment("Amount of rads required to start causing radiation sickness.")
    @Config.LangKey("config.atomicscience:radiation.sickness_radiation_point")
    @Config.RangeDouble(min = 1)
    public static float RADIATION_SICKNESS_POINT = 1000;

    @Config.Name("weakness_radiation_point")
    @Config.Comment("Amount of rads required to start causing weakness.")
    @Config.LangKey("config.atomicscience:radiation.weakness_radiation_point")
    @Config.RangeDouble(min = 1)
    public static float RADIATION_WEAKNESS_POINT = 5000;

    @Config.Name("confusion_radiation_point")
    @Config.Comment("Amount of rads required to start causing confusion.")
    @Config.LangKey("config.atomicscience:radiation.confusion_radiation_point")
    @Config.RangeDouble(min = 1)
    public static float RADIATION_CONFUSION_POINT = 8000;


    //-------------------------------------------------------------------------------
    // TODO not yet implemented
    //-------------------------------------------------------------------------------
    // Relative biological effectiveness (RBE)
    // Scale value to convert RADs to REMs
    // https://community.dur.ac.uk/ian.terry/teaching/nplab/dose_test.htm
    //-------------------------------------------------------------------------------

    @Config.Name("rbe_xray")
    @Config.Comment("Relative biological effectiveness for X-ray radiation (passes through entity).")
    @Config.LangKey("config.atomicscience:radiation.rbe_xray")
    @Config.RangeDouble(min = 0)
    public static float RBE_XRAY_RADIATION = 1;

    @Config.Name("rbe_gamma")
    @Config.Comment("Relative biological effectiveness for gamma radiation (passes through entity).")
    @Config.LangKey("config.atomicscience:radiation.rbe_gamma")
    @Config.RangeDouble(min = 0)
    public static float RBE_GAMMA_RADIATION = 1;

    @Config.Name("rbe_neutrons_fast")
    @Config.Comment("Relative biological effectiveness for fast neutron radiation (passes through entity).")
    @Config.LangKey("config.atomicscience:radiation.rbe_neutrons_fast")
    @Config.RangeDouble(min = 0)
    public static float RBE_NEURTONS_FAST = 10;

    @Config.Name("rbe_neutrons_slow")
    @Config.Comment("Relative biological effectiveness for slow neutron radiation (passes through entity).")
    @Config.LangKey("config.atomicscience:radiation.rbe_neutrons_slow")
    @Config.RangeDouble(min = 0)
    public static float RBE_NEURTONS_SLOW = 5;

    @Config.Name("rbe_alpha")
    @Config.Comment("Relative biological effectiveness for alpha radiation (stopped by anything).")
    @Config.LangKey("config.atomicscience:radiation.rbe_alpha")
    @Config.RangeDouble(min = 0)
    public static float RBE_ALPHA_RADIATION = 20;

    @Config.Name("rbe_beta")
    @Config.Comment("Relative biological effectiveness for beta radiation (stopped by entity).")
    @Config.LangKey("config.atomicscience:radiation.rbe_beta")
    @Config.RangeDouble(min = 0)
    public static float RBE_BETA_RADIATION = 10;


    //-------------------------------------------------------------------------------
    // Block radiation decay
    //-------------------------------------------------------------------------------

    @Config.Name("decay_per_block")
    @Config.Comment("Fraction of radiation absorbed per default material block.")
    @Config.LangKey("config.atomicscience:radiation.decay_per_block")
    @Config.RangeDouble(min = 0, max = 1)
    public static float RADIATION_DECAY_PER_BLOCK = 0.05f;

    @Config.Name("decay_per_fluid")
    @Config.Comment("Fraction of radiation absorbed per fluid block.")
    @Config.LangKey("config.atomicscience:radiation.decay_per_fluid")
    @Config.RangeDouble(min = 0, max = 1)
    public static float RADIATION_DECAY_PER_FLUID = 0.15f;

    @Config.Name("decay_metal")
    @Config.Comment("Fraction of radiation absorbed by metal blocks.")
    @Config.LangKey("config.atomicscience:radiation.decay_metal")
    @Config.RangeDouble(min = 0, max = 1)
    public static float RADIATION_DECAY_METAL = 0.50f;

    @Config.Name("decay_stone")
    @Config.Comment("Fraction of radiation absorbed by stone blocks.")
    @Config.LangKey("config.atomicscience:radiation.decay_stone")
    @Config.RangeDouble(min = 0, max = 1)
    public static float RADIATION_DECAY_STONE = 0.20f;


    //-------------------------------------------------------------------------------
    // Radioactive material values
    // Amount of radioactive material present inside each source, used to calculate emitted radiation.
    //-------------------------------------------------------------------------------

    @Config.Name("mat_u234")
    @Config.Comment("Radiation material value for a U234 pellet.")
    @Config.LangKey("config.atomicscience:radiation.mat_u234")
    @Config.RangeInt(min = 1)
    public static int RADIOACTIVE_MAT_VALUE_U234 = (int) Math.ceil(RAD_U235 / MAP_VALUE_TO_MILI_RAD);

    @Config.Name("mat_u235")
    @Config.Comment("Radiation material value for a U235 pellet.")
    @Config.LangKey("config.atomicscience:radiation.mat_u235")
    @Config.RangeInt(min = 1)
    public static int RADIOACTIVE_MAT_VALUE_U235 = (int) Math.ceil(RAD_U235 / MAP_VALUE_TO_MILI_RAD);

    @Config.Name("mat_u238")
    @Config.Comment("Radiation material value for a U238 pellet.")
    @Config.LangKey("config.atomicscience:radiation.mat_u238")
    @Config.RangeInt(min = 1)
    public static int RADIOACTIVE_MAT_VALUE_U238 = (int) Math.ceil(RAD_U238 / MAP_VALUE_TO_MILI_RAD);

    @Config.Name("mat_yellowcake")
    @Config.Comment("Radiation material value for yellowcake.")
    @Config.LangKey("config.atomicscience:radiation.mat_yellowcake")
    @Config.RangeInt(min = 1)
    public static int RADIOACTIVE_MAT_VALUE_YELLOW_CAKE = RADIOACTIVE_MAT_VALUE_U235 / 10;

    @Config.Name("mat_fuel_rod")
    @Config.Comment("Radiation material value for a fission fuel rod.")
    @Config.LangKey("config.atomicscience:radiation.mat_fuel_rod")
    @Config.RangeInt(min = 1)
    public static int RADIOACTIVE_MAT_VALUE_FUEL_ROD = RADIOACTIVE_MAT_VALUE_U235 * 100;

    @Config.Name("mat_breeder_rod")
    @Config.Comment("Radiation material value for a fission breeder rod.")
    @Config.LangKey("config.atomicscience:radiation.mat_breeder_rod")
    @Config.RangeInt(min = 1)
    public static int RADIOACTIVE_MAT_VALUE_BREEDER_ROD = RADIOACTIVE_MAT_VALUE_FUEL_ROD / 10;

    @Config.Name("reactor_fuel_rod")
    @Config.Comment("Radiation value for a fuel rod inside a reactor.")
    @Config.LangKey("config.atomicscience:radiation.reactor_fuel_rod")
    @Config.RangeInt(min = 1)
    public static int RADIOACTIVE_REACTOR_VALUE_FUEL_ROD = RADIOACTIVE_MAT_VALUE_FUEL_ROD * 100;

    @Config.Name("reactor_breeder_rod")
    @Config.Comment("Radiation value for a breeder rod inside a reactor.")
    @Config.LangKey("config.atomicscience:radiation.reactor_breeder_rod")
    @Config.RangeInt(min = 1)
    public static int RADIOACTIVE_REACTOR_VALUE_BREEDER_ROD = RADIOACTIVE_MAT_VALUE_BREEDER_ROD * 100;
}
