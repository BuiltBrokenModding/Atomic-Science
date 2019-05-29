package com.builtbroken.atomic.content.machines.accelerator.particle;

import com.builtbroken.jlib.data.vector.IPos3D;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-29.
 */
public interface IMovablePos extends IPos3D
{
    void move(double x, double y, double z);

    void set(double x, double y, double z);

    default void set(BlockPos pos)
    {
        set(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }
}
