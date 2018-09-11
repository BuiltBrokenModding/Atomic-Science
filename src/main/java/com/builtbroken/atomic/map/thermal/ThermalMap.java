package com.builtbroken.atomic.map.thermal;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.thermal.IHeatSource;
import com.builtbroken.atomic.api.thermal.IThermalSystem;
import com.builtbroken.atomic.config.ConfigNetwork;
import com.builtbroken.atomic.lib.MassHandler;
import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.MapSystem;
import com.builtbroken.atomic.map.data.DataChange;
import com.builtbroken.atomic.map.data.DataMap;
import com.builtbroken.atomic.map.data.DataPos;
import com.builtbroken.atomic.map.data.MapChangeSet;
import com.builtbroken.atomic.map.events.MapSystemEvent;
import com.builtbroken.atomic.network.netty.PacketSystem;
import com.builtbroken.atomic.network.packet.client.PacketSpawnParticle;
import com.builtbroken.jlib.lang.StringHelpers;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

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

    public ConcurrentLinkedQueue<MapChangeSet> dataFromThread = new ConcurrentLinkedQueue();

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
     * @param pos - location
     * @return
     */
    public long getJoules(World world, BlockPos pos)
    {
        return getData(world, pos) * 1000L; //Map stores heat in kilo-joules
    }

    public long getActualJoules(World world, BlockPos pos)
    {
        return getJoules(world, pos) + getEnvironmentalJoules(world, pos);
    }

    /**
     * Gets the temperature of the block at the location
     * <p>
     * Uses the properties of the block to calculate the value from
     * the heat + environmental values.
     *
     * @param world - map to pull data from
     * @param pos - location
     * @return temperature in Kelvin
     */
    public double getTemperature(World world, BlockPos pos)
    {
        return getTemperature(world, pos, getActualJoules(world, pos));
    }

    /**
     * Gets the temperature of the block at the location
     * <p>
     * Uses the properties of the block to calculate the value from
     * the heat + environmental values.
     *
     * @param world  - map to pull data from
     * @param pos - location
     * @param joules - heat energy
     * @return temperature in Kelvin
     */
    public double getTemperature(World world, BlockPos pos, double joules)
    {
        return joules / (MassHandler.getMass(world, pos) * ThermalHandler.getSpecificHeat(world, pos) * 1000);
    }

    /**
     * Gets the different in temperature between two locations
     *
     * @param world - map to pull data from
     * @param pos - location
     * @param pos2 - location2
     * @return temperature in Kelvin
     */
    public double getTemperatureDelta(World world, BlockPos pos, BlockPos pos2)
    {
        return getTemperature(world, pos) - getTemperature(world, pos2);
    }

    /**
     * Gets the amount of heat energy naturally present in the block due to environmental values
     *
     * @param world - location
     * @param pos - location
     * @return energy in joules
     */
    public long getEnvironmentalJoules(World world, BlockPos pos)
    {
        return ((long) Math.floor(getEnvironmentalTemperature(world, pos) * MassHandler.getMass(world, pos) * ThermalHandler.getSpecificHeat(world, pos))) * 1000L; //x1000 for kj -> j
    }

    /**
     * Gets the natural resting temperature of the environment
     *
     * @param world - location
     * @param pos - location
     * @return temperature in kelvin
     */
    public double getEnvironmentalTemperature(World world, BlockPos pos)
    {
        return 290; //Slightly under room temp
    }

    @Override
    public void onWorldUnload(World world)
    {
        super.onWorldUnload(world);
        thermalSourceMap.clear();
        steamSources.clear();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHeatChanged(MapSystemEvent.UpdateValue event)
    {
        World world = event.world();
        DataMap map = getMap(world, false);
        if (world != null && map != null && map.blockExists(event.pos))
        {
            checkForThermalChange(world, event.pos, event.new_value);
        }
    }

    @SubscribeEvent
    public void onBlockPlaced(PlayerInteractEvent.RightClickBlock event)
    {
        if (!event.getWorld().isRemote)
        {
            checkForThermalChange(event.getWorld(), event.getPos(), getData(event.getWorld(), event.getPos()));
        }
    }

    protected void checkForThermalChange(World world, BlockPos pos, int heat)
    {
        if (ThermalHandler.canChangeStates(world, pos))
        {
            long joules = heat * 1000 + getEnvironmentalJoules(world, pos); //x1000 for kj -> j
            if (joules > ThermalHandler.energyCostToChangeStates(world, pos))
            {
                ThermalHandler.changeStates(world, pos);
            }
        }

        int vap = ThermalHandler.getVaporRate(world, pos, heat * 1000 + getEnvironmentalJoules(world, pos));
        DataPos dataPos = DataPos.get(pos.getX(), pos.getY(), pos.getZ());

        final int dim = world.provider.getDimension();
        if (!steamSources.containsKey(dim))
        {
            steamSources.put(dim, new HashSet());
        }

        if (vap > 0)
        {
            if (!steamSources.get(dim).contains(dataPos))
            {
                steamSources.get(dim).add(dataPos);
            }
            else
            {
                dataPos.dispose();
            }
        }
        if (!steamSources.get(dim).contains(dataPos))
        {
            steamSources.remove(dataPos);
            dataPos.dispose();
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
        if (event.phase == TickEvent.Phase.START)
        {
            long time = System.currentTimeMillis();
            while (!dataFromThread.isEmpty() && System.currentTimeMillis() - time < 10)
            {
                MapChangeSet change = dataFromThread.poll();
                if (change != null)
                {
                    long t = System.nanoTime();
                    change.pop();
                    t = System.nanoTime() - t;
                    AtomicScience.logger.info("ThermalMap: Dumped data from thread to map. Data size: " + change.size + " Time: " + StringHelpers.formatNanoTime(t));
                }
            }
        }
        else if (event.phase == TickEvent.Phase.END)
        {
            final World world = event.world;
            final int dim = world.provider.getDimension();

            if (ConfigNetwork.BOILING_EFFECT && steamSources.containsKey(dim))
            {
                HashSet<DataPos> steamPositions = steamSources.get(dim);
                Iterator<DataPos> it = steamPositions.iterator();
                while (it.hasNext())
                {
                    DataPos pos = it.next(); //TODO change over to block pos
                    if (world.isBlockLoaded(new BlockPos(pos.x, pos.y, pos.z)))
                    {
                        int vap = ThermalHandler.getVaporRate(world, new BlockPos(pos.x, pos.y, pos.z));
                        if (vap > 0)
                        {
                            int count = Math.min(10, Math.max(1, vap / 100));
                            PacketSpawnParticle packetSpawnParticle = new PacketSpawnParticle(dim,
                                    pos.x + 0.5, pos.y + 0.5, pos.z + 0.5,
                                    0, 0, 0,
                                    "boiling;" + count);
                            PacketSystem.INSTANCE.sendToAllAround(packetSpawnParticle, world, pos, 30);
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
}
