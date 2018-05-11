package com.builtbroken.atomic.map.exposure;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.config.ConfigRadiation;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.data.DataChange;
import com.builtbroken.atomic.map.data.DataMap;
import com.builtbroken.atomic.map.data.ThreadDataChange;
import com.builtbroken.jlib.lang.StringHelpers;

/**
 * Handles updating the radiation map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/28/2018.
 */
public class ThreadRadExposure extends ThreadDataChange
{
    public ThreadRadExposure()
    {
        super("ThreadRadExposure");
    }

    @Override
    protected void updateLocation(DataChange change)
    {
        //Get radiation exposure map
        DataMap map;
        synchronized (MapHandler.RADIATION_MAP)
        {
            map = MapHandler.RADIATION_MAP.getMap(change.dim, true);
        }

        long time = System.nanoTime();

        //Clear old values
        updateValue(map, change.old_value, change.xi(), change.yi(), change.zi(), true);

        //Add new value, completed as a separate step due to range differences
        updateValue(map, change.new_value, change.xi(), change.yi(), change.zi(), false);

        if (AtomicScience.runningAsDev)
        {
            time = System.nanoTime() - time;
            AtomicScience.logger.info(String.format("ThreadRadExposure: %sx %sy %sz | %so %sn | took %s",
                    change.x, change.y, change.z,
                    change.old_value, change.new_value,
                    StringHelpers.formatNanoTime(time)
            ));
        }
    }

    /**
     * Removes the old value from the map
     *
     * @param map   - map to edit
     * @param value - value to remove
     * @param cx    - change location
     * @param cy    - change location
     * @param cz    - change location
     * @return true if finished
     */
    protected boolean updateValue(DataMap map, int value, int cx, int cy, int cz, boolean remove)
    {
        if (value > 0)
        {
            final int rad = getRadFromMaterial(value);
            final int edit_range = Math.min(ConfigRadiation.MAX_UPDATE_RANGE, (int) Math.floor(getDecayRange(rad)));

            if (AtomicScience.runningAsDev)
            {
                AtomicScience.logger.info(String.format("ThreadRadExposure: updateValue(map, %smat, %sx %sy %sz, %s) | %srad | %sm",
                        value,
                        cx, cy, cz,
                        remove,
                        rad,
                        edit_range
                ));
            }

            final int startX = cx - edit_range;
            final int startY = Math.max(0, cy - edit_range);
            final int startZ = cz - edit_range;

            final int endX = cx + edit_range + 1;
            final int endY = Math.min(255, cy + edit_range + 1);
            final int endZ = cz + edit_range + 1;

            for (int x = startX; x < endX; x++)
            {
                for (int y = startY; y < endY; y++)
                {
                    for (int z = startZ; z < endZ; z++)
                    {
                        if (!shouldRun)
                        {
                            return false;
                        }

                        //Get delta
                        int dx = x - cx;
                        int dy = y - cy;
                        int dz = z - cz;

                        //Get data
                        double distanceSQ = dx * dx + dz * dz + dy * dy;
                        int current_value = map.getData(x, y, z);
                        int change = getRadForDistance(rad, distanceSQ);

                        if (remove)
                        {
                            current_value -= change;
                        }
                        else
                        {
                            current_value += change;
                        }

                        //Prevents crashes loading map areas from thread
                        if (map.blockExists(x, y, z))
                        {
                            //Save
                            map.setData(x, y, z, Math.max(0, current_value));
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Converts material value to rad
     *
     * @param material_amount - amount of material
     * @return rad value
     */
    protected int getRadFromMaterial(int material_amount)
    {
        return (int) Math.ceil(material_amount * ConfigRadiation.MAP_VALUE_TO_MILI_RAD);
    }

    /**
     * Gets radiation value for the given distance
     *
     * @param power      - ordinal power at 1 meter
     * @param distanceSQ - distance to get current
     * @return distance reduced value, if less than 1 will return full
     */
    protected int getRadForDistance(int power, double distanceSQ)
    {
        if (distanceSQ < 1)
        {
            return power;
        }
        return (int) Math.floor(power / distanceSQ); //its assumed power is measured at 1 meter from source
    }

    /**
     * At what point does radiation power drop below 1
     *
     * @param value - starting value
     * @return distance
     */
    protected double getDecayRange(int value)
    {
        double power = value;
        double distance = 1;
        while (power > 1)
        {
            distance += 0.5;
            power = getRadForDistance(value, distance);
        }
        return distance;
    }
}
