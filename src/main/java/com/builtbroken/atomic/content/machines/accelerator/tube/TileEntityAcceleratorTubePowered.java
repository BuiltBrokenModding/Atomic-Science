package com.builtbroken.atomic.content.machines.accelerator.tube;

import com.builtbroken.atomic.content.machines.accelerator.magnet.TileEntityMagnet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/9/2018.
 */
public class TileEntityAcceleratorTubePowered extends TileEntityAcceleratorTube implements ITickable
{
    private int timer = 0;

    protected final List<MagnetPos> magnetPosList = new ArrayList();

    protected boolean isLayoutInvalid = false;

    protected float magnetPower;

    @Override
    public void update() //TODO replace with chunk update event and block update event to reduce tick lag
    {
        if (timer-- <= 0)
        {
            //Start timer
            timer = 100 + world.rand.nextInt(100);
            updateLayout();
        }
    }

    protected void updateLayout()
    {
        //Check layout
        if (isLayoutInvalid = scanForMagnets())
        {
            //If invalid layout, clear magnet ownership
            magnetPosList.forEach(pos -> {
                TileEntity tile = world.getTileEntity(pos.pos);
                if (tile instanceof TileEntityMagnet)
                {
                    ((TileEntityMagnet) tile).setOwner(null);
                }
            });
            magnetPosList.clear();
        }

        //Calculate power
        magnetPower = calculateMagnetPower();
    }

    protected float calculateMagnetPower()
    {
        float power = 0;
        for (MagnetPos magnetPos : magnetPosList)
        {
            power += (1 / magnetPos.distance);
        }

        return power;
    }

    protected boolean scanForMagnets()
    {
        magnetPosList.clear();

        final Set<BlockPos> alreadySearched = new HashSet();
        final Queue<BlockPos> queue = new LinkedList();

        //Add self as start
        queue.offer(getPos());
        alreadySearched.add(getPos());

        //Get directions to path
        List<EnumFacing> directions = new ArrayList(4);
        for (EnumFacing enumFacing : EnumFacing.VALUES)
        {
            if (enumFacing != getDirection() && enumFacing != getDirection().getOpposite())
            {
                directions.add(enumFacing);
            }
        }

        //Loop until we run out of things to path
        while (queue.peek() != null)
        {
            BlockPos pos = queue.poll();

            //Loop direction per post
            for (EnumFacing direction : directions)
            {
                //Check that we have not already pathed
                BlockPos next = pos.offset(direction);
                if (!alreadySearched.contains(next))
                {
                    //Mark as already pathed
                    alreadySearched.add(next);

                    //Check for magnet
                    TileEntity tile = world.getTileEntity(next);
                    if (tile instanceof TileEntityMagnet)
                    {
                        if (((TileEntityMagnet) tile).getOwner() == null || ((TileEntityMagnet) tile).getOwner() == this)
                        {
                            ((TileEntityMagnet) tile).setOwner(this);
                            addMagnet(next);
                            queue.offer(next);
                        }
                        else
                        {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    protected void addMagnet(BlockPos pos)
    {
        //Manhattan distance
        int distance = Math.abs(pos.getX() - xi()) + Math.abs(pos.getY() - yi()) + Math.abs(pos.getZ() - zi());
        magnetPosList.add(new MagnetPos(pos, distance));
    }

    public static class MagnetPos
    {
        public final BlockPos pos;
        public final int distance;

        public MagnetPos(BlockPos pos, int distance)
        {
            this.pos = pos;
            this.distance = distance;
        }
    }
}
