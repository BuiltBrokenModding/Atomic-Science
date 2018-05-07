package com.builtbroken.atomic.map;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.map.thread.ThreadRadExposure;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

/** Core handler for registering maps and triggering events
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/6/2018.
 */
public final class MapHandler
{
    public static final String NBT_RAD_CHUNK = AtomicScience.PREFIX + "radiation_data";
    public static final String NBT_THERMAL_CHUNK = AtomicScience.PREFIX + "thermal";

    /** Handles radiation exposure data storage*/
    public static final RadiationMap RADIATION_MAP = new RadiationMap();
    /** Handles radioactive material data storage */
    public static final RadMaterialMap MATERIAL_MAP = new RadMaterialMap();
    /** Handles radioactive material data storage */
    public static final ThermalMap THERMAL_MAP = new ThermalMap();

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

    }

    ///----------------------------------------------------------------
    ///-------Chunk Events
    ///----------------------------------------------------------------

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) //Only called if chunk unloads separate from world unload
    {

    }

    @SubscribeEvent
    public void onChunkLoadData(ChunkDataEvent.Load event) //Called before chunk load event
    {

    }

    @SubscribeEvent
    public void onChunkSaveData(ChunkDataEvent.Save event) //Called on world save
    {

    }
}
