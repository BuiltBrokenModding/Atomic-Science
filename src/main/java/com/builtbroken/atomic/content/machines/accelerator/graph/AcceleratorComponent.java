package com.builtbroken.atomic.content.machines.accelerator.graph;

import com.builtbroken.atomic.api.accelerator.IAcceleratorComponent;
import net.minecraft.util.math.BlockPos;

import java.util.function.IntSupplier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-29.
 */
public abstract class AcceleratorComponent implements IAcceleratorComponent
{
    private final IntSupplier dimSupplier;
    private BlockPos pos = BlockPos.ORIGIN;

    public AcceleratorComponent(IntSupplier dimSupplier)
    {
        this.dimSupplier = dimSupplier;
    }

    @Override
    public int dim()
    {
        return dimSupplier != null ? dimSupplier.getAsInt() : 0;
    }

    //<editor-fold desc="Pos3D">

    public void setPos(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public BlockPos getPos()
    {
        return pos;
    }

    @Override
    public double x()
    {
        return pos.getX() + 0.5;
    }

    @Override
    public double y()
    {
        return pos.getY() + 0.5;
    }

    @Override
    public double z()
    {
        return pos.getZ() + 0.5;
    }

    @Override
    public float xf()
    {
        return pos.getX() + 0.5f;
    }

    @Override
    public float yf()
    {
        return pos.getY() + 0.5f;
    }

    @Override
    public float zf()
    {
        return pos.getZ() + 0.5f;
    }

    @Override
    public int xi()
    {
        return pos.getX();
    }

    @Override
    public int yi()
    {
        return pos.getY();
    }

    @Override
    public int zi()
    {
        return pos.getZ();
    }
    //</editor-fold>
}
