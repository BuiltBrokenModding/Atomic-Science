package com.builtbroken.atomic.map;

import com.builtbroken.atomic.AtomicScience;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2018.
 */
public class RadiationSystem
{
    public static final String NBT_CHUNK_DATA = AtomicScience.PREFIX + "radiation_data";

    public static final RadiationSystem INSTANCE = new RadiationSystem();

    protected final HashMap<Integer, RadiationMap> dimensionToMap = new HashMap();

    public RadiationMap getMap(int dim, boolean init)
    {
        RadiationMap map = dimensionToMap.get(dim);
        if (map == null && init)
        {
            map = new RadiationMap(dim);
            dimensionToMap.put(dim, map);
        }
        return map;
    }

    public RadiationMap getMap(World world, boolean init)
    {
        if (world != null && world.provider != null)
        {
            return getMap(world.provider.dimensionId, init);
        }
        return null;
    }

    ///----------------------------------------------------------------
    ///--------World events
    ///----------------------------------------------------------------

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        RadiationMap map = getMap(event.world, false);
        if (map != null)
        {
            map.onWorldUnload();
        }
    }

    //@SubscribeEvent
    public void onWorldSave(WorldEvent.Save event)
    {
        System.out.println(String.format("World[%s] -> save", event.world.provider.dimensionId));
    }

    //@SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        System.out.println(String.format("World[%s] -> load", event.world.provider.dimensionId));
    }

    ///----------------------------------------------------------------
    ///-------Chunk Events
    ///----------------------------------------------------------------

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) //Only called if chunk unloads separate from world unload
    {
        RadiationMap map = getMap(event.world, false);
        if (map != null)
        {
            map.unloadChunk(event.getChunk());
        }
    }

    @SubscribeEvent
    public void onChunkLoadData(ChunkDataEvent.Load event) //Called before chunk load event
    {
        if (event.getData() != null && event.getData().hasKey(NBT_CHUNK_DATA))
        {
            RadiationMap map = getMap(event.world, true);
            if (map != null)
            {
                map.loadChunk(event.getChunk(), event.getData());
            }
        }
    }

    @SubscribeEvent
    public void onChunkSaveData(ChunkDataEvent.Save event) //Called on world save
    {
        RadiationMap map = getMap(event.world, false);
        if (map != null)
        {
            map.saveChunk(event.getChunk(), event.getData());
        }
    }
}
