package com.builtbroken.atomic.map.exposure;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.config.ConfigRadiation;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.data.*;
import com.builtbroken.jlib.lang.StringHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

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

        HashMap<DataPos, DataPos> old_data = updateValue(map, change.xi(), change.yi(), change.zi(), change.old_value);
        HashMap<DataPos, DataPos> new_data = updateValue(map, change.xi(), change.yi(), change.zi(), change.new_value);
        if (shouldRun)
        {
            new MapChangeSet(map, old_data, new_data).pop(); //TODO move to main thread to run
        }

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
    protected boolean updateValue2(DataMap map, int value, int cx, int cy, int cz, boolean remove) //Old method no used but saved TODO convert over to new system to allow users to cycle between methods
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
                        int change = (int) Math.floor(getRadForDistance(rad, distanceSQ));

                        if (remove)
                        {
                            current_value -= change;
                        }
                        else
                        {
                            current_value += change;
                        }

                        //Prevents crashes loading map areas from thread
                        if (map.blockExists(new BlockPos(x, y, z))) //TODO see if we need block pos
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
    protected double getRadForDistance(double power, double distanceSQ)
    {
        //its assumed power is measured at 1 meter from source
        return getRadForDistance(power, 1, distanceSQ);
    }

    /**
     * Gets radiation value for the given distance
     *
     * @param power            - ordinal power at 1 meter
     * @param distanceSourceSQ - distance from source were the power was measured
     * @param distanceSQ       - distance to get current
     * @return distance reduced value, if less than 1 will return full
     */
    protected double getRadForDistance(double power, double distanceSourceSQ, double distanceSQ)
    {
        //http://www.nde-ed.org/GeneralResources/Formula/RTFormula/InverseSquare/InverseSquareLaw.htm
        if (distanceSQ < distanceSourceSQ)
        {
            return power;
        }

        //I_2 = I * D^2 / D_2^2
        return (power * distanceSourceSQ) / distanceSQ;
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

    /**
     * Removes the old value from the map
     *
     * @param map   - map to edit
     * @param value - value to remove
     * @param cx    - change location
     * @param cy    - change location
     * @param cz    - change location
     * @return map of position to edit
     */
    protected HashMap<DataPos, DataPos> updateValue(DataMap map, int cx, int cy, int cz, int value)
    {
        if (value > 0)
        {
            final World world = map.getWorld();
            //Track data, also used to prevent editing same tiles (first pos is location, second stores data)
            final HashMap<DataPos, DataPos> radiationData = new HashMap();

            final int rad = getRadFromMaterial(value);
            final int edit_range = Math.min(ConfigRadiation.MAX_UPDATE_RANGE, (int) Math.floor(getDecayRange(rad)));

            if (edit_range > 1)
            {
                //How many steps to go per rotation
                final int steps = (int) Math.ceil(Math.PI / Math.atan(1.0D / edit_range));

                for (int phi_n = 0; phi_n < 2 * steps; phi_n++)
                {
                    for (int theta_n = 0; theta_n < 2 * steps; theta_n++)
                    {
                        //Get angles for rotation steps
                        double yaw = Math.PI * 2 / steps * phi_n;
                        double pitch = Math.PI * 2 / steps * theta_n;

                        //Figure out vector to move for trace (cut in half to improve trace skipping blocks)
                        double dx = sin(pitch) * cos(yaw) * 0.5;
                        double dy = cos(pitch) * 0.5;
                        double dz = sin(pitch) * sin(yaw) * 0.5;

                        path(radiationData, world,
                                cx + 0.5, cy + 0.5, cz + 0.5,
                                dx, dy, dz,
                                rad, edit_range);
                    }
                }
            }
            return radiationData;
        }
        return new HashMap();
    }

    protected void path(HashMap<DataPos, DataPos> radiationData, World world,
                        final double cx, final double cy, final double cz,
                        final double dx, final double dy, final double dz,
                        double power, double edit_range)
    {
        //Position
        double x = cx + 0.5;
        double y = cy + 0.5;
        double z = cz + 0.5;

        double distanceSQ = 1;
        double radDistance = 1;

        DataPos prevPos = null;

        do
        {
            if (!shouldRun)
            {
                return;
            }

            //Convert double position to int position
            int xi = (int) Math.floor(x);
            int yi = (int) Math.floor(y);
            int zi = (int) Math.floor(z);

            //Get distance to center of block from center
            double distanceX = cx - (xi + 0.5);
            double distanceY = cy - (yi + 0.5);
            double distanceZ = cz - (zi + 0.5);
            distanceSQ = distanceX * distanceX + distanceZ * distanceZ + distanceY * distanceY;

            //Ignore center block
            if (distanceSQ > 0.5)
            {
                DataPos pos = DataPos.get(xi, yi, zi);

                //Only do action one time per block (not a perfect solution, but solves double hit on the same block in the same line)
                if (prevPos != pos)
                {
                    //Reduce radiation for distance
                    power = getRadForDistance(power, radDistance, distanceSQ);

                    //Reduce radiation
                    power = reduceRadiationForBlock(world, xi, yi, zi, power);


                    //Store change
                    int change = (int) Math.floor(power);
                    radiationData.put(pos, DataPos.get(change, 0, 0));

                    //Note previous block
                    prevPos = pos;

                    //Track last distance of radiation, as power is now measured from that position
                    radDistance = distanceSQ;
                }
                else
                {
                    pos.dispose();
                }
            }

            //Move forward
            x += dx;
            y += dy;
            z += dz;
        }
        while ((distanceSQ <= edit_range * edit_range) && power > 1);
    }

    protected double reduceRadiationForBlock(World world, int xi, int yi, int zi, double power)
    {
        //Get reduction
        float reduction = getReduceRadiationForBlock(world, xi, yi, zi);

        //TODO add system to allow per block flat limit, then apply greater (limit or percentage)
        //TODO add an upper limit, how much radiation a block can stop, pick small (limit or percentage)
        //Flat line
        if (power < reduction * 1000)
        {
            return 0;
        }

        //Reduce if not flat
        power -= power * reduction;


        //Calculate radiation
        return power;
    }

    protected float getReduceRadiationForBlock(World world, int xi, int yi, int zi) //TODO move to handler
    {
        //TODO add registry that allows decay per block & meta
        //TODO add interface to define radiation based on tile data
        //TODO add JSON data to allow users to customize values

        //Decay power per block
        BlockPos pos = new BlockPos(xi, yi, zi);
        IBlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();

        if (!block.isAir(blockState, world, pos))
        {
            if (blockState.getMaterial().isSolid())
            {
                if (blockState.isOpaqueCube())
                {
                    if (blockState.getMaterial() == Material.ROCK)
                    {
                        return ConfigRadiation.RADIATION_DECAY_STONE;
                    }
                    else if (blockState.getMaterial() == Material.GROUND
                            || blockState.getMaterial() == Material.GRASS
                            || blockState.getMaterial() == Material.SAND
                            || blockState.getMaterial() == Material.CLAY)
                    {
                        return ConfigRadiation.RADIATION_DECAY_STONE / 2;
                    }
                    else if (blockState.getMaterial() == Material.ICE
                            ||blockState.getMaterial() == Material.PACKED_ICE
                            || blockState.getMaterial() == Material.CRAFTED_SNOW)
                    {
                        return ConfigRadiation.RADIATION_DECAY_STONE / 3;
                    }
                    else if (blockState.getMaterial() == Material.IRON)
                    {
                        return ConfigRadiation.RADIATION_DECAY_METAL;
                    }
                    else
                    {
                        return ConfigRadiation.RADIATION_DECAY_PER_BLOCK;
                    }
                }
                else
                {
                    return ConfigRadiation.RADIATION_DECAY_PER_BLOCK / 2;
                }
            }
            else if (blockState.getMaterial().isLiquid())
            {
                return ConfigRadiation.RADIATION_DECAY_PER_FLUID;
            }
        }
        return 0;
    }
}
