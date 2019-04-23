package com.builtbroken.atomic.config.logic;

import net.minecraftforge.common.config.Config;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */

public class ConfigSteam
{

    /** How much power to create per mb of steam flow */

    @Config.Name("steam_to_power_ratio")
    @Config.Comment("Ratio of milli-buckets of steam (1000 to a bucket) to amount of power in watts to generate. Normally a single source of water can produce 200-400mb of steam every tick.")
    @Config.LangKey("config.atomicscience:thermal.steam.energy.title")
    @Config.RangeInt(min = 1)
    public int STEAM_TO_ENERGY = 4;

    /** mb of steam produced per tick per heat % */
    @Config.Name("vapor_rate_water")
    @Config.Comment("Default amount of vapor a still water source can produce when heated. Scales with temperature but works as a lower limit.")
    @Config.LangKey("config.atomicscience:thermal.steam.water.vapor.title")
    @Config.RangeInt(min = 1)
    public int WATER_VAPOR_RATE = 120;

    /** mb of steam produced per tick per heat % */
    @Config.Name("vapor_rate_water_flowing")
    @Config.Comment("Default amount of vapor a flowing water source can produce when heated. Scales with temperature but works as a lower limit.")
    @Config.LangKey("config.atomicscience:thermal.steam.water.flowing.vapor.title")
    @Config.RangeInt(min = 1)
    public int WATER_FLOWING_VAPOR_RATE = 40;

    /** Max amount of steam that can be produced per tick from a water source */
    @Config.Name("max_vapor_rate_water")
    @Config.Comment("Max limit a single still wait source can produce in terms of vapor (steam) per tick.")
    @Config.LangKey("config.atomicscience:thermal.steam.water.max.vapor.title")
    @Config.RangeInt(min = 10)
    public int WATER_VAPOR_MAX_RATE = 1000;
}
