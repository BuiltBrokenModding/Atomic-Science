package com.builtbroken.atomic.content.machines.accelerator.graph;

import com.builtbroken.atomic.api.accelerator.IAcceleratorNode;
import com.builtbroken.atomic.api.accelerator.IAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-29.
 */
public class AcceleratorFakeTube implements IAcceleratorTube
{
    public int dim = 0;
    public BlockPos pos;
    public EnumFacing facing;
    public boolean isDead = false;

    public TubeConnectionType tubeConnectionType;

    public AcceleratorNode node;

    public AcceleratorFakeTube(int dim, BlockPos pos, EnumFacing facing, TubeConnectionType tubeConnectionType)
    {
        this.dim = dim;
        this.pos = pos;
        this.facing = facing;
        this.tubeConnectionType = tubeConnectionType;
        this.node = new AcceleratorNode(this);
    }

    public AcceleratorFakeTube(int dim, BlockPos pos, EnumFacing facing, TubeConnectionType tubeConnectionType, AcceleratorNode node)
    {
        this.dim = dim;
        this.pos = pos;
        this.facing = facing;
        this.tubeConnectionType = tubeConnectionType;
        this.node = node;
    }

    @Override
    public int dim()
    {
        return dim;
    }

    @Override
    public boolean isDead()
    {
        return isDead;
    }

    @Override
    public void markDirty()
    {

    }

    @Nonnull
    @Override
    public BlockPos getPosition()
    {
        return pos;
    }

    @Nonnull
    @Override
    public EnumFacing getDirection()
    {
        return facing;
    }

    @Nonnull
    @Override
    public AcceleratorNode getNode()
    {
        return node;
    }

    @Override
    public TubeConnectionType getConnectionType()
    {
        return tubeConnectionType;
    }
}
