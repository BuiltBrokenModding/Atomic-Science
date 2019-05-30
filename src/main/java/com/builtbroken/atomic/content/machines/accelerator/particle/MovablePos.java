package com.builtbroken.atomic.content.machines.accelerator.particle;

import com.builtbroken.jlib.data.vector.IPos3D;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-29.
 */
public class MovablePos implements IMovablePos
{
    private float x;
    private float y;
    private float z;

    @Override
    public void move(double x, double y, double z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        normalizePosition();
    }

    @Override
    public void set(double x, double y, double z)
    {
        this.x = (float)x;
        this.y = (float)y;
        this.z = (float)z;
        normalizePosition();
    }

    public void normalizePosition()
    {
        x = normalize(x);
        y = normalize(y);
        z = normalize(z);
    }

    protected float normalize(float v)
    {
        return Math.round(v * getPrecision()) / getPrecision();
    }

    public float getPrecision()
    {
        return 1000f;
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
