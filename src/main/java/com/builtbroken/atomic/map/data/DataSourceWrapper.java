package com.builtbroken.atomic.map.data;

import com.builtbroken.atomic.lib.transform.IPosWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * Used to wrapper sources in order to track changes between ticks
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public abstract class DataSourceWrapper<S extends IPosWorld> implements IPosWorld
{
    public final S source;

    public int dim;
    public double x;
    public double y;
    public double z;

    public DataSourceWrapper(S source)
    {
        this.source = source;
    }

    public boolean hasSourceChanged()
    {
        return dim != source.world().provider.dimensionId
                || hasDistanceChanged();
    }

    protected boolean hasDistanceChanged()
    {
        return distanceChangeEnough(x, source.x())
                || distanceChangeEnough(y, source.y())
                || distanceChangeEnough(z, source.z());
    }

    private boolean distanceChangeEnough(double prev, double current)
    {
        double delta = prev - current;
        return delta > 0.1 || delta < -0.1;
    }

    public void logCurrentData()
    {
        dim = source.world().provider.dimensionId;

        //Only want to log distance changes if it has changed enough to be noticed
        //  This is designed to prevent slow creep from going unnoticed
        if (hasDistanceChanged())
        {
            x = source.x();
            y = source.y();
            z = source.z();
        }
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

    @Override
    public World world()
    {
        return DimensionManager.getWorld(dim) != null ? DimensionManager.getWorld(dim) : source.world();
    }
}
