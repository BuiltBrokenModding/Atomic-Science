package com.builtbroken.atomic.map.data;

import com.builtbroken.atomic.map.MapSystem;
import com.builtbroken.atomic.map.events.MapSystemEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;

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

    protected final HashMap<Long, DataChunk> loadedChunks = new HashMap();

    public DataMap(MapSystem mapSystem, int dim)
    {
        this.mapSystem = mapSystem;
        this.dim = dim;
    }

    ///----------------------------------------------------------------
    ///-------- Input/Output
    ///----------------------------------------------------------------

    public int getData(int x, int y, int z)
    {
        DataChunk chunk = getChunkFromPosition(x, z, false);
        if (chunk != null)
        {
            return chunk.getValue(x & 15, y, z & 15);
        }
        return 0;
    }

    public boolean setData(int x, int y, int z, int amount)
    {
        DataChunk chunk = getChunkFromPosition(x, z, amount > 0);
        if (chunk != null)
        {
            final int prev_value = getData(x, y, z);

            //Fire change event for modification and to trigger exposure map update
            MapSystemEvent.UpdateValue event = new MapSystemEvent.UpdateValue(this, x, y, z, prev_value, amount);
            if (MinecraftForge.EVENT_BUS.post(event))
            {
                return false;
            }
            amount = event.new_value;

            //set value
            boolean hasChanged = chunk.setValue(x & 15, y, z & 15, amount);

            //if changed mark chunk so it saves
            if (hasChanged)
            {
                World world = DimensionManager.getWorld(dim);
                if (world != null)
                {
                    Chunk worldChunk = world.getChunkFromBlockCoords(x, z);
                    if (worldChunk != null)
                    {
                        worldChunk.setChunkModified();
                    }
                }
            }
            return hasChanged;
        }
        return true;
    }

    ///----------------------------------------------------------------
    ///-------- Event handlers
    ///----------------------------------------------------------------

    public void onWorldUnload()
    {
        loadedChunks.clear();
    }

    public void unloadChunk(Chunk chunk)
    {
        removeChunk(index(chunk));
    }

    protected void removeChunk(long index)
    {
        if (loadedChunks.containsKey(index))
        {
            //Trigger event
            DataChunk chunk = loadedChunks.get(index);
            if (chunk != null)
            {
                MinecraftForge.EVENT_BUS.post(new MapSystemEvent.RemoveChunk(this, chunk));
            }

            //Remove chunk
            loadedChunks.remove(index);
        }
    }

    /**
     * Called to save the chunk data.
     * <p>
     * Data provides should be an empty tag. Does not include
     * all data for the chunk.
     *
     * @param chunk
     * @param data  - tag to save directly two
     */
    public void saveChunk(Chunk chunk, NBTTagCompound data)
    {
        long index = index(chunk);
        if (loadedChunks.containsKey(index))
        {
            DataChunk radiationChunk = loadedChunks.get(index);
            if (radiationChunk != null)
            {
                radiationChunk.save(data);
            }
        }
    }

    /**
     * Called to load the chunk data
     * <p>
     * Will create a new chunk instance if missing
     * <p>
     * Data provides is unique to this chunk and is not
     * the entire save data for the game's map.
     *
     * @param chunk
     * @param data  - data to load
     */
    public void loadChunk(Chunk chunk, NBTTagCompound data)
    {
        final long index = index(chunk);

        //Get chunk
        DataChunk radiationChunk = null;
        if (loadedChunks.containsKey(index))
        {
            radiationChunk = loadedChunks.get(index);
        }

        //init chunk if missing
        if (radiationChunk == null)
        {
            radiationChunk = createNewChunk(chunk.worldObj.provider.dimensionId, chunk.zPosition, chunk.xPosition);
        }

        //Load
        radiationChunk.load(data);

        //Trigger event
        if (radiationChunk != null)
        {
            MinecraftForge.EVENT_BUS.post(new MapSystemEvent.AddChunk(this, radiationChunk));
        }
    }

    protected DataChunk createNewChunk(int dim, int chunkX, int chunkZ)
    {
        long index = index(chunkX, chunkZ);
        DataChunk radiationChunk = new DataChunk(dim, chunkX, chunkZ);
        loadedChunks.put(index, radiationChunk);
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
        long index = ChunkCoordIntPair.chunkXZ2Int(chunk_x, chunk_z);
        DataChunk chunk = loadedChunks.get(index);
        if (chunk == null && init)
        {
            chunk = new DataChunk(dim, chunk_x, chunk_z);
            loadedChunks.put(index, chunk);
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
        return index(chunk.xPosition, chunk.zPosition);
    }

    protected long index(int chunkX, int chunkZ)
    {
        return ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
    }
}
