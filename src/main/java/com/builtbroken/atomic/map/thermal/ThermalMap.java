package com.builtbroken.atomic.map.thermal;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.thermal.IThermalSource;
import com.builtbroken.atomic.api.thermal.IThermalSystem;
import com.builtbroken.atomic.config.server.ConfigServer;
import com.builtbroken.atomic.lib.MassHandler;
import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.lib.vapor.VaporHandler;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.events.MapSystemEvent;
import com.builtbroken.atomic.network.netty.PacketSystem;
import com.builtbroken.atomic.network.packet.client.PacketSpawnParticle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Handles heat in the map
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/6/2018.
 */
public class ThermalMap implements IThermalSystem
{
    private HashMap<Integer, HashSet<BlockPos>> steamSources = new HashMap();

    /**
     * Called to fire a source change event
     *
     * @param source
     * @param newValue
     */
    protected void updateSourceValue(IThermalSource source, int newValue)
    {
        if (AtomicScience.runningAsDev)
        {
            AtomicScience.logger.info("ThermalMap: on changed " + source);
        }

    }

    /**
     * Energy in joules
     *
     * @param world
     * @param pos   - location
     * @return
     */
    public long getJoules(World world, BlockPos pos)
    {
        return getStoredValue(world, pos) * 1000L; //Map stores heat in kilo-joules
    }

    public int getStoredValue(World world, BlockPos pos)
    {
        return DataMapType.THERMAL.getValue(MapHandler.GLOBAL_DATA_MAP.getData(world, pos));
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
     * @param pos   - location
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
     * @param pos    - location
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
     * @param pos   - location
     * @param pos2  - location2
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
     * @param pos   - location
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
     * @param pos   - location
     * @return temperature in kelvin
     */
    public double getEnvironmentalTemperature(World world, BlockPos pos)
    {
        return 290; //Slightly under room temp
    }


    public void onWorldUnload(World world)
    {
        steamSources.clear();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHeatChanged(MapSystemEvent.OnValueChanged event)
    {
        final World world = event.world();
        if (world != null && !world.isRemote && event.type == DataMapType.THERMAL && world.isBlockLoaded(event.getPos()))
        {
            checkForThermalChange(world, event.getPos(), event.getNewValue());
        }
    }

    @SubscribeEvent
    public void onBlockPlaced(PlayerInteractEvent.RightClickBlock event)
    {
        if (!event.getWorld().isRemote)
        {
            checkForThermalChange(event.getWorld(), event.getPos(), getStoredValue(event.getWorld(), event.getPos()));
        }
    }

    protected void checkForThermalChange(World world, BlockPos pos, int heat)
    {
        if (ThermalHandler.canChangeStates(world, pos))
        {
            long joules = heat * 1000L + getEnvironmentalJoules(world, pos); //x1000 for kj -> j
            if (joules > ThermalHandler.energyCostToChangeStates(world, pos))
            {
                ThermalHandler.changeStates(world, pos);
            }
        }

        int vap = VaporHandler.getVaporRate(world, pos, heat * 1000 + getEnvironmentalJoules(world, pos));

        final int dim = world.provider.getDimension();
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
        }
        else if (!steamSources.get(dim).contains(pos))
        {
            steamSources.remove(pos);
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            final World world = event.world;
            final int dim = world.provider.getDimension();

            if (ConfigServer.NETWORK.BOILING_EFFECT && steamSources.containsKey(dim))
            {
                HashSet<BlockPos> steamPositions = steamSources.get(dim);
                Iterator<BlockPos> it = steamPositions.iterator();
                while (it.hasNext())
                {
                    BlockPos pos = it.next();
                    if (world.isBlockLoaded(pos))
                    {
                        int vap = VaporHandler.getVaporRate(world, pos);
                        if (vap > 0)
                        {
                            if(event.world.rand.nextFloat() > 0.6)
                            {
                                int count = Math.min(10, Math.max(1, vap / 100));
                                PacketSpawnParticle packetSpawnParticle = new PacketSpawnParticle(dim,
                                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                                        0, 0, 0,
                                        "boiling;" + count);
                                PacketSystem.INSTANCE.sendToAllAround(packetSpawnParticle, world, pos, 30);
                            }
                        }
                        else
                        {
                            it.remove();
                        }
                    }
                    else
                    {
                        it.remove();
                    }
                }
            }
        }
    }
}
