package com.builtbroken.atomic.content.machines.accelerator.tube.powered;

import com.builtbroken.atomic.api.accelerator.AcceleratorHelpers;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorParticle;
import com.builtbroken.atomic.content.machines.accelerator.magnet.TileEntityMagnet;
import com.builtbroken.atomic.content.machines.accelerator.tube.normal.TileEntityAcceleratorTube;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.*;

/**
 * Created by Dark(DarkGuardsman, Robert) on 12/9/2018.
 */
public class TileEntityAcceleratorTubePowered extends TileEntityAcceleratorTube implements ITickable
{

    private int timer = 0;

    protected final List<MagnetPos> magnetPosList = new ArrayList();

    protected boolean isLayoutInvalid = false;

    protected float magnetPower;

    @Override
    public void onLoad()
    {
        super.onLoad();
        acceleratorNode.onMoveCallback = (particle) -> accelerate(particle);
    }

    @Override
    public void update() //TODO replace with chunk update event and block update event to reduce tick lag
    {
        if (timer-- <= 0)
        {
            //Start timer
            timer = 100 + world.rand.nextInt(100);
            updateLayout(world);
        }
    }

    /**
     * Called each tick the particle moves inside the tube
     *
     * @param particle
     */
    public void accelerate(AcceleratorParticle particle)
    {
        particle.addEnergy(magnetPower);
        particle.addVelocity(getAcceleration());
    }

    /**
     * Calculates the acceleration to apply to particles passing through the tube
     *
     * @return acceleration, max of 0.1f currently
     */
    public float getAcceleration()
    {
        return Math.min(.1f, magnetPower / 10f);
    }

    /**
     * Called to update the layout of magnet
     *
     * @param worldAccess - world to use for checking layout
     */
    public void updateLayout(final IBlockAccess worldAccess)
    {
        //Check layout
        if (isLayoutInvalid = scanForMagnets(worldAccess))
        {
            //If invalid layout, clear magnet ownership
            magnetPosList.forEach(magnet ->
            {
                TileEntity tile = worldAccess.getTileEntity(magnet.pos());
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

    /**
     * Calculates the power of the magnets
     *
     * @return power of magnets
     */
    public float calculateMagnetPower()
    {
        float power = 0;
        for (MagnetPos magnetPos : magnetPosList)
        {
            power += magnetPos.power();
        }

        return power;
    }

    /**
     * Called by {@link #updateLayout(IBlockAccess)} to scan for
     * magnets connected to the tube. Either directly connected
     * or indirectly connected via another magnet.
     * <p>
     * Uses a pathfinder to get magnets and forces magnets
     * to be in the same axis as the tube. This means
     * that magnets in front or behind of the tube are ignored.
     *
     * @param worldAccess - world to use for scanning
     * @return true if current scan does not match last scan
     */
    public boolean scanForMagnets(final IBlockAccess worldAccess)
    {
        magnetPosList.clear();

        final Set<BlockPos> alreadySearched = new HashSet();
        final Queue<BlockPos> queue = new LinkedList();

        //Add self as start
        queue.offer(getPos());
        alreadySearched.add(getPos());

        //Get directions to path
        final List<EnumFacing> directions = new ArrayList(4);
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
            final BlockPos pos = queue.poll();

            //Loop direction per post
            for (EnumFacing direction : directions)
            {
                //Check that we have not already pathed
                final BlockPos next = pos.offset(direction);
                if (!alreadySearched.contains(next))
                {
                    //Mark as already pathed
                    alreadySearched.add(next);

                    //Get Tile
                    final TileEntity tile = worldAccess.getTileEntity(next);

                    //Get power
                    final float power = AcceleratorHelpers.getMagnetPower(tubeCap, tile, direction);
                    if (power > 0)
                    {
                        magnetPosList.add(new MagnetPos(next, power));
                        queue.offer(next);
                    }
                }
            }
        }

        return true;
    }

}
