package com.builtbroken.atomic.map.thermal;

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

        //Get heat to move, only move heat if we are above natural decay limit
        int totalHeat = change.new_value - change.old_value;
        if (totalHeat > 6)
        {
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) //TODO recode to sort by lowest heat
            {
                int i = change.xi() + direction.offsetX;
                int j = change.yi() + direction.offsetY;
                int k = change.zi() + direction.offsetZ;

                //Only move heat if we can move
                int heat = map.getData(i, j, k);
                int delta = change.new_value - heat;
                if (delta > 0) //TODO check if we want to set a lower limit on this to reduce CPU time
                {
                    //Get heat to move, goal is to even out heat between tiles
                    int movement = Math.min(delta, totalHeat / 7); //7 -> 6 sides + self, can't transfer 100% heat away from self

                    //Get heat actual movement, heat will not transfer equally from 1 tile to the next
                    int actualMove = MapHandler.THERMAL_MAP.getHeatSpread(change.xi(), change.yi(), change.zi(), i, j, k, movement);

                    //Update values
                    heat += actualMove;
                    totalHeat -= heat;

                    //Update map
                    map.setData(i, j, k, heat);
                }
            }
        }
        else
        {
            map.setData(change.xi(), change.yi(), change.zi(), 0);
        }
    }
}
