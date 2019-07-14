package com.builtbroken.tools.as.heatflow;

import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.data.DataChange;
import com.builtbroken.atomic.map.thermal.ThermalMap;
import com.builtbroken.atomic.map.thermal.node.ThermalSourceMap;
import com.builtbroken.atomic.map.thermal.thread.ThreadThermalAction;
import com.builtbroken.mc.testing.junit.world.FakeWorldServer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-07-12.
 */
public class HeatFlow
{
    private static Logger LOGGER = LogManager.getLogger();

    public static void main(String... args)
    {
        //Init
        Bootstrap.register();
        final FakeWorldServer world = FakeWorldServer.newWorld("heatFlow");

        final ThermalSourceMap thermalSource = new ThermalSourceMap(0, new BlockPos(8, 61, 8), 1000);
        final ThreadTher thread = new ThreadTher();

        //Build test
        for (int z = 0; z < 16; z++)
        {
            for (int x = 0; x < 16; x++)
            {
                world.setBlockState(new BlockPos(x, 62, z), Blocks.WATER.getDefaultState());
                world.setBlockState(new BlockPos(x, 61, z), Blocks.WATER.getDefaultState());
                world.setBlockState(new BlockPos(x, 60, z), Blocks.STONE.getDefaultState());
            }
        }



        //run thread
        thread.updateLocation(DataChange.get(thermalSource, 1000));

        //Tick world
        Util.runTask(world.getMinecraftServer().futureTaskQueue.poll(), LOGGER);

        for (int z = 0; z < 16; z++)
        {
            for (int x = 0; x < 16; x++)
            {
                System.out.printf(" %5d ", MapHandler.THERMAL_MAP.getStoredHeat(world, new BlockPos(x, 61, z)));
            }
            System.out.println();
        }
    }

    public static class ThreadTher extends ThreadThermalAction
    {
        @Override
        public boolean updateLocation(DataChange change)
        {
            return super.updateLocation(change);
        }
    }
}
