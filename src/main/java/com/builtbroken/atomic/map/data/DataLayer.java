package com.builtbroken.atomic.map.data;

/**
 * Single Y level of data stores in the world
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2018.
 */
public class DataLayer
{
    /** Index of this layer */
    public final int y_index;

    /** Stored data in this layer */
    public final int[] data;

    /** Number of non-zero slots, used to track if layer is empty */
    public int blocksUsed = 0;

    public DataLayer(int y_index)
    {
        this.y_index = y_index;
        this.data = new int[16 * 16];
    }

    /**
     * Gets the data from the layer
     *
     * @param x - location
     * @param z - location
     * @return value
     */
    public int getData(int x, int z)
    {
        int index = index(x, z);
        if (index >= 0)
        {
            return data[index];
        }
        return 0;
    }

    /**
     * Sets data into the layer
     *
     * @param x     - location
     * @param z     - location
     * @param value - value
     * @return true if data was set, false if nothing happened (likely means outside of the map)
     */
    public boolean setData(int x, int z, int value)
    {
        int index = index(x, z);
        if (index >= 0)
        {
            int prev = data[index];
            data[index] = value;

            if (prev != 0 && value == 0)
            {
                blocksUsed--;
            }
            else if (prev == 0 && value != 0)
            {
                blocksUsed++;
            }
            return true;
        }
        return false;
    }

    /**
     * Index of the x z location
     *
     * @param x - location 0-15
     * @param z - location 0-15
     * @return index between 0-255, -1 returns if input data is invalid
     */
    public final int index(int x, int z)
    {
        //Bound check to prevent index values from generating outside range
        //      Is needed as a negative z can cause a value to overlap values normally in range
        //      Ex: 15x -1z -> 239, which is in range but not the right index
        if (x >= 0 && x < 16 && z >= 0 && z < 16)
        {
            return x * 16 + z;
        }
        return -1;
    }

    /**
     * Is the layer empty
     *
     * @return true if no blocks were ever set
     */
    public boolean isEmpty()
    {
        return blocksUsed <= 0;
    }

    //------------------------------------
    //Estimated ~memory usage data for int[]
    //------------------------------------
    //Each layer:
    //  int is 32 bits
    //  256 indexes
    //  8,192 bits per layer or 1024 bytes or 1 KiloBytes Memory

    //Full Chunk:
    //  2,097,152 bits or ~262 KiloBytes Memory
    //  For 32x32 chunks -> 2,147,483,648 bits -> 268,435,456 bytes or ~268 MB Memory

    //10 layer load per chunk
    //  On average radiation will only populate dirt & plants, which is a very low % of a chunk
    //  81,920 bits -> 10,240 bytes -> ~10 KiloBytes Memory
    //  For 32x32 chunks -> 83,886,080 bits -> 10,485,760 bytes -> ~10 MegaBytes Memory
    //------------------------------------
}
