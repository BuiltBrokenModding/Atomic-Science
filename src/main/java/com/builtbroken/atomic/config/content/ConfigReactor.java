package com.builtbroken.atomic.config.content;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */
@Config(modid = AtomicScience.DOMAIN, name = "content/reactor")
@Config.LangKey("config.atomicscience:content.reactor.title")
public class ConfigReactor
{
    @Config.Name("fuel_rod")
    @Config.Comment("How long the fuel rod runs in ticks (20 ticks a second)")
    @Config.RangeInt(min = 1)
    public static int FUEL_ROD_RUNTIME = 5 * AtomicScience.TICKS_HOUR;

    @Config.Name("breeder_rod")
    @Config.Comment("How long the fuel rod runs in ticks (20 ticks a second)")
    @Config.RangeInt(min = 1)
    public static int BREEDER_ROD_RUNTIME = 2 * AtomicScience.TICKS_HOUR;
}
