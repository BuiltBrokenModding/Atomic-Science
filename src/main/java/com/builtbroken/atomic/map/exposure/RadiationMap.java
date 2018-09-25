package com.builtbroken.atomic.map.exposure;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.radiation.IRadiationExposureSystem;
import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.data.DataChange;
import com.builtbroken.atomic.map.data.storage.DataMap;
import com.builtbroken.atomic.map.exposure.node.RadSourceEntityItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Handles radiation exposure map. Is not saved and only cached to improve runtime.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2018.
 */
public class RadiationMap implements IRadiationExposureSystem
{
    /** List of radiation sources in the world that are not part of the {@link com.builtbroken.atomic.api.radiation.IRadioactiveMaterialSystem} */
    private List<IRadiationSource> radiationSourceMap = new ArrayList();
    /** Map of entity to its wrapper */
    private HashMap<Entity, IRadiationSource> radiationEntityMap = new HashMap();
    private HashMap<Class<? extends Entity>, Function<Entity, IRadiationSource>> wrapperFactories = new HashMap();

    private int reloadTimer = 0;

    public RadiationMap()
    {
        wrapperFactories.put(EntityItem.class, e -> RadSourceEntityItem.build((EntityItem) e));
    }

    ///----------------------------------------------------------------
    ///-------- Radiation Sources
    ///----------------------------------------------------------------

    /**
     * Called to add source to the map
     * <p>
     * Only call this from the main thread. As the list of sources
     * is iterated at the end of each tick to check for changes.
     *
     * @param source - valid source currently in the world
     */
    public void addSource(IRadiationSource source)
    {
        if (source != null && source.isRadioactive() && !radiationSourceMap.contains(source))
        {
            radiationSourceMap.add(source);
            onSourceAdded(source);
        }
    }

    public void addSource(Entity entity)
    {
        if (entity != null && entity.isEntityAlive() && !radiationEntityMap.containsKey(entity))
        {
            IRadiationSource source = getSource(entity);
            if (source != null)
            {
                addSource(source);
                radiationEntityMap.put(entity, source);
            }
        }
    }

    /**
     * Called to remove a source from the map
     * <p>
     * Only call this from the main thread. As the list of sources
     * is iterated at the end of each tick to check for changes.
     * <p>
     * Only remove if external logic requires it. As the source
     * should return false for {@link IRadiationSource#isRadioactive()}
     * to be automatically removed.
     *
     * @param source - valid source currently in the world
     * @param dead   - is this due to entity death
     */
    public void removeSource(IRadiationSource source, boolean dead)
    {
        if (radiationSourceMap.contains(source))
        {
            //Remove
            radiationSourceMap.remove(source);

            onSourceRemoved(source);
        }
    }

    public void removeSource(Entity entity)
    {
        if (radiationEntityMap.containsKey(entity))
        {
            removeSource(radiationEntityMap.get(entity), entity.isDead);
            radiationEntityMap.remove(entity);
        }
    }

    /**
     * Called when a source is added
     *
     * @param source
     */
    protected void onSourceAdded(IRadiationSource source)
    {
        if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.info("RadiationMap: adding source " + source);
        }

        updateSourceData(source, source.getRadioactiveMaterial());
    }

    /**
     * Called when a source is removed
     *
     * @param source
     */
    protected void onSourceRemoved(IRadiationSource source)
    {
        if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.info("RadiationMap: remove source " + source);
        }
        source.disconnectMapData();
        source.clearMapData();
    }

    /**
     * Called to cleanup invalid sources from the map
     */
    public void clearDeadSources()
    {
        Iterator<IRadiationSource> it = radiationSourceMap.iterator();
        while (it.hasNext())
        {
            IRadiationSource source = it.next();
            if (source == null || !source.isStillValid() || !source.isRadioactive())
            {
                it.remove();
                onSourceRemoved(source);
            }
        }
    }

    /**
     * Called to fire a source change event
     *
     * @param source
     * @param newValue
     */
    protected void updateSourceData(IRadiationSource source, int newValue)
    {
        if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.info("RadiationMap: on changed " + source);
        }
        if (source.isRadioactive() && newValue > 0)
        {
            MapHandler.THREAD_RAD_EXPOSURE.queuePosition(DataChange.get(source, newValue));
        }
    }

    public IRadiationSource getSource(Entity entity)
    {
        if (entity instanceof IRadiationSource)
        {
            return (IRadiationSource) entity;
        }

        Class<? extends Entity> clazz = entity.getClass();
        if (wrapperFactories.containsKey(clazz))
        {
            return wrapperFactories.get(clazz).apply(entity);
        }
        //TODO wrapper IInventory
        return null;
    }

    ///----------------------------------------------------------------
    ///-------- Level Data Accessors
    ///----------------------------------------------------------------

    /**
     * Gets the '(REM) roentgen equivalent man' at the given location
     *
     * @param world - location
     * @param pos   - location
     * @return rem level in mili-rads (1/1000ths of a rem)
     */
    public int getRadLevel(World world, BlockPos pos)
    {
        DataMap map = MapHandler.GLOBAL_DATA_MAP.getMap(world, false);
        if (map != null)
        {
            return map.getValue(pos, DataMapType.RADIATION);
        }
        return 0;
    }

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
        DataMap map = MapHandler.GLOBAL_DATA_MAP.getMap(world, false);
        if (map != null)
        {
            return map.getValue(x, y, z, DataMapType.RADIATION);
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
        value += getRadLevel(entity.world, (int) Math.floor(entity.posX), (int) Math.floor(entity.posY + entity.height), (int) Math.floor(entity.posZ));

        //Mid point
        value += getRadLevel(entity.world, (int) Math.floor(entity.posX), (int) Math.floor(entity.posY + (entity.height / 2)), (int) Math.floor(entity.posZ));

        //Bottom point
        value += getRadLevel(entity.world, (int) Math.floor(entity.posX), (int) Math.floor(entity.posY), (int) Math.floor(entity.posZ));

        //Average TODO build alg to use body size (collision box)
        value /= 3f;

        //Convert from mili rem to rem
        value /= 1000f;

        return value;
    }

    ///----------------------------------------------------------------
    ///--------Edit events
    ///----------------------------------------------------------------

    public void onWorldUnload(World world)
    {
        radiationSourceMap.clear();
        radiationEntityMap.clear();
    }

    /*
    @SubscribeEvent()
    public void onChunkAdded(MapSystemEvent.AddChunk event)
    {
        if (event.world() != null && !!event.world().isRemote && event.map.mapSystem == MapHandler.MATERIAL_MAP)
        {
            MapHandler.THREAD_RAD_EXPOSURE.queueChunkForAddition(event.chunk);
        }
    }

    @SubscribeEvent()
    public void onChunkRemove(MapSystemEvent.RemoveChunk event)
    {
        if (event.world() != null && !!event.world().isRemote && event.map.mapSystem == MapHandler.MATERIAL_MAP)
        {
            MapHandler.THREAD_RAD_EXPOSURE.queueChunkForRemoval(event.chunk);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRadiationChange(MapSystemEvent.UpdateValue event)
    {
        if (event.world() != null && !event.world().isRemote && event.map.mapSystem == MapHandler.MATERIAL_MAP)
        {
            MapHandler.THREAD_RAD_EXPOSURE.queuePosition(DataChange.get(event.dim(), event.x, event.y, event.z, event.prev_value, event.node));
        }
    }
    */

    /*
    @SubscribeEvent()
    public void serverTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            //Cleanup
            clearDeadSources();

            //Loop sources looking for changes
            for (RadSourceWrapper wrapper : radiationSourceMap.values())
            {
                if (wrapper.hasSourceChanged())
                {
                    updateSourceData(wrapper.source, wrapper.source.getRadioactiveMaterial());
                }
            }
        }
    }

    @SubscribeEvent()
    public void itemPickUpEvent(PlayerEvent.ItemPickupEvent event)
    {
        if (!event.player.world.isRemote)
        {
            EntityItem entityItem = event.pickedUp;
            if (entityItem != null)
            {
                ItemStack stack = entityItem.getItem();
                if (stack != null && stack.getItem() instanceof IRadioactiveItem)
                {
                    removeSource(entityItem);
                }
            }
        }
    }

    @SubscribeEvent()
    public void entityJoinWorld(EntityJoinWorldEvent event)
    {
        if (!event.getWorld().isRemote && event.getEntity().isEntityAlive())
        {
            //Add source handles checking if its an actual source
            addSource(event.getEntity());
        }
    }
    */
}
