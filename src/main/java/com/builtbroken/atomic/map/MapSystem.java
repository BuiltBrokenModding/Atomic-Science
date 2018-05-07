package com.builtbroken.atomic.map;

import com.builtbroken.atomic.map.data.DataMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;

/**
 * Generic map system that can be used for anything so long as it stores data to the map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/6/2018.
 */
public class MapSystem
{
    /** Key used to save to the chunk */
    protected final String saveKey;

    /** Unique ID for tracking the map in events */
    protected final String id;

    /** Dimension to data map, saved to world and updated over time */
    protected final HashMap<Integer, DataMap> dimensionToMap = new HashMap();

    /**
     * @param id      - unique ID for tracking the map in events
     * @param saveKey - key used to save data to NBT of the game's map
     */
    public MapSystem(String id, String saveKey)
    {
        this.id = id;
        this.saveKey = saveKey;
    }

    /**
     * Gets the exposure map
     * <p>
     * If threaded map:
     * Make sure all changes to the map are thread safe. As this is
     * heavily accessed by the thread system. Changing values while
     * the thread is running can cause the system to break.
     *
     * @param dim  - dimension of the map to get
     * @param init - true to generate the map
     * @return map, or null if it was never created
     */
    public DataMap getMap(int dim, boolean init)
    {
        DataMap map = dimensionToMap.get(dim);
        if (map == null && init)
        {
            map = newMap(dim);
            dimensionToMap.put(dim, map);
        }
        return map;
    }

    protected DataMap newMap(int dim)
    {
        return new DataMap(this, dim);
    }

    /**
     * Gets the data map
     * <p>
     * If threaded map:
     * Make sure all changes to the map are thread safe. As this is
     * heavily accessed by the thread system. Changing values while
     * the thread is running can cause the system to break.
     *
     * @param world - dimension of the map to get
     * @param init  - true to generate the map
     * @return map, or null if it was never created
     */
    public DataMap getMap(World world, boolean init)
    {
        if (world != null && world.provider != null)
        {
            return getMap(world.provider.dimensionId, init);
        }
        return null;
    }


    /**
     * Gets the data value at the position
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return radioactive material amount
     */
    public int getData(World world, int x, int y, int z)
    {
        DataMap map = getMap(world, false);
        if (map != null)
        {
            return map.getData(x, y, z);
        }
        return 0;
    }

    /**
     * Gets the data value at the position
     *
     * @param dim - world id
     * @param x   - location
     * @param y   - location
     * @param z   - location
     * @return radioactive material amount
     */
    public int getData(int dim, int x, int y, int z)
    {
        DataMap map = getMap(dim, false);
        if (map != null)
        {
            return map.getData(x, y, z);
        }
        return 0;
    }

    /**
     * Called to set the data value of the position
     *
     * @param world  - location
     * @param x      - location
     * @param y      - location
     * @param z      - location
     * @param amount - data
     * @return true if the value was set
     */
    public boolean setData(World world, int x, int y, int z, int amount)
    {
        DataMap map = getMap(world, amount > 0);
        if (map != null)
        {
            return map.setData(x, y, z, amount);
        }
        return true;
    }

    /**
     * Called to set the data value of the position
     *
     * @param dim    - world id
     * @param x      - location
     * @param y      - location
     * @param z      - location
     * @param amount - data
     * @return true if the value was set
     */
    public boolean setData(int dim, int x, int y, int z, int amount)
    {
        DataMap map = getMap(dim, amount > 0);
        if (map != null)
        {
            return map.setData(x, y, z, amount);
        }
        return true;
    }

    ///----------------------------------------------------------------
    ///--------World events
    ///----------------------------------------------------------------

    public void onWorldUnload(World world)
    {
        DataMap map = getMap(world, false);
        if (map != null)
        {
            map.onWorldUnload();
        }
    }

    ///----------------------------------------------------------------
    ///-------Chunk Events
    ///----------------------------------------------------------------

    public void onChunkUnload(World world, Chunk chunk) //Only called if chunk unloads separate from world unload
    {
        DataMap map = getMap(world, false);
        if (map != null)
        {
            map.unloadChunk(chunk);
        }
    }

    public void onChunkLoadData(World world, Chunk chunk, NBTTagCompound save) //Called before chunk load event
    {
        if (save != null && save.hasKey(saveKey))
        {
            DataMap map = getMap(world, true);
            if (map != null)
            {
                NBTTagCompound tag = save.getCompoundTag(saveKey);
                if (!tag.hasNoTags())
                {
                    map.loadChunk(chunk, tag);
                }
            }
        }
    }

    public void onChunkSaveData(World world, Chunk chunk, NBTTagCompound save) //Called on world save
    {
        DataMap map = getMap(world, false);
        if (map != null)
        {
            NBTTagCompound tag = new NBTTagCompound();
            map.saveChunk(chunk, tag);
            if (!tag.hasNoTags())
            {
                save.setTag(saveKey, tag);
            }
        }
    }
}
