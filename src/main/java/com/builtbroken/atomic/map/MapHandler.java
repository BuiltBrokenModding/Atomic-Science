package com.builtbroken.atomic.map;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.map.thread.ThreadRadExposure;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Core handler for registering maps and triggering events
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/6/2018.
 */
public final class MapHandler
{
    public static final String RAD_EXPOSURE_MAP_ID = AtomicScience.PREFIX + "radiation_exposure";
    public static final String RAD_MATERIAL_MAP_ID = AtomicScience.PREFIX + "radiation_material";
    public static final String THERMAL_MAP_ID = AtomicScience.PREFIX + "thermal";

    public static final String NBT_RAD_CHUNK = AtomicScience.PREFIX + "radiation_data";
    public static final String NBT_THERMAL_CHUNK = AtomicScience.PREFIX + "thermal";

    /** Handles radiation exposure data storage */
    public static final RadiationMap RADIATION_MAP = new RadiationMap(); //TODO expose to API
    /** Handles radioactive material data storage */
    public static final RadMaterialMap MATERIAL_MAP = new RadMaterialMap(); //TODO expose to API
    /** Handles radioactive material data storage */
    public static final ThermalMap THERMAL_MAP = new ThermalMap(); //TODO expose to API

    /** Thread used to calculate exposure values per location */
    public static ThreadRadExposure THREAD_RAD_EXPOSURE;


    public static void register()
    {
        MinecraftForge.EVENT_BUS.register(new MapHandler());
        MinecraftForge.EVENT_BUS.register(RADIATION_MAP);
    }

    ///----------------------------------------------------------------
    ///--------World events
    ///----------------------------------------------------------------

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        RADIATION_MAP.onWorldUnload(event.world);
        MATERIAL_MAP.onWorldUnload(event.world);
        THERMAL_MAP.onWorldUnload(event.world);
    }

    ///----------------------------------------------------------------
    ///-------Chunk Events
    ///----------------------------------------------------------------

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) //Only called if chunk unloads separate from world unload
    {
        RADIATION_MAP.onChunkUnload(event.world, event.getChunk());
        MATERIAL_MAP.onChunkUnload(event.world, event.getChunk());
        THERMAL_MAP.onChunkUnload(event.world, event.getChunk());
    }

    @SubscribeEvent
    public void onChunkLoadData(ChunkDataEvent.Load event) //Called before chunk load event
    {
        RADIATION_MAP.onChunkLoadData(event.world, event.getChunk(), event.getData());
        MATERIAL_MAP.onChunkLoadData(event.world, event.getChunk(), event.getData());
        THERMAL_MAP.onChunkLoadData(event.world, event.getChunk(), event.getData());
    }

    @SubscribeEvent
    public void onChunkSaveData(ChunkDataEvent.Save event) //Called on world save
    {
        RADIATION_MAP.onChunkSaveData(event.world, event.getChunk(), event.getData());
        MATERIAL_MAP.onChunkSaveData(event.world, event.getChunk(), event.getData());
        THERMAL_MAP.onChunkSaveData(event.world, event.getChunk(), event.getData());
    }
}
