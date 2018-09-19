package com.builtbroken.atomic.config.content;

import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */
public class ConfigPowerUsage
{
    @Config.Name("enable_power")
    @Config.Comment("Enables power usage for machines")
    public boolean ENABLE = true;

    @Config.Name("power_boiler")
    @Config.Comment("Power FE (Forge Energy) used per ticks (20 ticks a second)")
    @Config.RangeInt(min = 1)
    public int BOILER = 100;

    @Config.Name("power_extractor")
    @Config.Comment("Power FE (Forge Energy) used per ticks (20 ticks a second)")
    @Config.RangeInt(min = 1)
    public int EXTRACTOR = 100;

    @Config.Name("power_centrifuge")
    @Config.Comment("Power FE (Forge Energy) used per ticks (20 ticks a second)")
    @Config.RangeInt(min = 1)
    public int CENTRIFUGE = 100;
}
