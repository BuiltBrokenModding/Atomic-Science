package com.builtbroken.atomic.map.thermal.node;

import com.builtbroken.atomic.api.thermal.IThermalSource;
import com.builtbroken.atomic.map.data.DataPool;
import com.builtbroken.atomic.map.data.IDataPoolObject;
import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.map.IDataMapSource;
import com.builtbroken.atomic.api.thermal.IThermalNode;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/22/2018.
 */
public class ThermalNode implements IThermalNode, IDataPoolObject
{
    private static final DataPool<ThermalNode> THERMAL_NODE_POOL = new DataPool(400000); //TODO add config

    private WeakReference<IThermalSource> source;

    private int value;

    private ThermalNode(IThermalSource source, int value)
    {
        this.source = new WeakReference(source);
        this.value = value;
    }

    @Override
    public int getHeatValue()
    {
        return value;
    }

    @Override
    public void setHeatValue(int value)
    {
        this.value = value;
    }

    @Override
    public DataMapType getType()
    {
        return DataMapType.THERMAL;
    }

    @Nullable
    @Override
    public IDataMapSource getSource()
    {
        if(source == null || source.get() == null)
        {
            return null;
        }
        return source.get();
    }

    public static ThermalNode get(IThermalSource source, int value)
    {
        if (THERMAL_NODE_POOL.has())
        {
            ThermalNode dataChange = THERMAL_NODE_POOL.get();
            if (dataChange != null)
            {
                dataChange.source = new WeakReference(source);
                dataChange.value = value;
                return dataChange;
            }
        }
        return new ThermalNode(source, value);
    }

    @Override
    public void dispose()
    {
        source = null;
        THERMAL_NODE_POOL.dispose(this);
    }
}
