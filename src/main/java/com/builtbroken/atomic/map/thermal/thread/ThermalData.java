package com.builtbroken.atomic.map.thermal.thread;

import com.builtbroken.atomic.map.data.DataPool;

/**
 * Created by Dark(DarkGuardsman, Robert) on 7/7/2019.
 */
public class ThermalData
{
    private static final DataPool<ThermalData> objectPool = new DataPool(100000);

    int heatToPush = -1;

    int heatValue;

    private ThermalData()
    {
    }

    public static ThermalData get(int heat)
    {
        if (objectPool.has())
        {
            ThermalData data = objectPool.get();
            if (data != null)
            {
                data.heatToPush = -1;
                data.heatValue = heat;
                return data;
            }
        }
        return new ThermalData().setHeat(heat);
    }

    public void setToPush()
    {
        heatToPush = heatValue;
    }

    public boolean hasPushedHeat()
    {
        return heatToPush != -1;
    }

    public int getHeatToPush()
    {
        return heatToPush;
    }

    public int getHeat()
    {
        return heatValue;
    }

    public int getHeatAndDispose()
    {
        int heat = heatValue;
        objectPool.dispose(this);
        return heat;
    }

    public ThermalData setHeat(int heat)
    {
        this.heatValue = heat;
        return this;
    }

    public ThermalData addHeat(int heat)
    {
        this.heatValue += heat;
        return this;
    }

    public void dispose()
    {
        objectPool.dispose(this);
    }
}
