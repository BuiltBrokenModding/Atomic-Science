package com.builtbroken.atomic.map.exposure;

import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.map.data.DataPool;
import com.builtbroken.atomic.map.data.IDataPoolObject;
import com.builtbroken.atomic.map.data.node.DataMapType;
import com.builtbroken.atomic.map.data.node.IDataMapSource;
import com.builtbroken.atomic.map.data.node.IRadiationNode;

import javax.annotation.Nullable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public class RadiationNode implements IRadiationNode, IDataPoolObject
{
    private static final DataPool<RadiationNode> RADIATION_NODE_POOL = new DataPool(100000);

    private IRadiationSource source;

    private int value;

    private RadiationNode(IRadiationSource source, int value)
    {
        this.source = source;
        this.value = value;
    }

    @Override
    public int getRadiationValue()
    {
        return value;
    }

    @Override
    public void setRadiationValue(int value)
    {
        this.value = value;
    }

    @Override
    public DataMapType getType()
    {
        return DataMapType.RADIATION;
    }

    @Nullable
    @Override
    public IDataMapSource getSource()
    {
        return source;
    }

    public static RadiationNode get(IRadiationSource source, int value)
    {
        if (RADIATION_NODE_POOL.has())
        {
            RadiationNode dataChange = RADIATION_NODE_POOL.get();
            if (dataChange != null)
            {
                dataChange.source = source;
                dataChange.value = value;
                return dataChange;
            }
        }
        return new RadiationNode(source, value);
    }

    public void dispose()
    {
        RADIATION_NODE_POOL.dispose(this);
    }
}
