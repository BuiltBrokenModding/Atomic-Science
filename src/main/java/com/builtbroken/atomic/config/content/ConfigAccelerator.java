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
}
