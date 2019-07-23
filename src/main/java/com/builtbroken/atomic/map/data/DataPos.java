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
        System.out.printf("Get: %d %d %d\n", x, y, z);
        if (dataPosPool.has())
        {
            final DataPos dataPos = dataPosPool.get();
            if (dataPos != null && dataPos.released)
            {
                dataPos.unlock();
                dataPos.set(x, y, z);
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

    @Override
    public int zi()
    {
        return z;
    }

    @Override
    public int xi()
    {
        return x;
    }

    @Override
    public int yi()
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

    public DataPos lock()
    {
        this.mutable = false;
        return this;
    }

    public DataPos unlock()
    {
        this.mutable = true;
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
            return x == ((DataPos) object).x
                    && y == ((DataPos) object).y
                    && z == ((DataPos) object).z;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return (this.yi() + this.zi() * 31) * 31 + this.xi();
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
        System.out.println("Release: " + this);
        if(released)
        {
            throw new RuntimeException(this + " has already been released");
        }
        else if(mutable)
        {
            released = true;
            dataPosPool.dispose(this.lock());
        }
        else
        {
            throw new RuntimeException(this + " is locked and can not be recycled until unlocked.");
        }
    }

    public BlockPos disposeReturnBlockPos()
    {
        final BlockPos pos = new BlockPos(xi(), yi(), zi());
        dispose();
        return pos;
    }
}
