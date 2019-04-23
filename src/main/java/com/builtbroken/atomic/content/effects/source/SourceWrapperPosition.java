package com.builtbroken.atomic.content.effects.source;

import com.builtbroken.atomic.api.effect.IIndirectEffectSource;
import net.minecraft.world.World;

/**
 * Wrappers a radiation source location
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 4/29/2018.
 */
public class SourceWrapperPosition implements IIndirectEffectSource
{
    public final World worldObj;
    public final double x;
    public final double y;
    public final double z;

    public SourceWrapperPosition(World worldObj, double x, double y, double z)
    {
        this.worldObj = worldObj;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public World world()
    {
        return worldObj;
    }

    @Override
    public double z()
    {
        return z;
    }

    @Override
    public double x()
    {
        return x;
    }

    @Override
    public double y()
    {
        return y;
    }
}
