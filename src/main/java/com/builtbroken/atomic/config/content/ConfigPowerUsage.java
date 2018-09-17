package com.builtbroken.atomic.config.content;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */
@Config(modid = AtomicScience.DOMAIN, name = "content/power_usage")
@Config.LangKey("config.atomicscience:content.power.usage.title")
public class ConfigPowerUsage
{
    @Config.Name("enable_power")
    @Config.Comment("Enables power usage for machines")
    public static boolean ENABLE_POWER_USAGE = true;

    @Config.Name("power_boiler")
    @Config.Comment("Power FE (Forge Energy) used per ticks (20 ticks a second)")
    @Config.RangeInt(min = 1)
    public static int POWER_USAGE_BOILER = 100;

    @Config.Name("power_extractor")
    @Config.Comment("Power FE (Forge Energy) used per ticks (20 ticks a second)")
    @Config.RangeInt(min = 1)
    public static int POWER_USAGE_EXTRACTOR = 100;

    @Config.Name("power_centrifuge")
    @Config.Comment("Power FE (Forge Energy) used per ticks (20 ticks a second)")
    @Config.RangeInt(min = 1)
    public static int POWER_USAGE_CENTRIFUGE = 100;
}
