package com.builtbroken.atomic.config.thermal;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */
@Config(modid = AtomicScience.DOMAIN, name = "thermal/steam")
@Config.LangKey("config.atomicscience:power.title")
public class ConfigSteam
{

    /** How much power to create per mb of steam flow */

    @Config.Name("steam_to_power_ratio")
    @Config.Comment("Ratio of milli-buckets of steam (1000 to a bucket) to amount of power in watts to generate. Normally a single source of water can produce 200-400mb of steam every tick.")
    @Config.RangeInt(min = 1)
    public static int STEAM_TO_ENERGY = 4;

    /** mb of steam produced per tick per heat % */
    @Config.Name("vapor_rate_water")
    @Config.Comment("Default amount of vapor a still water source can produce when heated. Scales with temperature but works as a lower limit.")
    @Config.RangeInt(min = 1)
    public static int WATER_VAPOR_RATE = 120;

    /** mb of steam produced per tick per heat % */
    @Config.Name("vapor_rate_water_flowing")
    @Config.Comment("Default amount of vapor a flowing water source can produce when heated. Scales with temperature but works as a lower limit.")
    @Config.RangeInt(min = 1)
    public static int WATER_FLOWING_VAPOR_RATE = 40;

    /** Max amount of steam that can be produced per tick from a water source */
    @Config.Name("max_vapor_rate_water")
    @Config.Comment("Max limit a single still wait source can produce in terms of vapor (steam) per tick.")
    @Config.RangeInt(min = 10)
    public static int WATER_VAPOR_MAX_RATE = 1000;
}
