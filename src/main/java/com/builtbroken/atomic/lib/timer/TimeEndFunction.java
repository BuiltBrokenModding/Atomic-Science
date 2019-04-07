package com.builtbroken.atomic.lib.timer;

/**
 * Simple int based apply function
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
@FunctionalInterface
public interface TimeEndFunction
{

    /**
     * Called to trigger the timer's end function
     *
     * @param ticks - current tick
     * @return true to reset the timer, not all timers support resetting
     */
    boolean trigger(int ticks);
}
