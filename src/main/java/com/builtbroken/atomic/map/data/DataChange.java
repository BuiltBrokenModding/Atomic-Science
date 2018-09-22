package com.builtbroken.atomic.map.data;

import com.builtbroken.atomic.lib.transform.IPosWorld;
import com.builtbroken.atomic.api.map.IDataMapSource;
import net.minecraft.world.World;

/**
 * Used to store data for thread
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/28/2018.
 */
public class DataChange implements IPosWorld
{
    private static final DataPool<DataChange> dataChangePool = new DataPool(100000);

    public IDataMapSource source;

    public int value;

    private DataChange(IDataMapSource source, int value)
    {
        this.source = source;
        this.value = value;
    }

    public static DataChange get(IDataMapSource source, int value)
    {
        if (dataChangePool.has())
        {
            DataChange dataChange = dataChangePool.get();
            if (dataChange != null)
            {
                dataChange.source = source;
                dataChange.value = value;
                return dataChange;
            }
        }
        return new DataChange(source, value);
    }

    public void dispose()
    {
        dataChangePool.dispose(this);
    }

    @Override
    public World world()
    {
        return source.world();
    }

    @Override
    public int dim()
    {
        return source.dim();
    }

    @Override
    public double x()
    {
        return source.x();
    }

    @Override
    public double y()
    {
        return source.y();
    }

    @Override
    public double z()
    {
        return source.z();
    }
}
