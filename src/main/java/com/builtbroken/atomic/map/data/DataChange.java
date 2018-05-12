package com.builtbroken.atomic.map.data;

import com.builtbroken.jlib.data.vector.IPos3D;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Used to store data for thread
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/28/2018.
 */
public class DataChange implements IPos3D //TODO make mutable and use object pool to save ram
{
    public static int maxObjectPoleCount = 100000;
    private static ConcurrentLinkedQueue<DataChange> objectPole = new ConcurrentLinkedQueue();
    private static int objectPoleCount = 0;

    public int dim;
    public int x;
    public int y;
    public int z;

    public int old_value;
    public int new_value;

    protected DataChange(int dim, int x, int y, int z, int old_value, int new_value)
    {
        this.dim = dim;
        this.x = x;
        this.y = y;
        this.z = z;
        this.old_value = old_value;
        this.new_value = new_value;
    }

    public static DataChange get(int dim, int x, int y, int z, int old_value, int new_value)
    {
        if (!objectPole.isEmpty())
        {
            DataChange dataChange = objectPole.poll();
            if(dataChange != null)
            {
                dataChange.dim = dim;
                dataChange.x = x;
                dataChange.y = y;
                dataChange.z = z;
                dataChange.old_value = old_value;
                dataChange.new_value = new_value;

                objectPoleCount--;

                return dataChange;
            }
            objectPoleCount = objectPole.size();
        }
        objectPoleCount = 0;
        return new DataChange(dim, x, y, z, old_value, new_value);
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

    public void dispose()
    {
        if(objectPoleCount < maxObjectPoleCount)
        {
            objectPole.add(this);
            objectPoleCount++;
        }
    }
}
