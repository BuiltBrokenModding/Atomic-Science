package com.builtbroken.atomic.config.content;

import net.minecraftforge.common.config.Config;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */
public class ConfigPowerUsage
{
    @Config.Name("enable_power")
    @Config.Comment("Enables power usage for machines")
    @Config.LangKey("config.atomicscience:content.power.usage.enable.title")
    public boolean ENABLE = true;

    @Config.Name("power_boiler")
    @Config.Comment("Power FE (Forge Energy) used per ticks (20 ticks a second)")
    @Config.LangKey("config.atomicscience:content.power.usage.boiler.title")
    @Config.RangeInt(min = 1)
    public int BOILER = 100;

    @Config.Name("power_extractor")
    @Config.Comment("Power FE (Forge Energy) used per ticks (20 ticks a second)")
    @Config.LangKey("config.atomicscience:content.power.usage.extractor.title")
    @Config.RangeInt(min = 1)
    public int EXTRACTOR = 100;

    @Config.Name("power_centrifuge")
    @Config.Comment("Power FE (Forge Energy) used per ticks (20 ticks a second)")
    @Config.LangKey("config.atomicscience:content.power.usage.centrifuge.title")
    @Config.RangeInt(min = 1)
    public int CENTRIFUGE = 100;

    @Config.Name("power_particle_capture_cost")
    @Config.Comment("Power FE (Forge Energy) used to capture a created particle")
    @Config.LangKey("config.atomicscience:content.power.usage.particle.capture.title")
    @Config.RangeInt(min = 1)
    public int PARTICLE_CAPTURE_COST = 10000;

    @Config.Name("power_particle_contain_cost")
    @Config.Comment("Power FE (Forge Energy) used to hold a particle")
    @Config.LangKey("config.atomicscience:content.power.usage.particle.contain.title")
    @Config.RangeInt(min = 1)
    public int PARTICLE_CONTAINMENT_COST = 100;
}
