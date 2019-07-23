package com.builtbroken.atomic.map.data;

import com.builtbroken.jlib.data.vector.IPos3D;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Dark(DarkGuardsman, Robert) on 5/12/2018.
 */
@Deprecated
public class DataPos implements IPos3D
{

    private static final DataPool<DataPos> dataPosPool = new DataPool(100000);

    private int x;
    private int y;
    private int z;

    private boolean released = false;
    private boolean mutable = true;

    protected DataPos(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static DataPos get(IPos3D pos)
    {
        return get(pos.xi(), pos.yi(), pos.zi());
    }

    public static DataPos get(IPos3D pos, EnumFacing direction)
    {
        int i = pos.xi() + direction.getXOffset();
        int j = pos.yi() + direction.getYOffset();
        int k = pos.zi() + direction.getZOffset();
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
            final DataPos dataPos = dataPosPool.get();
            if (dataPos != null)
            {
                dataPos.x = x;
                dataPos.y = y;
                dataPos.z = z;
                dataPos.released = false;
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

    public void set(int x, int y, int z)
    {
        if (mutable)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        else
        {
            throw new RuntimeException(this + " is locked from being changed");
        }
    }

    public DataPos toggleLock()
    {
        this.mutable = !mutable;
        return this;
    }

    public double distanceSQ(DataPos pos)
    {
        int dx = pos.x - x;
        int dy = pos.y - y;
        int dz = pos.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double distance(DataPos pos)
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
        if (released)
        {
            return "DataPos[Pooled]";
        }
        return String.format("DataPos[%d, %d, %d]", x, y, z);
    }

    public void dispose()
    {
        released = true;
        dataPosPool.dispose(this);
    }

    public BlockPos disposeReturnBlockPos()
    {
        BlockPos pos = new BlockPos(xi(), yi(), zi());
        dataPosPool.dispose(this);
        return pos;
    }
}
