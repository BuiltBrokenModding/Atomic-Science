package com.builtbroken.atomic.lib.timer;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

/**
 * Object used to count ticks for a tile or other logic. Uses lambda functions to operate the result of the tick. Allowing
 * code to be reduced at near zero cost of performance.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
public class TickTimer implements ITickTimer
{
    protected final IntSupplier triggerTime;
    protected final TimeEndFunction function;

    protected int ticks = 0;

    protected TickTimer(IntSupplier triggerTime, TimeEndFunction function)
    {
        this.triggerTime = triggerTime;
        this.function = function;
    }

    protected TickTimer(int triggerTime, TimeEndFunction function)
    {
        this(() -> triggerTime, function);
    }

    protected TickTimer(int triggerTime, IntConsumer consumer)
    {
        this.triggerTime = () -> triggerTime;
        this.function = ticks ->
        {
            consumer.accept(ticks);
            return true;
        };
    }

    protected TickTimer(IntSupplier triggerTime, IntConsumer consumer)
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
        if (ticks % triggerTime.getAsInt() == 0)
        {
            if (function.trigger(ticks) || ticks >= Integer.MAX_VALUE - 2)
            {
                ticks = 0;
            }
        }
    }

    @Override
    public void tick(Object host, int systemTick)
    {
        tick();
    }

}
