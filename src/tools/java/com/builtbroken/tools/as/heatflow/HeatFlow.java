package com.builtbroken.tools.as.heatflow;

import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.data.DataChange;
import com.builtbroken.atomic.map.data.DataPos;
import com.builtbroken.atomic.map.thermal.node.ThermalSourceMap;
import com.builtbroken.atomic.map.thermal.thread.HeatPushCallback;
import com.builtbroken.atomic.map.thermal.thread.ThermalThreadData;
import com.builtbroken.atomic.map.thermal.thread.ThreadThermalAction;
import com.builtbroken.mc.testing.junit.world.FakeWorldServer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;


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
        ThermalHandler.init();

        final FakeWorldServer world = FakeWorldServer.newWorld("heatFlow");

        final ThermalSourceMap thermalSource = new ThermalSourceMap(0, new BlockPos(5, 61, 5), 1000);
        final ThreadTher thread = new ThreadTher();

        //Build test
        for (int z = 0; z < 11; z++)
        {
            for (int x = 0; x < 11; x++)
            {
                if (x == 0 || x == 10 || z == 0 || z == 10)
                {
                    world.setBlockState(new BlockPos(x, 61, z), Blocks.BEDROCK.getDefaultState());
                }
                else
                {
                    world.setBlockState(new BlockPos(x, 61, z), Blocks.WATER.getDefaultState());
                }
                world.setBlockState(new BlockPos(x, 62, z), Blocks.BEDROCK.getDefaultState());
                world.setBlockState(new BlockPos(x, 60, z), Blocks.BEDROCK.getDefaultState());
            }
        }


        //run thread
        thread.updateLocation(DataChange.get(thermalSource, 1000));

        //Tick world
        Util.runTask(world.getMinecraftServer().futureTaskQueue.poll(), LOGGER);

        //Debug result
        for (int z = 0; z < 11; z++)
        {
            for (int x = 0; x < 11; x++)
            {
                if (x == 0 || x == 10 || z == 0 || z == 10)
                {
                    System.out.printf(" %5s ", "x");
                }
                else
                {
                    System.out.printf(" %5d ", MapHandler.THERMAL_MAP.getStoredHeat(world, new BlockPos(x, 61, z)));
                }
            }
            System.out.println();
            System.out.println();
        }

        //Debug heat flows
        int cx = 0, cy = 0, cz = 0;
        while (thread.heatActionLog.peek() != null)
        {
            HeatLog log = thread.heatActionLog.poll();

            //Check for issues
            boolean error = false;
            if(log.cx == log.x && log.cy == log.y && log.cz == log.z)
            {
                error = true; //Some how we pathed to center
            }
            else if(log.heatOld <= 0)
            {
                error = true; //Heat should never push with zero or less
            }

            //Spacer between cells
            if(cx != log.cx || cy != log.cy || cz != log.cz)
            {
                cx = log.cx;
                cy = log.cy;
                cz = log.cz;
                System.out.println();
            }

            //Output
            System.out.println(log + (error ? " <---  Error!!!!" : ""));
        }
    }

    public static class ThreadTher extends ThreadThermalAction
    {

        public final Queue<HeatLog> heatActionLog = new LinkedList();

        @Override
        public boolean updateLocation(DataChange change)
        {
            return super.updateLocation(change);
        }

        @Override
        protected void pathNext(final ThermalThreadData thermalThreadData, final DataPos currentPos, HeatPushCallback heatSetter)
        {
            super.pathNext(thermalThreadData, currentPos, (x, y, z, h) -> logHeat(thermalThreadData, currentPos, x, y, z, h, heatSetter));
        }

        protected void logHeat(final ThermalThreadData thermalThreadData, final DataPos currentPos, int x, int y, int z, int heat, HeatPushCallback orginal)
        {
            if (heat > 0)
            {
                //Get old value
                final int oldheat = thermalThreadData.getHeat(currentPos);
                final int push = thermalThreadData.getHeatToMove(currentPos);

                //Pass through orginal value
                orginal.pushHeat(x, y, z, heat);

                //Log
                final HeatLog log = new HeatLog();
                log.cx = currentPos.xi();
                log.cy = currentPos.yi();
                log.cz = currentPos.zi();
                log.x = x;
                log.y = y;
                log.z = z;
                log.heatNew = heat;
                log.push = push;
                log.heatOld = oldheat;

                heatActionLog.offer(log);
            }
        }
    }

    public static class HeatLog
    {

        int cx, cy, cz;
        int x, y, z;
        int heatNew, push, heatOld;

        @Override
        public String toString()
        {
            return String.format("HeatLog[%d %d %d; -> %d %d %d; %d %d -> %d]@%d",
                    cx, cy, cz,
                    x, y, z,
                    push, heatOld, heatNew,
                    hashCode());
        }
    }
}
