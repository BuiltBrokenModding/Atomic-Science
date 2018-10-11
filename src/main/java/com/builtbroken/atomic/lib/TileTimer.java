package com.builtbroken.atomic.lib;

import java.util.function.IntConsumer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
public class TileTimer
{
    private final int triggerTime;
    private final TimeEndFunction function;

    private int ticks = 0;


    public TileTimer(int triggerTime, TimeEndFunction function)
    {
        this.triggerTime = triggerTime;
        this.function = function;
    }

    public TileTimer(int triggerTime, IntConsumer consumer)
    {
        this.triggerTime = triggerTime;
        this.function = ticks ->
        {
            consumer.accept(ticks);
            return true;
        };
    }

    public void tick()
    {
        ticks++;
        if (ticks % triggerTime == 0)
        {
            if (function.apply(ticks) || ticks >= Integer.MAX_VALUE - 2)
            {
                ticks = 0;
            }
        }
    }

    @FunctionalInterface
    public interface TimeEndFunction
    {

        /**
         * Applies this function to the given argument.
         *
         * @param value the function argument
         * @return the function result
         */
        boolean apply(int value);
    }

}
