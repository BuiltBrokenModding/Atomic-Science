package com.builtbroken.atomic.map.exposure;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.config.logic.ConfigRadiation;
import com.builtbroken.atomic.map.data.DataChange;
import com.builtbroken.atomic.map.data.DataPos;
import com.builtbroken.atomic.map.data.ThreadDataChange;
import com.builtbroken.atomic.api.radiation.IRadiationNode;
import com.builtbroken.atomic.map.data.storage.DataChunk;
import com.builtbroken.atomic.map.exposure.node.RadSourceMap;
import com.builtbroken.atomic.map.exposure.node.RadiationNode;
import com.builtbroken.jlib.lang.StringHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.Map;

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
    protected boolean updateLocation(DataChange change)
    {
        long time = System.nanoTime();

        final World world = DimensionManager.getWorld(change.dim());
        if (world != null) //TODO check if world is loaded
        {
            final HashMap<BlockPos, Integer> collectedData = updateValue(world, change.xi(), change.yi(), change.zi(), change.value);
            if (shouldRun)
            {
                //TODO convert to method or class
                ((WorldServer) world).addScheduledTask(() ->
                {
                    if (change.source instanceof IRadiationSource)
                    {
                        final IRadiationSource source = ((IRadiationSource) change.source);
                        //Get data
                        final HashMap<BlockPos, IRadiationNode> oldMap = source.getCurrentNodes();
                        final HashMap<BlockPos, IRadiationNode> newMap = new HashMap();

                        //Remove old data from map
                        source.disconnectMapData();

                        //Add new data, recycle old nodes to reduce memory churn
                        for (Map.Entry<BlockPos, Integer> entry : collectedData.entrySet()) //TODO move this to source to give full control over data structure
                        {
                            final int value = entry.getValue();
                            final BlockPos pos = entry.getKey();

                            if (oldMap != null && oldMap.containsKey(pos))
                            {
                                final IRadiationNode node = oldMap.get(pos);
                                if (node != null)
                                {
                                    //Update value
                                    node.setRadiationValue(value);

                                    //Store in new map
                                    newMap.put(pos, node);
                                }

                                //Remove from old map
                                oldMap.remove(pos);
                            }
                            else
                            {
                                newMap.put(pos, RadiationNode.get(source, value));
                            }
                        }

                        //Clear old data
                        source.disconnectMapData();
                        source.clearMapData();

                        //Set new data
                        source.setCurrentNodes(newMap);

                        //Tell the source to connect to the map
                        source.connectMapData();

                        //Trigger source update
                        source.initMapData();
                    }
                });
            }

            if (AtomicScience.runningAsDev)
            {
                time = System.nanoTime() - time;
                AtomicScience.logger.info(String.format("ThreadRadExposure: %sx %sy %sz | %sn | took %s",
                        change.xi(), change.yi(), change.zi(),
                        change.value,
                        StringHelpers.formatNanoTime(time)
                ));
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
     * @param value - value to remove
     * @param cx    - change location
     * @param cy    - change location
     * @param cz    - change location
     * @return map of position to edit
     */
    protected HashMap<BlockPos, Integer> updateValue(World world, int cx, int cy, int cz, int value)
    {
        if (value > 0)
        {
            //Track data, also used to prevent editing same tiles (first pos is location, second stores data)
            final HashMap<BlockPos, Integer> radiationData = new HashMap();

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

    protected void path(HashMap<BlockPos, Integer> radiationData, World world,
                        final double cx, final double cy, final double cz,
                        final double dx, final double dy, final double dz,
                        double power, double edit_range)
    {
        //Position
        double x = cx;
        double y = cy;
        double z = cz;

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
                    radiationData.put(pos.disposeReturnBlockPos(), change);

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
                            || blockState.getMaterial() == Material.PACKED_ICE
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

    /**
     * Called to scan a chunk to add remove calls
     *
     * @param chunk
     */
    protected void queueRemove(DataChunk chunk)
    {
        chunk.forEachValue((dim, x, y, z, value) -> ThreadRadExposure.this.queuePosition(DataChange.get(new RadSourceMap(dim, new BlockPos(x, y, z), value), 0)), DataMapType.RAD_MATERIAL); //TODO see if needed
    }

    /**
     * Called to scan a chunk to add addition calls
     *
     * @param chunk
     */
    protected void queueAddition(DataChunk chunk)
    {
        chunk.forEachValue((dim, x, y, z, value) -> queuePosition(DataChange.get(new RadSourceMap(dim, new BlockPos(x, y, z), value), value)), DataMapType.RAD_MATERIAL);
    }
}
