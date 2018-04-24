package com.builtbroken.atomic.map;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Single chunk of radiation data
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2018.
 */
public class RadiationChunk
{
    /** The x coordinate of the chunk. */
    public final int xPosition;
    /** The z coordinate of the chunk. */
    public final int zPosition;

    /** Array of active layers, modified by yStart */
    protected RadiationLayer[] layers;

    /** Starting point of the layer array as a Y level */
    protected int yStart;

    public RadiationChunk(int xPosition, int zPosition)
    {
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
    public void setValue(int cx, int y, int cz, int value)
    {
        //Keep inside of chunk
        if (y >= 0 && y < getChunkHeight())
        {
            //Only set values that are above zero or have an existing layer
            if (value > 0 || hasLayer(y))
            {
                //Set data into layer
                getLayer(y).setData(cx, cz, value);

                //Remove layer if empty to save memory
                if (getLayer(y).isEmpty())
                {
                    removeLayer(y);
                }
            }
        }
        else if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.error("Something tried to place a block outside map", new RuntimeException("trace"));
        }
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
        return !(y < yStart || y >= yStart + layers.length || layers[getIndex(y)] == null);
    }

    /**
     * Called to remove a layer
     *
     * @param y
     */
    protected void removeLayer(int y)
    {
        int index = getIndex(y);
        if (index >= 0 && index < layers.length)
        {
            layers[index] = null;
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
        if (layers == null)
        {
            layers = new RadiationLayer[11];
            yStart = Math.max(0, y - 5);
        }
        //Check if we need to increase layer array to fit a new value
        else if (y < yStart)
        {
            RadiationLayer[] oldLayers = layers;

            //Increase array size by 5 or distance to zero
            int increase = y > 5 ? 5 : y + 1;
            layers = new RadiationLayer[oldLayers.length + increase];

            //Copy array
            for (int i = 0; i < oldLayers.length; i++)
            {
                layers[i + increase] = oldLayers[i];
            }

            //Set new y start
            yStart = yStart - increase;
        }
        else if (y >= yStart + layers.length)
        {
            RadiationLayer[] oldLayers = layers;

            //Increase array size by 5 above y
            int increase = y - (yStart + layers.length) + 5;
            layers = new RadiationLayer[Math.min(getChunkHeight(), oldLayers.length + increase)];

            //Copy array
            for (int i = 0; i < oldLayers.length; i++)
            {
                layers[i] = oldLayers[i];
            }
        }

        //If layer is null, create layer
        if (layers[getIndex(y)] == null)
        {
            layers[getIndex(y)] = new RadiationLayer(y);
        }
        return layers[getIndex(y)];
    }

    /**
     * Converts y level to layer index
     *
     * @param y
     * @return
     */
    protected int getIndex(int y)
    {
        return y - yStart;
    }

    public void save(NBTTagCompound tag)
    {

    }

    public void load(NBTTagCompound tag)
    {

    }
}
