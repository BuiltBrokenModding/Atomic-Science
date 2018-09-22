package com.builtbroken.atomic.map;

import com.builtbroken.atomic.api.map.IDataMapNode;
import com.builtbroken.atomic.api.map.IDataMapSource;
import com.builtbroken.atomic.map.data.storage.DataMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Generic map system that can be used for anything so long as it stores data to the map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/6/2018.
 */
public class MapSystem
{

    /** Dimension to data map, saved to world and updated over time */
    protected final HashMap<Integer, DataMap> dimensionToMap = new HashMap();

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
            return getMap(world.provider.getDimension(), init);
        }
        return null;
    }


    /**
     * Gets the data value at the position
     *
     * @param world - location
     * @param pos   - location
     * @return radioactive material amount
     */
    public ArrayList<IDataMapNode> getData(World world, BlockPos pos)
    {
        DataMap map = getMap(world, false);
        if (map != null)
        {
            return map.getData(pos);
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
    public ArrayList<IDataMapNode> getData(World world, int x, int y, int z)
    {
        DataMap map = getMap(world, false);
        if (map != null)
        {
            return map.getData(x, y, z);
        }
        return null;
    }

    /**
     * Gets the data value at the position
     *
     * @param dim - world id
     * @param pos - location
     * @return radioactive material amount
     */
    public ArrayList<IDataMapNode> getData(int dim, BlockPos pos)
    {
        DataMap map = getMap(dim, false);
        if (map != null)
        {
            return map.getData(pos);
        }
        return null;
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
    public ArrayList<IDataMapNode> getData(int dim, int x, int y, int z)
    {
        DataMap map = getMap(dim, false);
        if (map != null)
        {
            return map.getData(x, y, z);
        }
        return null;
    }

    /**
     * Called to set the data value of the position
     *
     * @param dim  - world id
     * @param x    - location
     * @param y    - location
     * @param z    - location
     * @param node - data
     * @return true if the value was set
     */
    public void addData(int dim, int x, int y, int z, IDataMapNode node)
    {
        DataMap map = getMap(dim, node != null);
        if (map != null)
        {
            map.addData(x, y, z, node);
        }
    }

    /**
     * Called to set the data value of the position
     *
     * @param world - location
     * @param pos   - location
     * @param node  - data
     * @return true if the value was set
     */
    public void addData(World world, BlockPos pos, IDataMapNode node)
    {
        DataMap map = getMap(world, node != null);
        if (map != null)
        {
            map.addData(pos, node);
        }
    }

    /**
     * Called to set the data value of the position
     *
     * @param dim  - world id
     * @param pos  - location
     * @param node - data
     */
    public void addData(int dim, BlockPos pos, IDataMapNode node)
    {
        DataMap map = getMap(dim, node != null);
        if (map != null)
        {
            map.addData(pos, node);
        }
    }

    public boolean removeData(World world, BlockPos pos, IDataMapSource source)
    {
        DataMap map = getMap(world, false);
        if (map != null)
        {
            return map.removeData(pos, source);
        }
        return false;
    }

    public boolean removeData(int dim, BlockPos pos, IDataMapSource source)
    {
        DataMap map = getMap(dim, false);
        if (map != null)
        {
            return map.removeData(pos, source);
        }
        return false;
    }

    public boolean removeData(int dim, int x, int y, int z, IDataMapSource source)
    {
        DataMap map = getMap(dim, false);
        if (map != null)
        {
            return map.removeData(x, y, z, source);
        }
        return false;
    }

    public boolean removeData(int dim, int x, int y, int z, IDataMapNode node)
    {
        DataMap map = getMap(dim, false);
        if (map != null)
        {
            return map.removeData(x, y, z, node);
        }
        return false;
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

    public void onWorldTick(World world)
    {
        DataMap map = getMap(world, false);
        if (map != null)
        {
            map.onWorldTick(world);
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
}
