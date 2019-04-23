package com.builtbroken.atomic.config.content;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */
public class ConfigReactor
{
    @Config.Name("fuel_rod")
    @Config.Comment("How long the fuel rod runs in ticks (20 ticks a second)")
    @Config.LangKey("config.atomicscience:content.reactor.fuel.rod.time.title")
    @Config.RangeInt(min = 1)
    public int FUEL_ROD_RUNTIME = 5 * AtomicScience.TICKS_HOUR;

    @Config.Name("breeder_rod")
    @Config.Comment("How long the fuel rod runs in ticks (20 ticks a second)")
    @Config.LangKey("config.atomicscience:content.reactor.breeder.rod.time.title")
    @Config.RangeInt(min = 1)
    public int BREEDER_ROD_RUNTIME = 2 * AtomicScience.TICKS_HOUR;

    //To boil 1 block of water takes 1,562,379.05 KJ
    /** Heat output from fuel rod when active in a reactor */
    @Config.Name("fuel_rod_heat")
    @Config.Comment("How much heat the fuel rod produces")
    @Config.LangKey("config.atomicscience:content.reactor.fuel.rod.heat.title")
    @Config.RangeInt(min = 1)
    public int HEAT_REACTOR_FUEL_ROD = 1562379 * 20;

    /** Heat output from fuel rod when active in a reactor */
    @Config.Name("breeder_rod_heat")
    @Config.Comment("How much heat the fuel rod produces")
    @Config.LangKey("config.atomicscience:content.reactor.breeder.rod.heat.title")
    @Config.RangeInt(min = 1)
    public int HEAT_REACTOR_BREEDER_ROD = 1562379 * 10;
}
