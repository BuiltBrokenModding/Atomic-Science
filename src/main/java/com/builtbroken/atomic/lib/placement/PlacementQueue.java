package com.builtbroken.atomic.lib.placement;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/12/2018.
 */
public class PlacementQueue
{
    private static final ConcurrentLinkedQueue<BlockPlacement> queue = new ConcurrentLinkedQueue();

    public static void queue(World world, int x, int y, int z, Block block, int meta)
    {
        queue(new BlockPlacement(world, x, y, z, block, meta));
    }

    public static void queue(BlockPlacement placement)
    {
        if (placement != null && placement.block != null)
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
            while (!queue.isEmpty() && System.currentTimeMillis() - time < 10)
            {
                BlockPlacement placement = queue.poll();
                if (placement != null)
                {
                    placement.doPlacement();
                }
            }
        }
    }
}
