package com.builtbroken.atomic.map.thermal.thread;

/**
 * Created by Dark(DarkGuardsman, Robert) on 7/7/2019.
 */
@FunctionalInterface
public interface HeatPushCallback
{
    void pushHeat(int x, int y, int z, int heat);
}
