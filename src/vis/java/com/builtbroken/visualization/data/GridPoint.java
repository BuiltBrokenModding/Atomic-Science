package com.builtbroken.visualization.data;

import com.builtbroken.atomic.map.data.DataPool;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/26/2018.
 */
public class GridPoint
{
    private static final DataPool<GridPoint> dataPosPool = new DataPool(100000);

    public int x;
    public int y;

    protected GridPoint(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public static GridPoint get(int x, int y)
    {
        if (dataPosPool.has())
        {
            GridPoint dataPos = dataPosPool.get();
            if (dataPos != null)
            {
                dataPos.x = x;
                dataPos.y = y;
                return dataPos;
            }
        }
        return new GridPoint(x, y);
    }

    public double distanceSQ(GridPoint pos)
    {
        return x * x + y * y;
    }

    public double distance(GridPoint pos)
    {
        return Math.sqrt(distanceSQ(pos));
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        else if (object instanceof GridPoint)
        {
            return x == ((GridPoint) object).x && y == ((GridPoint) object).y;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 23;
        hash = hash * 31 + x;
        hash = hash * 31 + y;
        return hash;
    }

    @Override
    public String toString()
    {
        return "DataPoint[" + x + "," + y + "]";
    }

    public void dispose()
    {
        dataPosPool.dispose(this);
    }
}
