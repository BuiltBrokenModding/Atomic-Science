package com.builtbroken.atomic.map.thermal;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.thermal.IThermalSource;
import com.builtbroken.atomic.api.thermal.IThermalSystem;
import com.builtbroken.atomic.config.server.ConfigServer;
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

    public int getStoredHeat(World world, BlockPos pos)
    {
        return DataMapType.THERMAL.getValue(MapHandler.GLOBAL_DATA_MAP.getData(world, pos));
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
            checkForThermalChange(event.getWorld(), event.getPos(), getStoredHeat(event.getWorld(), event.getPos()));
        }
    }

    protected void checkForThermalChange(World world, BlockPos pos, int heat)
    {
        /*
        if (ThermalHandler.canChangeStates(world, pos))
        {
            long joules = heat * 1000L + getEnvironmentalJoules(world, pos); //x1000 for kj -> j
            if (joules > ThermalHandler.energyCostToChangeStates(world, pos))
            {
                ThermalHandler.changeStates(world, pos);
            }
        }
        */

        int vap = VaporHandler.getVaporRate(world, pos, heat);

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
