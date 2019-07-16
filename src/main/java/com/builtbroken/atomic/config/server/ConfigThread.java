package com.builtbroken.atomic.config.server;

import net.minecraftforge.common.config.Config;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2/10/2019.
 */
public class ConfigThread
{
    //@Config.Name("thread_count")
    //@Config.Comment("Number of worker threads to run to handle blast calculations. " +
            //"Try to only match 50% of the number of cores your machine can support. " +
            //"Otherwise the main game thread will slow down while the workers are processing. " +
            //"Which is counter to the reason threads exist.")
    //@Config.RangeInt(min = 1, max = 8)
    public static int THREAD_COUNT = 1;

    @Config.Name("thread_pathing_range_heat")
    @Config.Comment("Range in blocks from the start position that the thread can path before force stopping")
    @Config.LangKey("config.atomicscience:server.thread.pathing.heat.title")
    public static int THREAD_HEAT_PATHING_RANGE = 10;

    @Config.Name("thread_pathing_range_rads")
    @Config.Comment("Range in blocks from the start position that the thread can path before force stopping")
    @Config.LangKey("config.atomicscience:server.thread.pathing.rads.title")
    public static int THREAD_RADS_PATHING_RANGE = 50;
}
