package com.builtbroken.atomic.config.content;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/18/2018.
 */
@Config(modid = AtomicScience.DOMAIN, name = AtomicScience.DOMAIN + "/content")
@Config.LangKey("config.atomicscience:content.title")
public class ConfigContent
{
    @Config.Name("uranium_ore")
    @Config.LangKey("config.atomicscience:content.ore.uranium.title")
    public static final ConfigUraniumOre URANIUM_ORE = new ConfigUraniumOre();

    @Config.Name("power_usage")
    @Config.LangKey("config.atomicscience:content.power.usage.title")
    public static final ConfigPowerUsage POWER_USAGE = new ConfigPowerUsage();

    @Config.Name("reactor")
    @Config.LangKey("config.atomicscience:content.reactor.title")
    public static final ConfigReactor REACTOR = new ConfigReactor();

    @Config.Name("laser")
    @Config.LangKey("config.atomicscience:content.laser.title")
    public static final ConfigLaser LASER = new ConfigLaser();

    @Config.Name("accelerator")
    @Config.LangKey("config.atomicscience:content.accelerator.title")
    public static final ConfigAccelerator ACCELERATOR = new ConfigAccelerator();
}
