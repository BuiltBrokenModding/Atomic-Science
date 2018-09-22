package com.builtbroken.atomic.map.data.storage;

import com.builtbroken.atomic.map.MapSystem;
import com.builtbroken.atomic.map.data.node.DataMapType;
import com.builtbroken.atomic.map.data.node.IDataMapNode;
import com.builtbroken.atomic.map.data.node.IDataMapSource;
import com.builtbroken.atomic.map.events.MapSystemEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;

/**
 * Stores a collection of chunks holding data
 * <p>
 * Radiation is not "rad or rem" value, it is how much radioactive material is present at the location. This
 * is used to calculate the rad value that an entity will be exposed to or can be released into the air.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2018.
 */
public class DataMap
{
    public final MapSystem mapSystem;
    public final int dim;

    protected final HashMap<Long, DataChunk> chunksCurrentlyLoaded = new HashMap();
    protected final HashMap<Long, DataChunk> chunksWaitingToUnload = new HashMap();

    public DataMap(MapSystem mapSystem, int dim)
    {
        this.mapSystem = mapSystem;
        this.dim = dim;
    }

    ///----------------------------------------------------------------
    ///-------- Input/Output
    ///----------------------------------------------------------------

    public int getValue(BlockPos pos, DataMapType type)
    {
        return type.getValue(getData(pos));
    }

    public int getValue(int x, int y, int z, DataMapType type)
    {
        return type.getValue(getData(x, y, z));
    }

    public ArrayList<IDataMapNode> getData(BlockPos pos)
    {
        return getData(pos.getX(), pos.getY(), pos.getZ());
    }

    public ArrayList<IDataMapNode> getData(int x, int y, int z)
    {
        DataChunk chunk = getChunkFromPosition(x, z, false);
        if (chunk != null)
        {
            return chunk.getData(x & 15, y, z & 15);
        }
        return null;
    }

    public void addData(BlockPos pos, IDataMapNode node)
    {
        addData(pos.getX(), pos.getY(), pos.getZ(), node);
    }

    public void addData(int x, int y, int z, IDataMapNode node)
    {
        DataChunk chunk = getChunkFromPosition(x, z, node != null);
        if (chunk != null)
        {
            final int prev = node.getType().getValue(getData(x, y, z));

            //Fire change event for modification and to trigger exposure map update
            MapSystemEvent.UpdateValue event = new MapSystemEvent.UpdateValue(this, node.getType(), x, y, z, prev, node); //TODO figure out if we need the block pos
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

    public boolean removeData(BlockPos pos, IDataMapNode node)
    {
        DataChunk chunk = getChunkFromPosition(pos.getX(), pos.getZ(), false);
        if (chunk != null)
        {
            return chunk.removeData(pos.getX() & 15, pos.getY(), pos.getZ() & 15, node);
        }
        return false;
    }

    public boolean removeData(int x, int y, int z, IDataMapNode node)
    {
        DataChunk chunk = getChunkFromPosition(x, z, false);
        if (chunk != null)
        {
            return chunk.removeData(x & 15, y, z & 15, node);
        }
        return false;
    }

    public boolean removeData(BlockPos pos, IDataMapSource source)
    {
        DataChunk chunk = getChunkFromPosition(pos.getX(), pos.getZ(), false);
        if (chunk != null)
        {
            return chunk.removeData(pos.getX() & 15, pos.getY(), pos.getZ() & 15, source);
        }
        return false;
    }

    public boolean removeData(int x, int y, int z, IDataMapSource source)
    {
        DataChunk chunk = getChunkFromPosition(x, z, false);
        if (chunk != null)
        {
            return chunk.removeData(x & 15, y, z & 15, source);
        }
        return false;
    }

    /**
     * Gets the world from the dim manager.
     * <p>
     * Is not cached
     *
     * @return world
     */
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
