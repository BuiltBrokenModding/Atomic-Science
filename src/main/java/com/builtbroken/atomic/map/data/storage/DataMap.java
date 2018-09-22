package com.builtbroken.atomic.map.data.storage;

import com.builtbroken.atomic.map.MapSystem;
import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.map.IDataMapNode;
import com.builtbroken.atomic.api.map.IDataMapSource;
import com.builtbroken.atomic.map.events.MapSystemEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Stores a map of {@link DataChunk} holding {@link IDataMapNode} from {@link IDataMapSource}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2018.
 */
public class DataMap
{
    public final MapSystem mapSystem;
    public final int dim;

    /** Chunks currently loaded and actively being used */
    protected final HashMap<Long, DataChunk> chunksCurrentlyLoaded = new HashMap();
    /** Chunks waiting to be unloaded from the map. Delayed to reduce memory churn and chunk loading spam */
    protected final HashMap<Long, DataChunk> chunksWaitingToUnload = new HashMap();

    public DataMap(MapSystem mapSystem, int dim)
    {
        this.mapSystem = mapSystem;
        this.dim = dim;
    }

    ///----------------------------------------------------------------
    ///-------- Input/Output
    ///----------------------------------------------------------------

    /**
     * Called to the value of the stored nodes
     *
     * @param pos  - location in world
     * @param type - used to selectively value the nodes
     * @return list of all nodes regardless of type
     */
    public int getValue(@Nonnull BlockPos pos, @Nonnull DataMapType type)
    {
        return type.getValue(getData(pos));
    }

    /**
     * Called to the value of the stored nodes
     *
     * @param x    - location in world
     * @param y    - location in world
     * @param z    - location in world
     * @param type - used to selectively value the nodes
     * @return list of all nodes regardless of type
     */
    public int getValue(int x, int y, int z, @Nonnull DataMapType type)
    {
        return type.getValue(getData(x, y, z));
    }

    /**
     * Called to get all node stored for the location
     *
     * @param pos - location in world
     * @return list of all nodes regardless of type
     */
    @Nullable
    public ArrayList<IDataMapNode> getData(@Nonnull BlockPos pos)
    {
        return getData(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Called to get all node stored for the location
     *
     * @param x - location in world
     * @param y - location in world
     * @param z - location in world
     * @return list of all nodes regardless of type
     */
    @Nullable
    public ArrayList<IDataMapNode> getData(int x, int y, int z)
    {
        DataChunk chunk = getChunkFromPosition(x, z, false);
        if (chunk != null)
        {
            return chunk.getData(x & 15, y, z & 15);
        }
        return null;
    }

    /**
     * Called to add a node to the world
     * <p>
     * Will fire an event if value changes in map
     *
     * @param pos- location in world
     * @param node - data point to add
     */
    public void addData(@Nonnull BlockPos pos, @Nonnull IDataMapNode node)
    {
        addData(pos.getX(), pos.getY(), pos.getZ(), node);
    }

    /**
     * Called to add a node to the world
     * <p>
     * Will fire an event if value changes in map
     *
     * @param x    - location in world
     * @param y    - location in world
     * @param z    - location in world
     * @param node - data point to add
     */
    public void addData(int x, int y, int z, @Nonnull IDataMapNode node)
    {
        DataChunk chunk = getChunkFromPosition(x, z, true);
        if (chunk != null)
        {
            final int prev = node.getType().getValue(getData(x, y, z));

            //Fire change event for modification and to trigger exposure map update
            MapSystemEvent.OnNodeAdded event = new MapSystemEvent.OnNodeAdded(this, node.getType(), x, y, z, prev, node); //TODO figure out if we need the block pos
            if (MinecraftForge.EVENT_BUS.post(event) || event.node == null)
            {
                return;
            }

            //Add node
            chunk.addData(x & 15, y, z & 15, event.node);

            //if changed mark chunk so it saves
            World world = DimensionManager.getWorld(dim);
            if (world != null)
            {
                Chunk worldChunk = world.getChunk(x >> 4, z >> 4);
                if (worldChunk != null)
                {
                    worldChunk.setModified(true);
                }
            }
        }
    }

    /**
     * Called to remove all a single node
     * <p>
     * Will fire an event if value changes in map
     *
     * @param pos  - location in world
     * @param node - data point to remove
     * @return true if any nodes were removed
     */
    public boolean removeData(@Nonnull BlockPos pos, @Nonnull IDataMapNode node)
    {
        return removeData(pos.getX(), pos.getY(), pos.getZ(), node);
    }

    /**
     * Called to remove all a single node
     * <p>
     * Will fire an event if value changes in map
     *
     * @param x    - location in world
     * @param y    - location in world
     * @param z    - location in world
     * @param node - data point to remove
     * @return true if any nodes were removed
     */
    public boolean removeData(int x, int y, int z, @Nonnull IDataMapNode node)
    {
        if (node != null)
        {
            DataChunk chunk = getChunkFromPosition(x, z, false);
            if (chunk != null)
            {
                int prev = node.getType().getValue(getData(x, y, z));
                if (chunk.removeData(x & 15, y, z & 15, node))
                {
                    fireChangeEvent(node.getType(), x, y, z, prev);
                }
            }
        }
        return false;
    }

    /**
     * Called to remove all nodes linked to the source.
     * <p>
     * Will fire an event if value changes in map
     *
     * @param pos    - location in world
     * @param source - source to use to ID nodes
     * @return true if any nodes were removed
     */
    public boolean removeData(@Nonnull BlockPos pos, @Nonnull IDataMapSource source)
    {
        return removeData(pos.getX(), pos.getY(), pos.getZ(), source);
    }

    /**
     * Called to remove all nodes linked to the source.
     * <p>
     * Will fire an event if value changes in map
     *
     * @param x      - location in world
     * @param y      - location in world
     * @param z      - location in world
     * @param source - source to use to ID nodes
     * @return true if any nodes were removed
     */
    public boolean removeData(int x, int y, int z, @Nonnull IDataMapSource source)
    {
        DataChunk chunk = getChunkFromPosition(x, z, false);
        if (chunk != null)
        {
            int prev = source.getType().getValue(getData(x, y, z));
            if (chunk.removeData(x & 15, y, z & 15, source))
            {
                fireChangeEvent(source.getType(), x, y, z, prev);
            }
        }
        return false;
    }

    private final void fireChangeEvent(@Nonnull DataMapType type, int x, int y, int z, int prev)
    {
        int current = type.getValue(getData(x, y, z));
        if (prev != current)
        {
            MinecraftForge.EVENT_BUS.post(new MapSystemEvent.OnNodeRemoved(this, type, x, y, z, prev, current));
        }
    }

    /**
     * Gets the world from the dim manager.
     * <p>
     * Is not cached
     *
     * @return world
     */
    @Nullable
    public World getWorld()
    {
        return DimensionManager.getWorld(dim);
    }

    /**
     * Gets all loaded chunks
     * <p>
     * Do not edit list
     *
     * @return list of chunks
     */
    public Collection<DataChunk> getLoadedChunks()
    {
        return chunksCurrentlyLoaded.values();
    }

    ///----------------------------------------------------------------
    ///-------- Event handlers
    ///----------------------------------------------------------------

    public void onWorldUnload()
    {
        clearData();
    }

    public void clearData()
    {
        chunksCurrentlyLoaded.clear();
    }

    public void onWorldTick(World world)
    {
        Iterator<Map.Entry<Long, DataChunk>> it = chunksWaitingToUnload.entrySet().iterator();
        while (it.hasNext())
        {
            final Map.Entry<Long, DataChunk> entry = it.next();
            final DataChunk chunk = entry.getValue();

            //Should not happen but could
            if (chunk == null)
            {
                it.remove();
            }
            //Chunk is loaded, so we can re-add data
            else if (world.getChunkProvider().getLoadedChunk(chunk.xPosition, chunk.zPosition) != null)
            {
                chunksCurrentlyLoaded.put(entry.getKey(), chunk);
                it.remove();
            }
            //Delay chunk remove to give a chance for chunk to reload
            else if (entry.getValue().unloadTick++ > 1000) //TODO move to config
            {
                MinecraftForge.EVENT_BUS.post(new MapSystemEvent.RemoveChunk(this, entry.getValue()));
                it.remove();
            }
        }
    }

    public void unloadChunk(Chunk chunk)
    {
        long index = index(chunk);
        if (chunksCurrentlyLoaded.containsKey(index))
        {
            chunksWaitingToUnload.put(index, chunksCurrentlyLoaded.get(index));
            chunksCurrentlyLoaded.remove(index);
        }
    }

    protected DataChunk createNewChunk(int dim, int chunkX, int chunkZ)
    {
        long index = index(chunkX, chunkZ);
        DataChunk radiationChunk = new DataChunk(dim, chunkX, chunkZ);
        chunksCurrentlyLoaded.put(index, radiationChunk);
        return radiationChunk;
    }

    ///----------------------------------------------------------------
    ///-------- Helpers
    ///----------------------------------------------------------------

    public DataChunk getChunkFromPosition(int x, int z, boolean init)
    {
        return getChunk(x >> 4, z >> 4, init);
    }

    public DataChunk getChunk(int chunk_x, int chunk_z, boolean init)
    {
        DataChunk chunk = findChunk(chunk_x, chunk_z, init);
        if (chunk == null && init)
        {
            chunk = createNewChunk(dim, chunk_x, chunk_z);
        }
        return chunk;
    }

    protected DataChunk findChunk(int chunk_x, int chunk_z, boolean load)
    {
        return findChunk(index(chunk_x, chunk_z), load);
    }

    protected DataChunk findChunk(long index, boolean load)
    {
        DataChunk chunk = chunksCurrentlyLoaded.get(index);
        if (chunk == null && load)
        {
            chunk = chunksWaitingToUnload.get(index);
            if (chunk != null)
            {
                chunksWaitingToUnload.remove(index);
                chunksCurrentlyLoaded.put(index, chunk);
            }
        }
        return chunk;
    }

    /**
     * Index of the chunk
     *
     * @param chunk
     * @return
     */
    protected long index(Chunk chunk)
    {
        return index(chunk.x, chunk.z);
    }

    protected long index(int chunkX, int chunkZ)
    {
        return ChunkPos.asLong(chunkX, chunkZ);
    }
}
