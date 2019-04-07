package com.builtbroken.atomic.lib.timer;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

/**
 * Version of {@link TickTimer} that provides a way to disable tick or reset based on conditions
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/26/2018.
 */
public class TickTimerConditional extends TickTimer
{
    protected Optional<BooleanSupplier> shouldTickFunction;
    protected Optional<BooleanSupplier> shouldResetFunction;

    public TickTimerConditional(int triggerTime, TimeEndFunction function)
    {
        super(triggerTime, function);
    }

    public TickTimerConditional(IntSupplier triggerTime, TimeEndFunction function)
    {
        super(triggerTime, function);
    }

    public TickTimerConditional(int triggerTime, IntConsumer consumer)
    {
        super(triggerTime, consumer);
    }

    public TickTimerConditional(IntSupplier triggerTime, IntConsumer consumer)
    {
        super(triggerTime, consumer);
    }

    public static TickTimerConditional newSimple(int triggerTime, IntConsumer consumer)
    {
        return new TickTimerConditional(triggerTime, consumer);
    }

    public static TickTimerConditional newSimple(IntSupplier triggerTime, IntConsumer consumer)
    {
        return new TickTimerConditional(triggerTime, consumer);
    }

    public static TickTimerConditional newTrigger(IntSupplier triggerTime, IntConsumer consumer, BooleanSupplier triggerCondition)
    {
        return new TickTimerConditional(triggerTime, consumer).setShouldTickFunction(triggerCondition).setShouldResetFunction(() -> !triggerCondition.getAsBoolean());
    }

    public static TickTimerConditional newTrigger(int triggerTime, IntConsumer consumer, BooleanSupplier triggerCondition)
    {
        return new TickTimerConditional(triggerTime, consumer).setShouldTickFunction(triggerCondition).setShouldResetFunction(() -> !triggerCondition.getAsBoolean());
    }

    public void tick()
    {
        if (shouldTickFunction == null || shouldTickFunction.orElse(() -> true).getAsBoolean())
        {
            super.tick();
        }

        if (shouldResetFunction != null && shouldResetFunction.orElse(() -> true).getAsBoolean())
        {
            ticks = 0;
        }
    }


    public TickTimerConditional setShouldTickFunction(BooleanSupplier shouldTickFunction)
    {
        this.shouldTickFunction = Optional.of(shouldTickFunction);
        return this;
    }

    public TickTimerConditional setShouldResetFunction(BooleanSupplier shouldResetFunction)
    {
        this.shouldResetFunction = Optional.of(shouldResetFunction);
        return this;
    }

    public TickTimerConditional setTickAndRefreshFunction(BooleanSupplier function)
    {
        this.shouldResetFunction = Optional.of(function);
        this.shouldTickFunction = Optional.of(function);
        return this;
    }

}
