package com.builtbroken.atomic.map.thermal;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.data.DataChange;
import com.builtbroken.atomic.map.data.DataMap;
import com.builtbroken.atomic.map.data.DataPos;
import com.builtbroken.atomic.map.data.ThreadDataChange;
import com.builtbroken.jlib.lang.StringHelpers;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Handles updating the radiation map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/28/2018.
 */
public class ThreadThermalAction extends ThreadDataChange
{
    public ThreadThermalAction()
    {
        super("ThreadThermalAction");
    }

    @Override
    protected void updateLocation(DataChange change)
    {
        //Get radiation exposure map
        DataMap map;
        synchronized (MapHandler.THERMAL_MAP)
        {
            map = MapHandler.THERMAL_MAP.getMap(change.dim, true);
        }

        spreadHeat(map, change);
    }

    protected void spreadHeat(DataMap map, DataChange change)
    {
        final int cx = change.xi();
        final int cy = change.yi();
        final int cz = change.zi();

        long time = System.nanoTime();
        if (change.new_value > 6)
        {
            int heatToMove = (int) (change.new_value * 0.25f); //Only move 25% of heat at a time
            change.new_value -= heatToMove;

            HashMap<DataPos, Integer> pathedTiles = new HashMap();
            Queue<DataPos> pathNext = new LinkedList();

            //Add first
            pathNext.add(DataPos.get(cx, cy, cz));

            while (!pathNext.isEmpty())
            {
                final DataPos currentPos = pathNext.poll();

                int heatAsPosition = 0;
                for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                {
                    int i = currentPos.x + direction.offsetX;
                    int j = currentPos.y + direction.offsetY;
                    int k = currentPos.z + direction.offsetZ;

                    DataPos pos = DataPos.get(i, j, k);
                    if (!pathedTiles.containsKey(pos))
                    {
                        pathNext.add(pos);
                    }
                    else
                    {
                        int heatAtNext = pathedTiles.get(pos);
                        heatAsPosition += getHeatToSpread(map, i, j, k, currentPos.x, currentPos.y, currentPos.z, heatAtNext);
                    }
                }
                pathedTiles.put(currentPos, heatAsPosition);
            }
        }

        if (AtomicScience.runningAsDev)
        {
            time = System.nanoTime() - time;
            AtomicScience.logger.info(String.format("%s: Spread heat %s | %s %s %s | in %s",
                    name,
                    change.new_value,
                    cx, cy, cz,
                    StringHelpers.formatNanoTime(time)));
        }

    }

    protected int getHeatToSpread(DataMap map, int x, int y, int z, int i, int j, int k, final int heatToMove)
    {
        if (map.blockExists(i, j, k))
        {
            //Only move heat if we can move
            int heat = map.getData(i, j, k);
            if (heatToMove > heat)
            {
                //Get heat actual movement, heat will not transfer equally from 1 tile to the next
                return MapHandler.THERMAL_MAP.getHeatSpread(map.getWorld(), x, y, z, i, j, k, heatToMove);
            }
            return 0;
        }
        return heatToMove;
    }

    protected void setData(int dim, int x, int y, int z, int newValue)
    {
        synchronized (MapHandler.THERMAL_MAP.setDataQueue)
        {
            MapHandler.THERMAL_MAP.setDataQueue.add(DataChange.get(dim, x, y, z, 0, newValue));
        }
    }
}
