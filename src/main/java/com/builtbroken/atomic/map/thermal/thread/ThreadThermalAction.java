package com.builtbroken.atomic.map.thermal.thread;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.thermal.IThermalNode;
import com.builtbroken.atomic.api.thermal.IThermalSource;
import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.map.data.DataChange;
import com.builtbroken.atomic.map.data.DataPos;
import com.builtbroken.atomic.map.data.ThreadDataChange;
import com.builtbroken.atomic.map.thermal.node.ThermalNode;
import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.jlib.lang.StringHelpers;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

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
    public static final List<EnumFacing> DIRECTIONS = Lists.newArrayList(EnumFacing.values());

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

        if (world instanceof WorldServer && change.source instanceof IThermalSource)
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
                    for (Map.Entry<DataPos, ThermalData> entry : thermalThreadData.getData().entrySet()) //TODO move this to source to give full control over data structure
                    {
                        final BlockPos pos = entry.getKey().disposeReturnBlockPos();
                        final int value = entry.getValue().getHeatAndDispose(); //TODO cap to capacity

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
     * @param heatTotal         - amount of heat to move
     * @return positions and changes (first pos is position, second is data (x -> heat, y -> heat used))
     */
    protected void calculateHeatSpread(final ThermalThreadData thermalThreadData, final int heatTotal)
    {
        //TODO consider splitting over several threads
        //TODO map fluid(water, air, lava, etc) pockets to allow convection currents
        //TODO use fluid pockets to equalize heat levels


        long time = System.nanoTime();
        if (heatTotal > 6)
        {
            //Track tiles to path
            final Queue<DataPos> currentPathQueue = new LinkedList();
            final List<DataPos> nextPathQueue = new LinkedList();

            //Add center point
            thermalThreadData.setHeat(DataPos.get(thermalThreadData), heatTotal);
            currentPathQueue.add(DataPos.get(thermalThreadData));

            //Breadth first pathfinder
            while (!currentPathQueue.isEmpty() || !nextPathQueue.isEmpty())
            {
                //Path next shell
                if (currentPathQueue.isEmpty())
                {
                    currentPathQueue.addAll(nextPathQueue);
                    nextPathQueue.clear();
                }

                //Get next and set to push heat
                final DataPos nextPathPos = thermalThreadData.setToPush(currentPathQueue.poll());

                //Calculate heat pushed from all sides and look for new tiles to path
                pathNext(thermalThreadData, nextPathPos, (pos, heat) -> {
                    if (heat > 0)
                    {
                        if (!thermalThreadData.hasData(pos))
                        {
                            nextPathQueue.add(DataPos.get(pos));
                        }
                        thermalThreadData.addHeat(pos, heat);
                    }
                });

                //Recycle pathed position
                nextPathPos.dispose();
            }
        }

        //Logging
        if (AtomicScience.runningAsDev)
        {
            time = System.nanoTime() - time;
            AtomicScience.logger.info(String.format("%s: Spread heat %s | %s tiles | %s %s %s | in %s",
                    name,
                    heatTotal,
                    thermalThreadData.getData().size(),
                    thermalThreadData.xi(),
                    thermalThreadData.yi(),
                    thermalThreadData.zi(),
                    StringHelpers.formatNanoTime(time)));
        }
    }

    private void pathNext(final ThermalThreadData thermalThreadData, final DataPos pushBlock, HeatPushCallback heatSetter)
    {
        //Find directions to spread heat and calculate max heat ratio
        final LinkedList<DataPos> spreadPositions = new LinkedList();

        //Total heat transfer ratio, used to convert ratio to percentages when balancing heat flow
        int heatRateTotal = calculateHeatSpread(thermalThreadData, pushBlock, pos -> spreadPositions.add(pos));

        //Block giving heat
        final IBlockState giverBlock = thermalThreadData.world.getBlockState(pushBlock.getPos());

        //Amount of heat lost in the movement
        final int heatLoss = ThermalHandler.getBlockLoss(giverBlock);

        //Total heat to give
        final int totalMovementHeat = thermalThreadData.getHeatToMove(pushBlock);

        //Only loop values we had within range
        for (DataPos pos : spreadPositions)
        {
            final BlockPos next = pos.getPos(); //TODO use mutable pos

            //Block receiving heat
            final IBlockState targetBlock = thermalThreadData.world.getBlockState(next);

            //Calculate spread ratio from direction
            double transferRate = ThermalHandler.getHeatMovementWeight(targetBlock);

            //Convert ratio into percentage
            double percentage = (transferRate / (float) heatRateTotal);

            //Calculate heat to move to current position from direction
            int heatMoved = Math.max(0, (int) Math.floor(totalMovementHeat * percentage) - heatLoss);

            //Push heat
            heatSetter.pushHeat(pos, heatMoved);

            //Recycle
            pos.dispose();
        }
    }

    private int calculateHeatSpread(final ThermalThreadData thermalThreadData, final DataPos currentPos, final Consumer<DataPos> consumer)
    {
        return forEachHeatDirection(thermalThreadData, currentPos, DIRECTIONS, (x, y, z, dir) ->
        {
            final BlockPos next = new BlockPos(x, y, z);
            if (thermalThreadData.world.isBlockLoaded(next))
            {
                final DataPos pos = DataPos.get(x, y, z);
                if (thermalThreadData.canReceive(pos))
                {
                    //Get block
                    final IBlockState targetBlock = thermalThreadData.world.getBlockState(next); //TODO use mutable pos

                    //Add to set
                    consumer.accept(pos);

                    //Return heat transfer rate
                    return ThermalHandler.getHeatMovementWeight(targetBlock);
                }

                //Recycle unused
                pos.dispose();
            }
            return 0;
        });
    }

    private int forEachHeatDirection(final IPos3D center, final DataPos currentPos, Iterable<EnumFacing> dirs, HeatDirConsumer directionConsumer)
    {
        int value = 0;
        for (EnumFacing direction : dirs)
        {
            //Check range to prevent infinite spread
            int x = currentPos.x + direction.getXOffset();
            int y = currentPos.y + direction.getYOffset();
            int z = currentPos.z + direction.getZOffset();
            if (inRange(center, x, y, z, RANGE) && y >= 0 && y < 256)
            {
                value += directionConsumer.accept(x, y, z, direction);
            }
        }
        return value;
    }

    public interface HeatDirConsumer
    {
        int accept(int x, int y, int z, EnumFacing direction);
    }
}
