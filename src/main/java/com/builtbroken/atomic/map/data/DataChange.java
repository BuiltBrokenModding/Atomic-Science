package com.builtbroken.atomic.map.data;

/**
 * Used to store data for thread
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/28/2018.
 */
public class DataChange extends DataPos
{
    private static final DataPool<DataChange> dataChangePool = new DataPool(100000);

    public int dim;

    public int old_value;
    public int new_value;

    public DataChange(int dim, int x, int y, int z, int old_value, int new_value)
    {
        super(x, y, z);
        this.dim = dim;
        this.old_value = old_value;
        this.new_value = new_value;
    }

    public static DataChange get(int dim, int x, int y, int z, int old_value, int new_value)
    {
        if (dataChangePool.has())
        {
            DataChange dataChange = dataChangePool.get();
            if (dataChange != null)
            {
                dataChange.dim = dim;
                dataChange.x = x;
                dataChange.y = y;
                dataChange.z = z;
                dataChange.old_value = old_value;
                dataChange.new_value = new_value;
                return dataChange;
            }
        }
        return new DataChange(dim, x, y, z, old_value, new_value);
    }

    public void dispose()
    {
        dataChangePool.dispose(this);
    }
}
