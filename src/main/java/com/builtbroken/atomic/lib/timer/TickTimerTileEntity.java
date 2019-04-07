package com.builtbroken.atomic.lib.timer;

import net.minecraft.tileentity.TileEntity;

import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;

/**
 * Timer that uses the TileEntity's timer to save on needing a tick integer per task.
 * Created by Dark(DarkGuardsman, Robert) on 4/6/2019.
 */
public class TickTimerTileEntity implements ITickTimer<TileEntity>
{
    protected final int triggerTime;
    protected final IntConsumer function;
    protected final BooleanSupplier shouldRun;

    protected TickTimerTileEntity(int triggerTime, IntConsumer consumer, BooleanSupplier shouldRun)
    {
        this.triggerTime = triggerTime;
        this.function = consumer;
        this.shouldRun = shouldRun;
    }

    public static TickTimerTileEntity newSimple(int triggerTime, IntConsumer consumer)
    {
        return new TickTimerTileEntity(triggerTime, consumer, () -> true);
    }

    public static TickTimerTileEntity newConditional(int triggerTime, IntConsumer consumer, BooleanSupplier shouldRun)
    {
        return new TickTimerTileEntity(triggerTime, consumer, shouldRun);
    }

    @Override
    public void tick(TileEntity host, int systemTick)
    {
        if (shouldRun.getAsBoolean() && systemTick % triggerTime == 0)
        {
            function.accept(systemTick);
        }
    }
}
