package com.builtbroken.atomic.map.data.storage;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.map.IDataMapNode;
import com.builtbroken.atomic.api.map.IDataMapSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Single Y level of data stores in the world
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2018.
 */
public class DataLayer
{
    /** Index of this layer */
    public final int y_index;

    public final DataChunk host;

    /** Stored data in this layer */
    private final ArrayList<IDataMapNode>[] data;

    /** Number of non-zero slots, used to track if layer is empty */
    public int blocksUsed = 0;

    public DataLayer(DataChunk host, int y_index)
    {
        this.y_index = y_index;
        this.host = host;
        this.data = new ArrayList[16 * 16];
    }

    /**
     * Gets the data from the layer
     *
     * @param x - location
     * @param z - location
     * @return value
     */
    public ArrayList<IDataMapNode> getData(int x, int z)
    {
        int index = index(x, z);
        if (index >= 0)
        {
            return data[index];
        }
        return null;
    }

    public void forEach(int x, int z, DataMapType type, Consumer<IDataMapNode> consumer)
    {
        ArrayList<IDataMapNode> list = getData(x, z);
        if (list != null)
        {
            for (IDataMapNode node : list)
            {
                if (node != null && node.getType() == type)
                {
                    consumer.accept(node);
                }
            }
        }
    }

    /**
     * Called to remove nodes from source in the chunk
     * *
     *
     * @param source - source of nodes
     * @return true if a node was removed
     */
    public boolean removeData(IDataMapSource source)
    {
        int count = 0;
        for (int i = 0; i < 256; i++)
        {
            count += removeData(source, data[i]);
        }
        blocksUsed -= count;
        return count > 0;
    }

    /**
     * Called to remove nodes from source at the position
     *
     * @param x      - location
     * @param z      - location
     * @param source - source of nodes
     * @return true if a node was removed
     */
    public boolean removeData(int x, int z, IDataMapSource source)
    {
        int count = removeData(source, getData(x, z));
        blocksUsed -= count;
        return count > 0;
    }

    /**
     * Called to remove nodes from source at the position
     *
     * @param x    - location
     * @param z    - location
     * @param node - data point to remove
     * @return true if a node was removed
     */
    public boolean removeData(int x, int z, IDataMapNode node)
    {
        ArrayList<IDataMapNode> list = getData(x, z);
        if (list != null && list.remove(node))
        {
            blocksUsed -= 1;
            return true;
        }
        return false;
    }

    private int removeData(IDataMapSource source, ArrayList<IDataMapNode> list)
    {
        int count = 0;
        if (list != null && !list.isEmpty())
        {
            Iterator<IDataMapNode> it = list.iterator();
            while (it.hasNext())
            {
                IDataMapNode node = it.next();
                if (node == null || !node.isNodeValid())
                {
                    it.remove();
                }
                else if (source.equals(node.getSource()))
                {
                    it.remove();
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Sets data into the layer
     *
     * @param x    - location
     * @param z    - location
     * @param node - data point
     */
    public void addData(int x, int z, IDataMapNode node)
    {
        final int index = index(x, z);
        if (index >= 0)
        {
            //Check if array list is null, set if null
            if (data[index] == null)
            {
                data[index] = new ArrayList();
            }

            //Add data
            data[index].add(node);

            //Increase block count
            blocksUsed++;
        }
        else if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.error("DataLayer[" + y_index + ", " + host + "] Something tried to insert outside of the layer's bounds at [" + x + ", " + z + "]", new RuntimeException("trace"));
        }
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

    public void checkForIssues()
    {
        for (int i = 0; i < data.length; i++)
        {
            if (data[i] != null)
            {
                //Clear bad nodes
                Iterator<IDataMapNode> it = data[i].iterator();
                while (it.hasNext())
                {
                    IDataMapNode node = it.next();
                    if (!node.isNodeValid())
                    {
                        it.remove();
                    }
                }

                //If empty clear slot
                if (data[i].size() <= 0)
                {
                    data[i] = null;
                }
            }
        }
    }
}
