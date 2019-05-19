package com.builtbroken.atomic.content.machines.pipe.imp;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.prefab.TileEntityPrefab;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Generic pipe that acts as a direction path for connecting to other machines
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2019.
 */
public abstract class TileEntityDirectionalPipe extends TileEntityPrefab
{
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (facing != null && canSupportDirection(facing) && canSupport(capability))
        {
            //Get inverse side of connection
            //      EX: connection top is passed to bottom
            final EnumFacing outputDirection = getOutDirection(facing);
            final EnumFacing sideToAccess = outputDirection.getOpposite();
            TileEntity tile = world.getTileEntity(getPos().offset(outputDirection));

            //Find target tile, prevents looping to self
            if (tile instanceof TileEntityDirectionalPipe)
            {
                tile = ((TileEntityDirectionalPipe) tile).getTargetTile(capability, sideToAccess, this);
            }

            //Check if machine is supported
            if (tile != null && canSupport(tile))
            {
                boolean hasCap = tile.hasCapability(capability, sideToAccess);
                return hasCap;
            }
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (facing != null && canSupportDirection(facing) && canSupport(capability))
        {
            //Get inverse side of connection
            //      EX: connection top is passed to bottom
            final EnumFacing outputDirection = getOutDirection(facing);
            final EnumFacing sideToAccess = outputDirection.getOpposite();
            TileEntity tile = world.getTileEntity(getPos().offset(outputDirection));

            //Find target tile, prevents looping to self
            if (tile instanceof TileEntityDirectionalPipe)
            {
                tile = ((TileEntityDirectionalPipe) tile).getTargetTile(capability, sideToAccess, this);
            }

            //Get capability
            if (tile != null && canSupport(tile))
            {

                T r = tile.getCapability(capability, sideToAccess);
                if (r != null)
                {
                    return r;
                }
            }
        }
        return super.getCapability(capability, facing);
    }

    /**
     * Look for target of pipe path
     *
     * @param capability - capability being searched for
     * @param sideAccessed     - direction being accessed
     * @param source     - source of the path start
     * @return tile found that is not a pipe, or null for end of path
     */
    protected TileEntity getTargetTile(Capability capability, EnumFacing sideAccessed, TileEntity source)
    {
        //Check if we can support the connection
        if (canSupportDirection(sideAccessed) && canSupport(capability))
        {
            //Change direction
            final EnumFacing outDirection = getOutDirection(sideAccessed);

            final TileEntity tile = world.getTileEntity(getPos().offset(outDirection));
            if (canSupport(tile))
            {
                //Loop prevention
                if (tile == source)
                {
                    if(AtomicScience.runningAsDev)
                    {
                        AtomicScience.logger.error("TileEntityDirectionalPipe: Infinite loop detected from " + source + " to " + this);
                    }
                    return null;
                }

                //Next pipe
                if (tile instanceof TileEntityDirectionalPipe)
                {
                    final EnumFacing sideToAccess = outDirection.getOpposite();
                    return ((TileEntityDirectionalPipe) tile).getTargetTile(capability, sideToAccess, this);
                }

                //End condition
                return tile;
            }
        }
        return null;
    }

    protected EnumFacing getOutDirection(EnumFacing input)
    {
        return getDirection();
    }

    /**
     * Can support connection pass through on this side
     *
     * @param facing
     * @return true if can pass connections to other side
     */
    public abstract boolean canSupportDirection(EnumFacing facing);

    /**
     * Can support passing through the capability into the block under
     *
     * @param capability
     * @return
     */
    public abstract boolean canSupport(Capability capability);

    /**
     * Can support passing through the capability into the block under
     *
     * @param tileEntity
     * @return
     */
    public boolean canSupport(TileEntity tileEntity)
    {
        return true;
    }
}
