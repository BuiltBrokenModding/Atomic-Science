package com.builtbroken.atomic.map.thermal;

import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.data.DataChange;
import com.builtbroken.atomic.map.data.DataMap;
import com.builtbroken.atomic.map.data.ThreadDataChange;
import net.minecraftforge.common.util.ForgeDirection;

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

        spreadHeat(map, change.xi(), change.yi(), change.zi(), change.new_value);
    }

    protected void spreadHeat(DataMap map, int x, int y, int z, int totalHeat)
    {
        if (totalHeat > 6)
        {
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) //TODO recode to sort by lowest heat
            {
                int i = x + direction.offsetX;
                int j = y + direction.offsetY;
                int k = z + direction.offsetZ;

                if(map.blockExists(i, j, k))
                {
                    //Only move heat if we can move
                    int heat = map.getData(i, j, k);
                    int delta = totalHeat - heat;
                    if (delta > 0) //TODO check if we want to set a lower limit on this to reduce CPU time
                    {
                        //Get heat to move, goal is to even out heat between tiles
                        int movement = Math.min(delta, totalHeat / 7); //7 -> 6 sides + self, can't transfer 100% heat away from self

                        //Get heat actual movement, heat will not transfer equally from 1 tile to the next
                        int actualMove = MapHandler.THERMAL_MAP.getHeatSpread(map.getWorld(), x, y, z, i, j, k, movement);

                        //Update values
                        heat += actualMove;
                        totalHeat -= heat;

                        //Update map
                        map.setData(i, j, k, heat);
                    }
                }
            }

            if(map.blockExists(x, y, z) && ThermalHandler.canChangeStates(map.getWorld(), x, y, z))
            {
                long joules = totalHeat * 1000;
                if (joules > ThermalHandler.energyToChangeStates(map.getWorld(), x, y, z))
                {
                  ThermalHandler.changeStates(map.getWorld(), x, y, z);
                }
            }

            //Update heat value
            map.setData(x, y, z, totalHeat);
        }
        else
        {
            map.setData(x, y, z, 0);
        }
    }
}
