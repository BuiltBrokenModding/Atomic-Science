package com.builtbroken.atomic.lib.timer;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/26/2018.
 */
public class TileTimerConditional extends TileTimer
{
    protected Optional<BooleanSupplier> shouldTickFunction;
    protected Optional<BooleanSupplier> shouldResetFunction;

    public TileTimerConditional(int triggerTime, TimeEndFunction function)
    {
        super(triggerTime, function);
    }

    public TileTimerConditional(int triggerTime, IntConsumer consumer)
    {
        super(triggerTime, consumer);
    }

    public static TileTimerConditional newSimple(int triggerTime, IntConsumer consumer)
    {
        return new TileTimerConditional(triggerTime, consumer);
    }

    public void tick()
    {
        if (shouldTickFunction.orElse(() -> true).getAsBoolean())
        {
            super.tick();
        }

        if (shouldResetFunction.orElse(() -> true).getAsBoolean())
        {
            ticks = 0;
        }
    }


    public TileTimerConditional setShouldTickFunction(BooleanSupplier shouldTickFunction)
    {
        this.shouldTickFunction = Optional.of(shouldTickFunction);
        return this;
    }

    public TileTimerConditional setShouldResetFunction(BooleanSupplier shouldResetFunction)
    {
        this.shouldResetFunction = Optional.of(shouldResetFunction);
        return this;
    }

    public TileTimerConditional setTickAndRefreshFunction(BooleanSupplier function)
    {
        this.shouldResetFunction = Optional.of(function);
        this.shouldTickFunction = Optional.of(function);
        return this;
    }

}
