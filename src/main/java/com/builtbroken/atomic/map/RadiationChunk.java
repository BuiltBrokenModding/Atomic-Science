package com.builtbroken.atomic.map;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Single chunk of radiation data
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2018.
 */
public class RadiationChunk
{
    //Constants for NBT save/load
    public static final String NBT_Y_START = "y_start";
    public static final String NBT_SIZE = "size";
    public static final String NBT_LAYERS = "layers";
    public static final String NBT_LAYER_INDEX = "i";
    public static final String NBT_LAYER_Y = "y";
    public static final String NBT_LAYER_DATA = "data";

    /** The x coordinate of the chunk. */
    public final int xPosition;
    /** The z coordinate of the chunk. */
    public final int zPosition;

    /** The dimension of the world of the chunk. */
    public final int dimension;

    /** Array of active layers, modified by yStart */
    protected RadiationLayer[] layers;

    /** Starting point of the layer array as a Y level */
    protected int yStart;

    /** Triggers thread to rescan chunk to calculate exposure values */
    public boolean hasChanged = true;

    /** Used by thread to let it know last time it scanned this thread */
    public long lastScanTime;

    public RadiationChunk(int dimension, int xPosition, int zPosition)
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
     * @param cx    - location (0-15)
     * @param y     - location (0-255)
     * @param cz    - location (0-15)
     * @param value - value to set
     */
    public boolean setValue(int cx, int y, int cz, int value)
    {
        //Keep inside of chunk
        if (y >= 0 && y < getChunkHeight())
        {
            //Only set values that are above zero or have an existing layer
            if (value > 0 || hasLayer(y))
            {
                int prev = getLayer(y).getData(cx, cz);

                //Set data into layer
                boolean b = getLayer(y).setData(cx, cz, value);

                //Remove layer if empty to save memory
                if (getLayer(y).isEmpty())
                {
                    removeLayer(y);
                }

                //Check for change
                if (prev != getLayer(y).getData(cx, cz))
                {
                    hasChanged = true;
                }

                //Return
                return b;
            }
            return true; //value was zero with no layer, return true as in theory prev = 0 and value = 0
        }
        else if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.error("Something tried to place a block outside map", new RuntimeException("trace"));
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
    public int getValue(int cx, int y, int cz)
    {
        if (y >= 0 && y < getChunkHeight() && hasLayer(y))
        {
            return getLayer(y).getData(cx, cz);
        }
        return 0;
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
    protected RadiationLayer getLayer(int y)
    {
        //Init array if not initialized
        if (getLayers() == null)
        {
            layers = new RadiationLayer[11];
            yStart = Math.max(0, y - 10);
        }
        //Check if we need to increase layer array to fit a new value
        else if (y < getYStart())
        {
            final RadiationLayer[] oldLayers = getLayers();

            //Increase array size by 5 more than expected y level, if under 10 fully expand to zero
            int increase = getYStart() > 10 ? ((getYStart() - y) + 5) : getYStart();

            //New array
            int newLength = Math.min(getChunkHeight(), oldLayers.length + increase);
            layers = new RadiationLayer[newLength];

            //Copy array
            for (int i = 0; i < oldLayers.length; i++)
            {
                getLayers()[i + increase] = oldLayers[i];
            }

            //Set new y start
            yStart = getYStart() - increase;
        }
        else if (y > getLayerEnd())
        {
            RadiationLayer[] oldLayers = getLayers();

            //Increase array size by 5 above y
            int increase = y - (getYStart() + getLayers().length) + 5;
            layers = new RadiationLayer[Math.min(getChunkHeight(), oldLayers.length + increase)];

            //Copy array
            for (int i = 0; i < oldLayers.length; i++)
            {
                getLayers()[i] = oldLayers[i];
            }
        }

        //If layer is null, create layer
        if (getLayers()[getIndex(y)] == null)
        {
            getLayers()[getIndex(y)] = new RadiationLayer(y);
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

    /**
     * Called to save data
     *
     * @param tag
     */
    public void save(NBTTagCompound tag)
    {
        if (getLayers() != null)
        {
            tag.setInteger(NBT_Y_START, getYStart());
            tag.setInteger(NBT_SIZE, getLayers().length);
            NBTTagList list = new NBTTagList();
            for (int i = 0; i < getLayers().length; i++)
            {
                RadiationLayer layer = getLayers()[i];
                if (layer != null && !layer.isEmpty())
                {
                    NBTTagCompound save = new NBTTagCompound();
                    save.setInteger(NBT_LAYER_INDEX, i);
                    save.setInteger(NBT_LAYER_Y, layer.y_index);
                    save.setIntArray(NBT_LAYER_DATA, layer.data);
                    list.appendTag(save);
                }
            }

            tag.setTag(NBT_LAYERS, list);
        }
    }

    /**
     * Called to load data from save
     *
     * @param tag
     */
    public void load(NBTTagCompound tag)
    {
        //Set y start
        this.yStart = tag.getInteger(NBT_Y_START);

        //Rebuild array
        int size = tag.getInteger(NBT_SIZE);
        this.layers = new RadiationLayer[size];

        //Load layers
        NBTTagList list = tag.getTagList(NBT_LAYERS, 10);
        for (int list_index = 0; list_index < list.tagCount(); list_index++)
        {
            NBTTagCompound save = list.getCompoundTagAt(list_index);

            //Load indexs
            int index = save.getInteger(NBT_LAYER_INDEX);
            int y = save.getInteger(NBT_LAYER_Y);

            //Create layer
            RadiationLayer layer = new RadiationLayer(y);

            //Load data
            int[] data = save.getIntArray(NBT_LAYER_DATA);

            //Error if invalid size (unlikely to happen unless corruption or user errors)
            if (data.length != layer.data.length)
            {
                AtomicScience.logger.error(String.format("RadiationChunk[%sd, %scx, %scz]#load(NBT) layer[%s] -> data array has " +
                                "invalid size, will attempt to read in as much as possible. This may result" +
                                "in radiation values changing per position for the given y level.",
                        dimension, xPosition, zPosition, y));
            }

            //Copy over array
            for (int j = 0; j < data.length && j < layer.data.length; j++)
            {
                layer.data[j] = data[j];
                if (data[j] != 0)
                {
                    layer.blocksUsed++;
                }
            }

            //Insert layer
            getLayers()[index] = layer;
        }
    }

    public RadiationLayer[] getLayers()
    {
        return layers;
    }

    public int getYStart()
    {
        return yStart;
    }
}
