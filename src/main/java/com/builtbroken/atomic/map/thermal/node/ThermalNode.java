package com.builtbroken.atomic.map.thermal.node;

import com.builtbroken.atomic.api.thermal.IThermalSource;
import com.builtbroken.atomic.map.data.DataPool;
import com.builtbroken.atomic.map.data.IDataPoolObject;
import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.map.IDataMapSource;
import com.builtbroken.atomic.api.thermal.IThermalNode;

import javax.annotation.Nullable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/22/2018.
 */
public class ThermalNode implements IThermalNode, IDataPoolObject
{
    private static final DataPool<ThermalNode> THERMAL_NODE_POOL = new DataPool(400000); //TODO add config

    private IThermalSource source;

    private int value;

    private ThermalNode(IThermalSource source, int value)
    {
        this.source = source;
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
        return source;
    }

    public static ThermalNode get(IThermalSource source, int value)
    {
        if (THERMAL_NODE_POOL.has())
        {
            ThermalNode dataChange = THERMAL_NODE_POOL.get();
            if (dataChange != null)
            {
                dataChange.source = source;
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
