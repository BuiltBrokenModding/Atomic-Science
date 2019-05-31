package com.builtbroken.atomic.content.machines.accelerator.tube.imp;

import com.builtbroken.atomic.api.accelerator.IAcceleratorNode;
import com.builtbroken.atomic.api.accelerator.IAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/20/2019.
 */
public class AcceleratorTubeCap implements IAcceleratorTube
{
    private final TileEntity tileEntity;
    private final Supplier<EnumFacing> direction;

    protected final AcceleratorNode acceleratorNode = new AcceleratorNode(this);

    public AcceleratorTubeCap(TileEntity tileEntity, Supplier<EnumFacing> direction)
    {
        this.tileEntity = tileEntity;
        this.direction = direction;
    }

    @Override
    public int dim()
    {
        if (tileEntity.getWorld() == null || tileEntity.getWorld().provider == null)
        {
            return 0;
        }
        return tileEntity.getWorld().provider.getDimension();
    }

    @Override
    public boolean isDead()
    {
        return tileEntity.isInvalid();
    }

    @Override
    public void markDirty()
    {
        tileEntity.markDirty();
    }

    @Nonnull
    @Override
    public BlockPos getPosition()
    {
        return tileEntity.getPos();
    }

    @Nonnull
    @Override
    public EnumFacing getDirection()
    {
        return direction.get();
    }

    @Nonnull
    @Override
    public AcceleratorNode getNode()
    {
        return acceleratorNode;
    }
}
