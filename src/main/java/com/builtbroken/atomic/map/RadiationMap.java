package com.builtbroken.atomic.map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;

/**
 * Stores a collection of chunks holding radiation data
 * <p>
 * Radiation is not "rad or rem" value, it is how much radioactive material is present at the location. This
 * is used to calculate the rad value that an entity will be exposed to or can be released into the air.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2018.
 */
public class RadiationMap
{
    public final int dim;

    protected final HashMap<Long, RadiationChunk> loadedChunks = new HashMap();

    public RadiationMap(int dim)
    {
        this.dim = dim;
    }

    public void onWorldUnload()
    {
        for (long index : loadedChunks.keySet())
        {
            removeChunk(index);
        }
    }

    public void unloadChunk(Chunk chunk)
    {
        removeChunk(index(chunk));
    }

    protected void removeChunk(long index)
    {
        if (loadedChunks.containsKey(index))
        {
            //TODO maybe fire events?
            loadedChunks.remove(index);
        }
    }

    /**
     * Called to save the chunk data
     *
     * @param chunk
     * @param data
     */
    public void saveChunk(Chunk chunk, NBTTagCompound data)
    {
        long index = index(chunk);
        if (loadedChunks.containsKey(index))
        {
            RadiationChunk radiationChunk = loadedChunks.get(index);
            if (radiationChunk != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                radiationChunk.save(tag);
                if (!tag.hasNoTags())
                {
                    data.setTag(RadiationSystem.NBT_CHUNK_DATA, tag);
                }
            }
        }
    }

    /**
     * Called to load the chunk data
     * <p>
     * Will create a new chunk instance if missing
     *
     * @param chunk
     * @param data
     */
    public void loadChunk(Chunk chunk, NBTTagCompound data)
    {
        final long index = index(chunk);

        //Get chunk
        RadiationChunk radiationChunk = null;
        if (loadedChunks.containsKey(index))
        {
            radiationChunk = loadedChunks.get(index);
        }

        //init chunk if missing
        if (radiationChunk == null)
        {
            radiationChunk = new RadiationChunk(chunk.xPosition, chunk.zPosition);
            loadedChunks.put(index, radiationChunk);
        }

        //Load
        radiationChunk.load(data.getCompoundTag(RadiationSystem.NBT_CHUNK_DATA));
    }

    /**
     * Index of the chunk
     *
     * @param chunk
     * @return
     */
    protected long index(Chunk chunk)
    {
        return ChunkCoordIntPair.chunkXZ2Int(chunk.xPosition, chunk.zPosition);
    }
}
