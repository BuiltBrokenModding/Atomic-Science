package com.builtbroken.atomic.lib.timer;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/6/2019.
 */
public interface ITickTimer<H extends Object>
{
    /**
     * Called each tick of the host machine, item, or entity
     *
     * @param host       - optional, host of the timer
     * @param systemTick - tick of the machine, item, or entity
     */
    void tick(H host, int systemTick);
}
