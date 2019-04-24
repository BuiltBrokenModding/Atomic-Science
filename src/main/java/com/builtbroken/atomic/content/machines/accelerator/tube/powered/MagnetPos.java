package com.builtbroken.atomic.content.machines.accelerator.tube.powered;

import com.builtbroken.atomic.map.data.DataPool;
import net.minecraft.util.math.BlockPos;

/**
 * Used to track a position of a magnet in the world with
 * its last reported power level.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-24.
 */
public class MagnetPos
{

    private static DataPool<MagnetPos> objectPool = new DataPool(1000); //TODO config

    private BlockPos pos;
    private float power;

    MagnetPos(BlockPos pos, float power)
    {
        this.pos = pos;
        this.power = power;
    }

    public float power()
    {
        return power;
    }

    public BlockPos pos()
    {
        return pos;
    }
}
