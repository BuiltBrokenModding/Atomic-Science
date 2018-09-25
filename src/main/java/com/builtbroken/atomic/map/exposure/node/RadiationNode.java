package com.builtbroken.atomic.map.exposure.node;

import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.map.IDataMapSource;
import com.builtbroken.atomic.api.radiation.IRadiationNode;
import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.map.data.DataPool;
import com.builtbroken.atomic.map.data.IDataPoolObject;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public class RadiationNode implements IRadiationNode, IDataPoolObject
{
    private static final DataPool<RadiationNode> RADIATION_NODE_POOL = new DataPool(400000); //TODO add config

    private WeakReference<IRadiationSource> source;

    private int value;

    private RadiationNode(IRadiationSource source, int value)
    {
        this.source = new WeakReference(source);
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
        if (source == null || source.get() == null)
        {
            return null;
        }
        return source.get();
    }

    public static RadiationNode get(IRadiationSource source, int value)
    {
        if (RADIATION_NODE_POOL.has())
        {
            RadiationNode dataChange = RADIATION_NODE_POOL.get();
            if (dataChange != null)
            {
                dataChange.source = new WeakReference(source);
                dataChange.value = value;
                return dataChange;
            }
        }
        return new RadiationNode(source, value);
    }

    @Override
    public void dispose()
    {
        source = null;
        RADIATION_NODE_POOL.dispose(this);
    }
}
