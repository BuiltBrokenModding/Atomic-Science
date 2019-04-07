package com.builtbroken.atomic.config.content;

import net.minecraftforge.common.config.Config;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/24/2019.
 */
public class ConfigLaser
{
    @Config.Name("max_boosters")
    @Config.Comment("Largest number of boosters that can be placed behind the laser emitter")
    @Config.LangKey("config.atomicscience:content.laser.booster.count.title")
    @Config.RangeInt(min = 1, max = 40)
    public int BOOSTER_MAX = 10;

    @Config.Name("fire_cost")
    @Config.Comment("Amount of energy to consume to fire the laser in a single burst per booster")
    @Config.LangKey("config.atomicscience:content.laser.fire.cost.title")
    @Config.RangeInt(min = 0, max = (Integer.MAX_VALUE / 2))
    public int FIRING_COST = 100000;

    @Config.Name("booster_energy_buffer")
    @Config.Comment("Amount of energy to store per booster")
    @Config.LangKey("config.atomicscience:content.laser.booster.buffer.title")
    @Config.RangeInt(min = 0, max = (Integer.MAX_VALUE / 2))
    public int ENERGY_PER_BOOSTER = 100000;

    @Config.Name("cooldown")
    @Config.Comment("Time to wait between firing the laser")
    @Config.LangKey("config.atomicscience:content.laser.cooldown.title")
    @Config.RangeInt(min = 0, max = Short.MAX_VALUE)
    public int LASER_COOLDOWN = 20 * 60 * 2; //2 mins

    @Config.Name("firing_delay")
    @Config.Comment("Time to wait before firing a laser when triggered to fire")
    @Config.LangKey("config.atomicscience:content.laser.firing.delay.title")
    @Config.RangeInt(min = 0, max = Short.MAX_VALUE)
    public int LASER_FIRING_DELAY = 10; //half a second
}
