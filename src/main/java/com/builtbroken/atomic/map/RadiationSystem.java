package com.builtbroken.atomic.map;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.map.events.RadiationMapEvent;
import com.builtbroken.atomic.map.thread.RadChange;
import com.builtbroken.atomic.map.thread.ThreadRadExposure;
import cpw.mods.fml.common.eventhandler.EventPriority;
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
    /** Thread used to calculate exposure values per location */
    public static final ThreadRadExposure THREAD_RAD_EXPOSURE = new ThreadRadExposure();

    /** Dimension to radiation material map, saved to world and updated over time */
    protected final HashMap<Integer, RadiationMap> dimensionToMaterialMap = new HashMap();
    /** Dimension to '(REM) roentgen equivalent man' map, not saved and calculated only as needed */
    protected final HashMap<Integer, RadiationMap> dimensionToExposureMap = new HashMap();

    ///----------------------------------------------------------------
    ///-------- Level Data Accessors
    ///----------------------------------------------------------------

    /**
     * Gets the '(REM) roentgen equivalent man' at the given location
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return rem level in mili-rads (1/1000ths of a rem)
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
     * Gets the REM exposure for the entity
     *
     * @param entity - entity, will use the entity size to get an average value
     * @return REM value
     */
    public float getRemExposure(Entity entity)
    {
        float value = 0;

        //Top point
        value += getRadLevel(entity.worldObj, (int) Math.floor(entity.posX), (int) Math.floor(entity.posY + entity.height), (int) Math.floor(entity.posZ));

        //Mid point
        value += getRadLevel(entity.worldObj, (int) Math.floor(entity.posX), (int) Math.floor(entity.posY + (entity.height / 2)), (int) Math.floor(entity.posZ));

        //Bottom point
        value += getRadLevel(entity.worldObj, (int) Math.floor(entity.posX), (int) Math.floor(entity.posY), (int) Math.floor(entity.posZ));

        //Average TODO build alg to use body size (collision box)
        value /= 3;

        return value;
    }

    /**
     * Gets the amount of radioactive material that is present at the location
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return radioactive material amount
     */
    public int getRadioactiveMaterial(World world, int x, int y, int z)
    {
        RadiationMap map = getMaterialMap(world, false);
        if (map != null)
        {
            return map.getData(x, y, z);
        }
        return 0;
    }

    /**
     * Gets the amount of radioactive material that is present at the location
     *
     * @param dim - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return radioactive material amount
     */
    public int getRadioactiveMaterial(int dim, int x, int y, int z)
    {
        RadiationMap map = getMaterialMap(dim, false);
        if (map != null)
        {
            return map.getData(x, y, z);
        }
        return 0;
    }

    /**
     * Called to set the radioactive material amount of the position
     *
     * @param world  - location
     * @param x      - location
     * @param y      - location
     * @param z      - location
     * @param amount - weight of material
     * @return true if the value was set
     */
    public boolean setRadioactiveMaterial(World world, int x, int y, int z, int amount)
    {
        RadiationMap map = getMaterialMap(world, amount > 0);
        if (map != null)
        {
            return map.setData(x, y, z, amount);
        }
        return true;
    }

    /**
     * Called to set the radioactive material amount of the position
     *
     * @param dim  - location
     * @param x      - location
     * @param y      - location
     * @param z      - location
     * @param amount - weight of material
     * @return true if the value was set
     */
    public boolean setRadioactiveMaterial(int dim, int x, int y, int z, int amount)
    {
        RadiationMap map = getMaterialMap(dim, amount > 0);
        if (map != null)
        {
            return map.setData(x, y, z, amount);
        }
        return true;
    }

    ///----------------------------------------------------------------
    ///-------- Map Accessors
    ///----------------------------------------------------------------

    /**
     * Gets the exposure map
     * <p>
     * Make sure all changes to the map are thread safe. As this is
     * heavily accessed by the thread system. Changing values while
     * the thread is running can cause the system to break.
     *
     * @param dim  - dimension of the map to get
     * @param init - true to generate the map
     * @return map, or null if it was never created
     */
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

    /**
     * Gets the exposure map
     * <p>
     * Make sure all changes to the map are thread safe. As this is
     * heavily accessed by the thread system. Changing values while
     * the thread is running can cause the system to break.
     *
     * @param world - dimension of the map to get
     * @param init  - true to generate the map
     * @return map, or null if it was never created
     */
    public RadiationMap getExposureMap(World world, boolean init)
    {
        if (world != null && world.provider != null)
        {
            return getExposureMap(world.provider.dimensionId, init);
        }
        return null;
    }

    /**
     * Gets the map of radioactive material per positon. Is used
     * to calculate exposure and other effects.
     *
     * @param dim  - dimension of the map to get
     * @param init - true to generate the map
     * @return map, or null if it was never created
     */
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

    /**
     * Gets the map of radioactive material per positon. Is used
     * to calculate exposure and other effects.
     *
     * @param world - dimension of the map to get
     * @param init  - true to generate the map
     * @return map, or null if it was never created
     */
    public RadiationMap getMaterialMap(World world, boolean init)
    {
        if (world != null && world.provider != null)
        {
            return getMaterialMap(world.provider.dimensionId, init);
        }
        return null;
    }

    ///----------------------------------------------------------------
    ///--------Edit events
    ///----------------------------------------------------------------

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRadiationChange(RadiationMapEvent.UpdateRadiationMaterial event)
    {
        if (event.prev_value != event.new_value)
        {
            THREAD_RAD_EXPOSURE.changeQueue.add(new RadChange(event.dim(), event.x, event.y, event.z, event.prev_value, event.new_value));
        }
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
