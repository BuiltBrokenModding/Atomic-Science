package com.builtbroken.atomic.lib.timer;

import java.util.function.IntConsumer;

/**
 * Object used to count ticks for a tile or other logic. Uses lambda functions to operate the result of the tick. Allowing
 * code to be reduced at near zero cost of performance.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
public class TickTimer
{
    protected final int triggerTime;
    protected final TimeEndFunction function;

    protected int ticks = 0;


    public TickTimer(int triggerTime, TimeEndFunction function)
    {
        this.triggerTime = triggerTime;
        this.function = function;
    }

    public TickTimer(int triggerTime, IntConsumer consumer)
    {
        this.triggerTime = triggerTime;
        this.function = ticks ->
        {
            consumer.accept(ticks);
            return true;
        };
    }

    public static TickTimer newSimple(int triggerTime, IntConsumer consumer)
    {
        return new TickTimer(triggerTime, consumer);
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
