package com.builtbroken.atomic.map.thermal;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.thermal.IHeatSource;
import com.builtbroken.atomic.api.thermal.IThermalSystem;
import com.builtbroken.atomic.lib.MassHandler;
import com.builtbroken.atomic.lib.network.netty.PacketSystem;
import com.builtbroken.atomic.lib.network.packet.client.PacketSpawnParticle;
import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.MapSystem;
import com.builtbroken.atomic.map.data.DataChange;
import com.builtbroken.atomic.map.data.DataMap;
import com.builtbroken.atomic.map.data.DataPos;
import com.builtbroken.atomic.map.events.MapSystemEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Handles heat in the map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/6/2018.
 */
public class ThermalMap extends MapSystem implements IThermalSystem
{
    /** List of thermal sources in the world */
    private HashMap<IHeatSource, ThermalSourceWrapper> thermalSourceMap = new HashMap();
    private HashMap<Integer, HashSet<DataPos>> steamSources = new HashMap();

    public ThermalMap()
    {
        super(MapHandler.THERMAL_MAP_ID, null);
    }

    /**
     * Called to add source to the map
     * <p>
     * Only call this from the main thread. As the list of sources
     * is iterated at the end of each tick to check for changes.
     *
     * @param source - valid source currently in the world
     */
    public void addSource(IHeatSource source)
    {
        if (source != null && source.canGeneratingHeat() && !thermalSourceMap.containsKey(source))
        {
            thermalSourceMap.put(source, new ThermalSourceWrapper(source));
            onSourceAdded(source);
        }
    }

    /**
     * Called to remove a source from the map
     * <p>
     * Only call this from the main thread. As the list of sources
     * is iterated at the end of each tick to check for changes.
     * <p>
     * Only remove if external logic requires it. As the source
     * should return false for {@link IHeatSource#canGeneratingHeat()}
     * to be automatically removed.
     *
     * @param source - valid source currently in the world
     */
    public void removeSource(IHeatSource source)
    {
        if (thermalSourceMap.containsKey(source))
        {
            onSourceRemoved(source);
            thermalSourceMap.remove(source);
        }
    }

    /**
     * Called when a source is added
     *
     * @param source
     */
    protected void onSourceAdded(IHeatSource source)
    {
        if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.info("ThermalMap: adding source " + source);
        }
        fireSourceChange(source, source.getHeatGenerated());
    }

    /**
     * Called when a source is removed
     *
     * @param source
     */
    protected void onSourceRemoved(IHeatSource source)
    {
        if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.info("ThermalMap: remove source " + source);
        }
        fireSourceChange(source, 0);
    }

    /**
     * Called to cleanup invalid sources from the map
     */
    public void clearDeadSources()
    {
        Iterator<Map.Entry<IHeatSource, ThermalSourceWrapper>> it = thermalSourceMap.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<IHeatSource, ThermalSourceWrapper> next = it.next();
            if (next == null || next.getKey() == null || next.getValue() == null || !next.getKey().canGeneratingHeat())
            {
                if (next.getKey() != null)
                {
                    onSourceRemoved(next.getKey());
                }
                it.remove();
            }
        }
    }

    /**
     * Called to fire a source change event
     *
     * @param source
     * @param newValue
     */
    protected void fireSourceChange(IHeatSource source, int newValue)
    {
        if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.info("ThermalMap: on changed " + source);
        }
        ThermalSourceWrapper wrapper = getRadSourceWrapper(source);
        if (wrapper != null && wrapper.heatValue != newValue)
        {
            //Remove old, called separate in case position changed
            if (wrapper.heatValue != 0)
            {
                MapHandler.THREAD_THERMAL_ACTION.queuePosition(DataChange.get(wrapper.dim, wrapper.xi(), wrapper.yi(), wrapper.zi(), wrapper.heatValue, 0));
            }
            //Log changes
            wrapper.logCurrentData();

            //Add new, called separate in case position changed
            if (newValue != 0 && source.canGeneratingHeat())
            {
                MapHandler.THREAD_THERMAL_ACTION.queuePosition(DataChange.get(wrapper.dim, wrapper.xi(), wrapper.yi(), wrapper.zi(), 0, newValue));
            }
        }
    }

    protected ThermalSourceWrapper getRadSourceWrapper(IHeatSource source)
    {
        if (thermalSourceMap.containsKey(source))
        {
            ThermalSourceWrapper wrapper = thermalSourceMap.get(source);
            if (wrapper == null)
            {
                thermalSourceMap.put(source, wrapper = new ThermalSourceWrapper(source));
            }
            return wrapper;
        }
        return null;
    }

    /**
     * Energy in joules
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public long getJoules(World world, int x, int y, int z)
    {
        return getData(world, x, y, z) * 1000L; //Map stores heat in kilo-joules
    }

    public long getActualJoules(World world, int x, int y, int z)
    {
        return getJoules(world, x, y, z) + getEnvironmentalJoules(world, x, y, z);
    }

    /**
     * Gets the temperature of the block at the location
     * <p>
     * Uses the properties of the block to calculate the value from
     * the heat + environmental values.
     *
     * @param world - map to pull data from
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return temperature in Kelvin
     */
    public double getTemperature(World world, int x, int y, int z)
    {
        return getTemperature(world, x, y, z, getActualJoules(world, x, y, z));
    }

    /**
     * Gets the temperature of the block at the location
     * <p>
     * Uses the properties of the block to calculate the value from
     * the heat + environmental values.
     *
     * @param world  - map to pull data from
     * @param x      - location
     * @param y      - location
     * @param z      - location
     * @param joules - heat energy
     * @return temperature in Kelvin
     */
    public double getTemperature(World world, int x, int y, int z, double joules)
    {
        return joules / (MassHandler.getMass(world, x, y, z) * ThermalHandler.getSpecificHeat(world, x, y, z) * 1000);
    }

    /**
     * Gets the different in temperature between two locations
     *
     * @param world - map to pull data from
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @param i     - location 2
     * @param j     - location 2
     * @param k     - location 2
     * @return temperature in Kelvin
     */
    public double getTemperatureDelta(World world, int x, int y, int z, int i, int j, int k)
    {
        return getTemperature(world, x, y, z) - getTemperature(world, i, j, k);
    }

    /**
     * Gets the amount of heat energy naturally present in the block due to environmental values
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return energy in joules
     */
    public long getEnvironmentalJoules(World world, int x, int y, int z)
    {
        return ((long) Math.floor(getEnvironmentalTemperature(world, x, y, z) * MassHandler.getMass(world, x, y, z) * ThermalHandler.getSpecificHeat(world, x, y, z))) * 1000L; //x1000 for kj -> j
    }

    /**
     * Gets the natural resting temperature of the environment
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return temperature in kelvin
     */
    public double getEnvironmentalTemperature(World world, int x, int y, int z)
    {
        return 290; //Slightly under room temp
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHeatChanged(MapSystemEvent.UpdateValue event)
    {
        World world = event.world();
        DataMap map = getMap(world, false);
        if (world != null && map != null && map.blockExists(event.x, event.y, event.z))
        {
            checkForThermalChange(world, event.x, event.y, event.z, event.new_value);
        }
    }

    @SubscribeEvent
    public void onBlockPlaced(PlayerInteractEvent event)
    {
        if (!event.world.isRemote && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
        {
            checkForThermalChange(event.world, event.x, event.y, event.z, getData(event.world, event.x, event.y, event.z));
        }
    }

    protected void checkForThermalChange(World world, int x, int y, int z, int heat)
    {
        if (ThermalHandler.canChangeStates(world, x, y, z))
        {
            long joules = heat * 1000 + getEnvironmentalJoules(world, x, y, z); //x1000 for kj -> j
            if (joules > ThermalHandler.energyCostToChangeStates(world, x, y, z))
            {
                ThermalHandler.changeStates(world, x, y, z);
            }
        }

        int vap = ThermalHandler.getVaporRate(world, x, y, z, heat);
        DataPos pos = DataPos.get(x, y, z);

        final int dim = world.provider.dimensionId;
        if (!steamSources.containsKey(dim))
        {
            steamSources.put(dim, new HashSet());
        }

        if (vap > 0)
        {
            if (!steamSources.get(dim).contains(pos))
            {
                steamSources.get(dim).add(pos);
            }
            else
            {
                pos.dispose();
            }
        }
        if (!steamSources.get(dim).contains(pos))
        {
            steamSources.remove(pos);
            pos.dispose();
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            //Cleanup
            clearDeadSources();

            //Loop sources looking for changes
            for (ThermalSourceWrapper wrapper : thermalSourceMap.values())
            {
                if (wrapper.hasSourceChanged())
                {
                    fireSourceChange(wrapper.source, wrapper.source.getHeatGenerated());
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            final World world = event.world;
            final int dim = world.provider.dimensionId;

            if (steamSources.containsKey(dim))
            {
                HashSet<DataPos> steamPositions = steamSources.get(dim);
                Iterator<DataPos> it = steamPositions.iterator();
                while (it.hasNext())
                {
                    DataPos pos = it.next();
                    if (world.blockExists(pos.x, pos.y, pos.z))
                    {
                        int vap = ThermalHandler.getVaporRate(world, pos.x, pos.y, pos.z);
                        if (vap > 0)
                        {
                            if (Math.random() > 0.4)
                            {
                                PacketSpawnParticle packetSpawnParticle = new PacketSpawnParticle(dim, pos.x + r(), pos.y + r(), pos.z + r(), 0, 0, 0, "smoke");
                                PacketSystem.INSTANCE.sendToAllAround(packetSpawnParticle, world, pos, 30);
                            }
                        }
                        else
                        {
                            it.remove();
                            pos.dispose();
                        }
                    }
                    else
                    {
                        it.remove();
                        pos.dispose();
                    }
                }
            }
        }
    }

    private double r()
    {
        return Math.random() * 0.5 - Math.random() * 0.5;
    }
}
