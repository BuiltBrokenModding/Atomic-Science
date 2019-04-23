package com.builtbroken.atomic.map.data.storage;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.map.IDataMapNode;
import com.builtbroken.atomic.api.map.IDataMapSource;
import com.builtbroken.atomic.map.data.MapValueConsumer;

import java.util.ArrayList;

/**
 * Single chunk of data
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2018.
 */
public class DataChunk
{
    /** The x coordinate of the chunk. */
    public final int xPosition;
    /** The z coordinate of the chunk. */
    public final int zPosition;

    /** The dimension of the world of the chunk. */
    public final int dimension;

    /** Number of world ticks this chunk has been in the unload queue */
    public int unloadTick = 0;

    /** Last time this chunk was scanned for issues */
    public Long lastScanTime = System.currentTimeMillis();

    /** Array of active layers, modified by yStart */
    protected DataLayer[] layers = new DataLayer[256];

    /** Starting point of the layer array as a Y level */
    protected int yStart;

    public DataChunk(int dimension, int xPosition, int zPosition)
    {
        this.dimension = dimension;
        this.xPosition = xPosition;
        this.zPosition = zPosition;
    }

    protected int getChunkHeight()
    {
        return 256; //TODO find a way to get
    }

    /**
     * Sets the value into the chunk
     *
     * @param cx   - location (0-15)
     * @param y    - location (0-255)
     * @param cz   - location (0-15)
     * @param node - data point to add
     */
    public void addData(int cx, int y, int cz, IDataMapNode node)
    {
        //Keep inside of chunk
        if (y >= 0 && y < getChunkHeight())
        {
            getLayer(y).addData(cx, cz, node);
        }
        else if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.error("DataChunk[" + xPosition + "," + zPosition + "]: Something tried to place a block outside map", new RuntimeException("trace"));
        }
    }

    /**
     * Sets the value into the chunk
     *
     * @param cx   - location (0-15)
     * @param y    - location (0-255)
     * @param cz   - location (0-15)
     * @param node - data point to add
     */
    public boolean removeData(int cx, int y, int cz, IDataMapNode node)
    {
        //Keep inside of chunk
        if (y >= 0 && y < getChunkHeight())
        {
            return getLayer(y).removeData(cx, cz, node);
        }
        else if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.error("DataChunk[" + xPosition + "," + zPosition + "]: Something tried to remove a block outside map", new RuntimeException("trace"));
        }
        return false;
    }

    /**
     * Sets the value into the chunk
     *
     * @param cx     - location (0-15)
     * @param y      - location (0-255)
     * @param cz     - location (0-15)
     * @param source - data point to add
     */
    public boolean removeData(int cx, int y, int cz, IDataMapSource source)
    {
        //Keep inside of chunk
        if (y >= 0 && y < getChunkHeight())
        {
            return getLayer(y).removeData(cx, cz, source);
        }
        else if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.error(
                    "DataChunk[" + xPosition + "," + zPosition + "]: " +
                            "Something tried to remove a block outside map, pos(" + cx + ", " + y + ", " + cz + ")",
                    new DataMapException());
        }
        return false;
    }

    /**
     * Gets the data from the chunk
     *
     * @param cx - location (0-15)
     * @param y  - location (0-255)
     * @param cz - location (0-15)
     * @return value stored
     */
    public ArrayList<IDataMapNode> getData(int cx, int y, int cz)
    {
        if (y >= 0 && y < getChunkHeight() && hasLayer(y))
        {
            return getLayer(y).getData(cx, cz);
        }
        return null;
    }

    /**
     * Checks if there is a layer for the y level
     *
     * @param y - y level (0-255)
     * @return true if layer exists
     */
    protected boolean hasLayer(int y)
    {
        return getLayers() != null && y >= getYStart() && y <= getLayerEnd() && getLayers()[getIndex(y)] != null;
    }

    /**
     * End point of the layers, inclusive
     *
     * @return
     */
    public int getLayerEnd()
    {
        return getYStart() + getLayers().length - 1;
    }

    /**
     * Called to remove a layer
     *
     * @param y
     */
    protected void removeLayer(int y)
    {
        int index = getIndex(y);
        if (index >= 0 && index < getLayers().length)
        {
            getLayers()[index] = null;
        }
    }

    /**
     * Called to get a layer
     *
     * @param y
     * @return
     */
    public DataLayer getLayer(int y)
    {
        //If layer is null, create layer
        if (getLayers()[getIndex(y)] == null)
        {
            getLayers()[getIndex(y)] = new DataLayer(this, y);
        }
        return getLayers()[getIndex(y)];
    }

    /**
     * Converts y level to layer index
     *
     * @param y
     * @return
     */
    protected int getIndex(int y)
    {
        return y - getYStart();
    }

    public DataLayer[] getLayers()
    {
        return layers;
    }

    public int getYStart()
    {
        return yStart;
    }

    public boolean hasData()
    {
        for (DataLayer layer : getLayers())
        {
            if (layer != null && !layer.isEmpty())
            {
                return true;
            }
        }
        return false;
    }

    public void checkForIssues()
    {
        for (int i = 0; i < getLayers().length; i++)
        {
            DataLayer layer = getLayers()[i];
            if (layer != null)
            {
                //Allow layer to free up data and correct problems
                layer.checkForIssues();

                //If layer is empty remove to save memory
                if (layer.isEmpty())
                {
                    getLayers()[i] = null;
                }
            }
        }
    }

    public final void forEachValue(MapValueConsumer consumer, DataMapType type)
    {
        for (DataLayer layer : getLayers())
        {
            if (layer != null && layer.blocksUsed > 0)
            {
                for (int cx = 0; cx < 16; cx++)
                {
                    for (int cz = 0; cz < 16; cz++)
                    {
                        ArrayList<IDataMapNode> list = layer.getData(cx, cz);
                        if (list != null && !list.isEmpty())
                        {
                            int value = type.getValue(list);
                            if (value > 0)
                            {
                                int x = cx + xPosition * 16;
                                int z = cz + xPosition * 16;
                                consumer.accept(dimension, x, layer.y_index, z, value);
                            }
                        }
                    }
                }
            }
        }
    }
}
