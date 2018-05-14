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
import java.util.Map;
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

        final int cx = change.xi();
        final int cy = change.yi();
        final int cz = change.zi();

        HashMap<DataPos, Integer> old_data = calculateHeatSpread(map, cx, cy, cz, change.old_value);
        HashMap<DataPos, Integer> new_data = calculateHeatSpread(map, cx, cy, cz, change.new_value);

        //Clear old data
        for (Map.Entry<DataPos, Integer> entry : old_data.entrySet())
        {
            final DataPos dataPos = entry.getKey();
            int heat = map.getData(dataPos.xi(), dataPos.yi(), dataPos.zi());

            //Remove heat
            heat -= entry.getValue();

            //Add heat, saves a bit of time pulling from other map
            if (new_data.containsKey(dataPos))
            {
                heat += new_data.get(dataPos);
                new_data.remove(dataPos);
            }

            //Update map
            map.setData(dataPos.xi(), dataPos.yi(), dataPos.zi(), Math.max(0, heat));
        }

        //Add new data
        for (Map.Entry<DataPos, Integer> entry : new_data.entrySet())
        {
            final DataPos dataPos = entry.getKey();
            int heat = map.getData(dataPos.xi(), dataPos.yi(), dataPos.zi());

            //add heat
            heat += entry.getValue();

            //Update map
            map.setData(dataPos.xi(), dataPos.yi(), dataPos.zi(), heat);
        }
    }

    /**
     * Calculates spread of heat from source.
     * <p>
     * Works by pathing all tiles and calculating heat movement towards current tile.
     * In other words: Pulls heat towards tile instead of pushing heat.
     * <p>
     * Heat is not consumed for movement as would be expected with real world movement. Instead
     * its assumed heat will constantly be generated. Thus migrating heat is not needed beyond
     * estimating how much heat would be moved.
     *
     * @param map  - map to pull data from
     * @param cx   - center of heat
     * @param cy   - center of heat
     * @param cz   - center of heat
     * @param heat - amount of heat to move
     * @return positions and changes
     */
    protected HashMap<DataPos, Integer> calculateHeatSpread(final DataMap map, final int cx, final int cy, final int cz, final int heat)
    {
        final int range = 100;

        //Track data, also used to prevent looping same tiles
        final HashMap<DataPos, Integer> heatSpreadData = new HashMap();

        long time = System.nanoTime();
        if (heat > 6)
        {
            //Track tiles to path
            final Queue<DataPos> pathNext = new LinkedList();

            //Add center point
            pathNext.add(DataPos.get(cx, cy, cz));

            //Breadth first pathfinder
            while (!pathNext.isEmpty())
            {
                final DataPos currentPos = pathNext.poll();

                //Calculate heat pushed from all sides & look for new tiles to path
                int heatAsPosition = 0;
                for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                {
                    int i = currentPos.x + direction.offsetX;
                    int j = currentPos.y + direction.offsetY;
                    int k = currentPos.z + direction.offsetZ;

                    //Only path tiles in map and in range of source
                    if (inRange(cx, cy, cz, i, j, k, range) && j >= 0 && k < 256)
                    {
                        DataPos pos = DataPos.get(i, j, k);
                        if (!heatSpreadData.containsKey(pos))
                        {
                            pathNext.add(pos);
                        }
                        else
                        {
                            int heatAtNext = heatSpreadData.get(pos);
                            heatAsPosition += getHeatToSpread(map, i, j, k, currentPos.x, currentPos.y, currentPos.z, heatAtNext);
                        }
                    }
                }

                //Keep track of value
                heatSpreadData.put(currentPos, heatAsPosition);
            }
        }

        if (AtomicScience.runningAsDev)
        {
            time = System.nanoTime() - time;
            AtomicScience.logger.info(String.format("%s: Spread heat %s | %s tiles | %s %s %s | in %s",
                    name,
                    heat,
                    heatSpreadData.size(),
                    cx, cy, cz,
                    StringHelpers.formatNanoTime(time)));
        }
        return heatSpreadData;
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
}
