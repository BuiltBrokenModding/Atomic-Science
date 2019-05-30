package com.builtbroken.atomic.config.content;

import net.minecraftforge.common.config.Config;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/24/2019.
 */
public class ConfigAccelerator
{
    @Config.Name("cost_per_magnet")
    @Config.Comment("Energy to consume per magnet")
    @Config.LangKey("config.atomicscience:content.accelerator.magnet.power.title")
    @Config.RangeInt(min = 0, max = Integer.MAX_VALUE / 100)
    public int ENERGY_PER_MAGNET = 100;

    @Config.Name("acceleration_scale")
    @Config.Comment("Acceleration to use for a basic setup, scales with ring size. Default ring is 8 magnets around a tube in a box shape.")
    @Config.LangKey("config.atomicscience:content.accelerator.scale.title")
    public float ACCELERATION_SCALE = 0.005f;

    @Config.Name("acceleration_max")
    @Config.Comment("Largest amount of acceleration a single ring can produce")
    @Config.LangKey("config.atomicscience:content.accelerator.max.title")
    public float ACCELERATION_MAX = 0.1f;


    @Config.Name("fraction_math")
    @Config.Comment("Set to true to use fraction math when possible. In most cases this is faster and more precise. " +
            "However, can be disabled if you notice issues with particle movement.")
    @Config.LangKey("config.atomicscience:content.accelerator.fraction.math.title")
    public boolean FRACTION_MATH = true;
}
