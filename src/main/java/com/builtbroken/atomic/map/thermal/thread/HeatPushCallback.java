package com.builtbroken.atomic.map.thermal.thread;

import com.builtbroken.atomic.map.data.DataPos;

/**
 * Created by Dark(DarkGuardsman, Robert) on 7/7/2019.
 */
@FunctionalInterface
public interface HeatPushCallback
{
    void pushHeat(DataPos pos, int heat);
}
