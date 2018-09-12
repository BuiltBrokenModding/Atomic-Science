package com.builtbroken.atomic.map;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.config.ConfigRadiation;
import com.builtbroken.atomic.map.exposure.RadiationMap;
import com.builtbroken.atomic.map.exposure.ThreadRadExposure;
import com.builtbroken.atomic.map.thermal.ThermalMap;
import com.builtbroken.atomic.map.thermal.ThreadThermalAction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
    /** Thread used to move heat around the map */
    public static ThreadThermalAction THREAD_THERMAL_ACTION;

    public static final MapHandler INSTANCE = new MapHandler();


    public static void register()
    {
        AtomicScienceAPI.radiationExposureSystem = RADIATION_MAP;
        AtomicScienceAPI.radioactiveMaterialSystem = MATERIAL_MAP;
        AtomicScienceAPI.thermalSystem = THERMAL_MAP;

        MinecraftForge.EVENT_BUS.register(INSTANCE);

        if(ConfigRadiation.ENABLE_MAP)
        {
            MinecraftForge.EVENT_BUS.register(RADIATION_MAP);
        }

        MinecraftForge.EVENT_BUS.register(THERMAL_MAP);
    }

    ///----------------------------------------------------------------
    ///--------World events
    ///----------------------------------------------------------------

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        RADIATION_MAP.onWorldUnload(event.getWorld());
        MATERIAL_MAP.onWorldUnload(event.getWorld());
        THERMAL_MAP.onWorldUnload(event.getWorld());
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        RADIATION_MAP.onWorldTick(event.world);
        MATERIAL_MAP.onWorldTick(event.world);
        THERMAL_MAP.onWorldTick(event.world);
    }

    ///----------------------------------------------------------------
    ///-------Chunk Events
    ///----------------------------------------------------------------

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) //Only called if chunk unloads separate from world unload
    {
        RADIATION_MAP.onChunkUnload(event.getWorld(), event.getChunk());
        MATERIAL_MAP.onChunkUnload(event.getWorld(), event.getChunk());
        THERMAL_MAP.onChunkUnload(event.getWorld(), event.getChunk());
    }

    @SubscribeEvent
    public void onChunkLoadData(ChunkDataEvent.Load event) //Called before chunk load event
    {
        RADIATION_MAP.onChunkLoadData(event.getWorld(), event.getChunk(), event.getData());
        MATERIAL_MAP.onChunkLoadData(event.getWorld(), event.getChunk(), event.getData());
        THERMAL_MAP.onChunkLoadData(event.getWorld(), event.getChunk(), event.getData());
    }

    @SubscribeEvent
    public void onChunkSaveData(ChunkDataEvent.Save event) //Called on world save
    {
        RADIATION_MAP.onChunkSaveData(event.getWorld(), event.getChunk(), event.getData());
        MATERIAL_MAP.onChunkSaveData(event.getWorld(), event.getChunk(), event.getData());
        THERMAL_MAP.onChunkSaveData(event.getWorld(), event.getChunk(), event.getData());
    }
}
