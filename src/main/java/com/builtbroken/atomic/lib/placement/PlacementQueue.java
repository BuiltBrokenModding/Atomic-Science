package com.builtbroken.atomic.lib.placement;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/12/2018.
 */
public class PlacementQueue
{
    private static final ConcurrentLinkedQueue<BlockPlacement> queue = new ConcurrentLinkedQueue();

    public static void queue(World world, BlockPos pos, IBlockState state)
    {
        queue(new BlockPlacement(world, pos, state));
    }

    public static void queue(BlockPlacement placement)
    {
        if (placement != null && placement.blockState != null)
        {
            queue.add(placement);
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            long time = System.currentTimeMillis();
            LinkedList<BlockPlacement> delayList = new LinkedList();
            while (!queue.isEmpty() && System.currentTimeMillis() - time < 10)
            {
                BlockPlacement placement = queue.poll();
                if (placement != null && !placement.doPlacement())
                {
                    //If not placed, add to delay list to be re-added to queue
                    delayList.add(placement);
                }
            }
            queue.addAll(delayList);
        }
    }
}
