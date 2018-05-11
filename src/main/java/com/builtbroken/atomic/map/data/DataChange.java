package com.builtbroken.atomic.map.data;

import com.builtbroken.jlib.data.vector.IPos3D;

/**
 * Used to store data for thread
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/28/2018.
 */
public class DataChange implements IPos3D //TODO make mutable and use object pool to save ram
{
    public final int dim;
    public final double x;
    public final double y;
    public final double z;

    public final int old_value;
    public final int new_value;

    public DataChange(int dim, double x, double y, double z, int old_value, int new_value)
    {
        this.dim = dim;
        this.x = x;
        this.y = y;
        this.z = z;
        this.old_value = old_value;
        this.new_value = new_value;
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
