package com.builtbroken.atomic.map;

import com.builtbroken.atomic.AtomicScience;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
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

    /** Primary radiation system, handles events and access calls */
    public static final RadiationSystem INSTANCE = new RadiationSystem();

    /** Dimension to radiation material map, saved to world and updated over time */
    protected final HashMap<Integer, RadiationMap> dimensionToMaterialMap = new HashMap();
    /** Dimension to '(REM) roentgen equivalent man' map, not saved and calculated only as needed */
    protected final HashMap<Integer, RadiationMap> dimensionToExposureMap = new HashMap();

    ///----------------------------------------------------------------
    ///-------- Level Data Accessors
    ///----------------------------------------------------------------

    /**
     * Gets the 'radiation absorption dose (RAD)' at the given location
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return rad level
     */
    public int getRadLevel(World world, int x, int y, int z)
    {
        RadiationMap map = getExposureMap(world, false);
        if (map != null)
        {
            return map.getData(x, y, z);
        }
        return 0;
    }

    /**
     * Gets the 'radiation absorption dose (RAD)' at the given location
     *
     * @param entity - used to get location
     * @return rad level
     */
    public int getRadLevel(Entity entity)
    {
        //TODO get average level by getting rad exposure at several locations (use height sliced by 0.5) then averaging
        return getRadLevel(entity.worldObj, (int) Math.floor(entity.posX), (int) Math.floor(entity.posY + (entity.height / 2)), (int) Math.floor(entity.posZ));
    }

    ///----------------------------------------------------------------
    ///-------- Map Accessors
    ///----------------------------------------------------------------

    public RadiationMap getExposureMap(int dim, boolean init)
    {
        RadiationMap map = dimensionToExposureMap.get(dim);
        if (map == null && init)
        {
            map = new RadiationMap(dim);
            dimensionToExposureMap.put(dim, map);
        }
        return map;
    }

    public RadiationMap getExposureMap(World world, boolean init)
    {
        if (world != null && world.provider != null)
        {
            return getExposureMap(world.provider.dimensionId, init);
        }
        return null;
    }

    public RadiationMap getMaterialMap(int dim, boolean init)
    {
        RadiationMap map = dimensionToMaterialMap.get(dim);
        if (map == null && init)
        {
            map = new RadiationMap(dim);
            dimensionToMaterialMap.put(dim, map);
        }
        return map;
    }

    public RadiationMap getMaterialMap(World world, boolean init)
    {
        if (world != null && world.provider != null)
        {
            return getMaterialMap(world.provider.dimensionId, init);
        }
        return null;
    }

    ///----------------------------------------------------------------
    ///--------World events
    ///----------------------------------------------------------------

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        RadiationMap map = getMaterialMap(event.world, false);
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
        RadiationMap map = getMaterialMap(event.world, false);
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
            RadiationMap map = getMaterialMap(event.world, true);
            if (map != null)
            {
                map.loadChunk(event.getChunk(), event.getData());
            }
        }
    }

    @SubscribeEvent
    public void onChunkSaveData(ChunkDataEvent.Save event) //Called on world save
    {
        RadiationMap map = getMaterialMap(event.world, false);
        if (map != null)
        {
            map.saveChunk(event.getChunk(), event.getData());
        }
    }
}
