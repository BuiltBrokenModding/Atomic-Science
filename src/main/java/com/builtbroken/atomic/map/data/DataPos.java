package com.builtbroken.atomic.map.data;

import com.builtbroken.jlib.data.vector.IPos3D;
import net.minecraft.util.EnumFacing;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/12/2018.
 */
@Deprecated
public class DataPos implements IPos3D
{
    private static final DataPool<DataPos> dataPosPool = new DataPool(100000);

    public int x;
    public int y;
    public int z;

    protected DataPos(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static DataPos get(DataPos pos, EnumFacing direction)
    {
        int i = pos.x + direction.getXOffset();
        int j = pos.y + direction.getYOffset();
        int k = pos.z + direction.getZOffset();
        return get(i, j, k);
    }

    public static DataPos get(int x, int y, int z, EnumFacing direction)
    {
        int i = x + direction.getXOffset();
        int j = y + direction.getYOffset();
        int k = z + direction.getZOffset();
        return get(i, j, k);
    }

    public static DataPos get(int x, int y, int z)
    {
        if (dataPosPool.has())
        {
            DataPos dataPos = dataPosPool.get();
            if (dataPos != null)
            {
                dataPos.x = x;
                dataPos.y = y;
                dataPos.z = z;
                return dataPos;
            }
        }
        return new DataPos(x, y, z);
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
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        else if (object instanceof DataPos)
        {
            return x == ((DataPos) object).x && y == ((DataPos) object).y && z == ((DataPos) object).z;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 23;
        hash = hash * 31 + x;
        hash = hash * 31 + y;
        hash = hash * 31 + z;
        return hash;
    }

    @Override
    public String toString()
    {
        return "DataPos[" + x + "," + y + "," + z + "]";
    }

    public void dispose()
    {
        dataPosPool.dispose(this);
    }
}
