package com.builtbroken.atomic.map.exposure;

import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.jlib.data.vector.IPos3D;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public class RadSourceWrapper implements IPos3D
{
    public final IRadiationSource source;

    public int radioactiveMaterialValue;

    public int dim;
    public double x;
    public double y;
    public double z;

    public RadSourceWrapper(IRadiationSource source)
    {
        this.source = source;
    }

    public boolean hasSourceChanged()
    {
        return dim != source.world().provider.dimensionId
                || distanceChangeEnough(x, source.x())
                || distanceChangeEnough(y, source.y())
                || distanceChangeEnough(z, source.z())
                || radioactiveMaterialValue != source.getRadioactiveMaterial();
    }

    private boolean distanceChangeEnough(double prev, double current)
    {
        double delta = prev - current;
        return delta > 0.001 || delta < -0.001;
    }

    public void logCurrentData()
    {
        radioactiveMaterialValue = source.getRadioactiveMaterial();
        dim = source.world().provider.dimensionId;

        //Only want to log distance changes if it has changed enough to be noticed
        //  This is designed to prevent slow creep (distance < 0.001) from going unnoticed
        if (distanceChangeEnough(x, source.x()))
        {
            x = source.x();
        }
        if (distanceChangeEnough(y, source.y()))
        {
            y = source.y();
        }
        if (distanceChangeEnough(z, source.z()))
        {
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
}
