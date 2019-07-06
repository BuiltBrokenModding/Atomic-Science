package com.builtbroken.atomic.map.thermal.thread;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.thermal.IThermalNode;
import com.builtbroken.atomic.api.thermal.IThermalSource;
import com.builtbroken.atomic.lib.thermal.HeatSpreadDirection;
import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.map.data.DataChange;
import com.builtbroken.atomic.map.data.DataPos;
import com.builtbroken.atomic.map.data.ThreadDataChange;
import com.builtbroken.atomic.map.thermal.node.ThermalNode;
import com.builtbroken.jlib.lang.StringHelpers;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * Handles updating the radiation map
 * <p>
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 4/28/2018.
 */
public class ThreadThermalAction extends ThreadDataChange
{
    //Max range, hard coded until algs can be completed
    public static final int RANGE = 10;
    public static final List<HeatSpreadDirection> DIRECTIONS = Lists.newArrayList(HeatSpreadDirection.values());

    public ThreadThermalAction()
    {
        super("ThreadThermalAction");
    }

    @Override
    protected boolean updateLocation(DataChange change)
    {
        //Get world
        final World world = DimensionManager.getWorld(change.dim());

        final int cx = change.xi();
        final int cy = change.yi();
        final int cz = change.zi();

        if (world != null && change.source instanceof IThermalSource)
        {
            //Collect data
            final ThermalThreadData thermalThreadData = new ThermalThreadData(world, cx, cy, cz, RANGE);
            calculateHeatSpread(thermalThreadData, change.value); //TODO store data into heat source

            //TODO convert to method or class
            ((WorldServer) world).addScheduledTask(() ->
            {
                if (change.source instanceof IThermalSource)
                {
                    final IThermalSource source = ((IThermalSource) change.source);
                    //Get data
                    final HashMap<BlockPos, IThermalNode> oldMap = source.getCurrentNodes();
                    final HashMap<BlockPos, IThermalNode> newMap = new HashMap();

                    //Remove old data from map
                    source.disconnectMapData();

                    //Add new data, recycle old nodes to reduce memory churn
                    for (Map.Entry<DataPos, DataPos> entry : thermalThreadData.getData().entrySet()) //TODO move this to source to give full control over data structure
                    {
                        final BlockPos pos = entry.getKey().disposeReturnBlockPos();
                        final int value = entry.getValue().x - entry.getValue().y;

                        if (oldMap != null && oldMap.containsKey(pos))
                        {
                            final IThermalNode node = oldMap.get(pos);
                            if (node != null)
                            {
                                //Update value
                                node.setHeatValue(value);

                                //Store in new map
                                newMap.put(pos, node);
                            }

                            //Remove from old map
                            oldMap.remove(pos);
                        }
                        else
                        {
                            newMap.put(pos, ThermalNode.get(source, value));
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


            return true;
        }
        return false;
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
     * @param thermalThreadData - data about the current thread job
     * @param heat              - amount of heat to move
     * @return positions and changes (first pos is position, second is data (x -> heat, y -> heat used))
     */
    protected void calculateHeatSpread(final ThermalThreadData thermalThreadData, final int heat)
    {
        //TODO consider splitting over several threads
        //TODO map fluid(water, air, lava, etc) pockets to allow convection currents
        //TODO use fluid pockets to equalize heat levels


        long time = System.nanoTime();
        if (heat > 6)
        {
            //Track tiles to path
            final Queue<DataPos> currentPathQueue = new LinkedList();
            final List<DataPos> nextPathQueue = new LinkedList();

            //Add center point
            thermalThreadData.setData(DataPos.get(thermalThreadData.pos), DataPos.get(heat, 0, 0));

            //Add connected tiles
            for (EnumFacing direction : EnumFacing.VALUES)
            {
                DataPos pos = DataPos.get(thermalThreadData.pos, direction);
                currentPathQueue.add(pos);
                thermalThreadData.setData(pos, DataPos.get(0, 0, 0));
            }

            //List of node to add to path queue after each loop
            final ArrayList<DataPos> tempHold = new ArrayList(6);

            //Breadth first pathfinder
            while (!currentPathQueue.isEmpty() || !nextPathQueue.isEmpty())
            {
                if (currentPathQueue.isEmpty())
                {
                    Collections.sort(nextPathQueue, Comparator.comparingDouble(pos -> pos.distance(thermalThreadData.pos)));
                    currentPathQueue.addAll(nextPathQueue);

                    nextPathQueue.clear();
                }
                //Get next
                final DataPos currentPos = currentPathQueue.poll();

                //Calculate heat pushed from all sides & look for new tiles to path
                int heatAsPosition = pathNext(thermalThreadData, currentPos, dataPos -> tempHold.add(dataPos));

                //Only add positions from temp if there is heat to move from current
                if (heatAsPosition > 0)
                {
                    //Add to path queue
                    nextPathQueue.addAll(tempHold);

                    //Add to map so we don't path over again and have init data to grab
                    tempHold.forEach(e -> thermalThreadData.setData(e, DataPos.get(0, 0, 0)));
                }
                else
                {
                    //Recycle objects
                    tempHold.forEach(e -> e.dispose());
                }
                //Clear for next run
                tempHold.clear();

                //Keep track of value
                thermalThreadData.setHeat(currentPos, heatAsPosition);
            }
        }

        //Logging
        if (AtomicScience.runningAsDev)
        {
            time = System.nanoTime() - time;
            AtomicScience.logger.info(String.format("%s: Spread heat %s | %s tiles | %s %s %s | in %s",
                    name,
                    heat,
                    thermalThreadData.getData().size(),
                    thermalThreadData.pos.xi(),
                    thermalThreadData.pos.yi(),
                    thermalThreadData.pos.zi(),
                    StringHelpers.formatNanoTime(time)));
        }
    }

    private int pathNext(final ThermalThreadData thermalThreadData, final DataPos currentPos, Consumer<DataPos> tempHold)
    {
        //Find directions to spread heat and calculate max heat ratio
        final Queue<HeatSpreadDirection> spreadDirections = new LinkedList();

        //Total heat transfer ratio, used to convert ratio to percentages when balancing heat flow
        double heatRateTotal = calculateHeatSpread(thermalThreadData, currentPos, dir -> spreadDirections.add(dir));


        final IBlockState centerBlock = thermalThreadData.world.getBlockState(new BlockPos(currentPos.xi(), currentPos.yi(), currentPos.zi()));

        //Only loop values we had within range
        int heatAsPosition = (int) forEachHeatDirection(thermalThreadData.pos, currentPos, spreadDirections, (x, y, z, direction) ->
        {
            final DataPos pos = DataPos.get(
                    currentPos.x + direction.offsetX,
                    currentPos.y + direction.offsetY,
                    currentPos.z + direction.offsetZ);

            //If we have no path position add to queue
            if (!thermalThreadData.hasData(pos))
            {
                //Only add if only sides, do not path corners. As it will result in low heat spread.
                if (direction.ordinal() < 6)
                {
                    tempHold.accept(pos);
                }
            }
            //If we have data do heat movement
            else
            {
                final BlockPos next = new BlockPos(x, y, z);
                if (thermalThreadData.world.isBlockLoaded(next))
                {
                    //Get heat from direction
                    final int heatAtNext = thermalThreadData.getHeat(pos);

                    //Get block
                    IBlockState nextBlock = thermalThreadData.world.getBlockState(next); //TODO use mutable pos

                    //Calculate spread ratio from direction
                    double transferRate = ThermalHandler.getHeatMoveRate(nextBlock, centerBlock);   //TODO recycle block pos

                    //Convert ratio into percentage
                    double percentage = (transferRate / heatRateTotal) * direction.percentage;

                    //Calculate heat to move to current position from direction
                    int heatMoved = (int) Math.floor(heatAtNext * percentage);

                    //Update direction position with heat moved
                    thermalThreadData.addHeatMoved(pos, heatMoved);

                    //Recycle
                    pos.dispose();

                    //Increase heat at position
                    return heatMoved;
                }
            }
            return 0;
        });

        return heatAsPosition;
    }

    private float calculateHeatSpread(final ThermalThreadData thermalThreadData, final DataPos currentPos, final Consumer<HeatSpreadDirection> consumer)
    {
        final IBlockState centerBlock = thermalThreadData.world.getBlockState(new BlockPos(currentPos.xi(), currentPos.yi(), currentPos.zi())); //TODO use mutable pos
        return forEachHeatDirection(thermalThreadData.pos, currentPos, DIRECTIONS, (x, y, z, dir) ->
        {
            final BlockPos next = new BlockPos(x, y, z);
            if (thermalThreadData.world.isBlockLoaded(next))
            {
                //Get block
                IBlockState nextBlock = thermalThreadData.world.getBlockState(next); //TODO use mutable pos

                //Add to set
                consumer.accept(dir);

                //Return heat transfer rate
                return ThermalHandler.getHeatMoveRate(nextBlock, centerBlock);
            }
            return 0;
        });
    }

    private float forEachHeatDirection(final DataPos center, final DataPos currentPos, Iterable<HeatSpreadDirection> dirs, HeatDirConsumer directionConsumer)
    {
        float value = 0;
        for (HeatSpreadDirection direction : dirs)
        {
            //Check range to prevent infinite spread
            int x = currentPos.x + direction.offsetX;
            int y = currentPos.y + direction.offsetY;
            int z = currentPos.z + direction.offsetZ;
            if (inRange(center, x, y, z, RANGE) && y >= 0 && y < 256)
            {
                value += directionConsumer.accept(x, y, z, direction);
            }
        }
        return value;
    }

    public interface HeatDirConsumer
    {
        float accept(int x, int y, int z, HeatSpreadDirection direction);
    }
}
