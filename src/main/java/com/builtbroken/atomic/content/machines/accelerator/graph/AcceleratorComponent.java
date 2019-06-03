package com.builtbroken.atomic.content.machines.accelerator.graph;

import com.builtbroken.atomic.api.accelerator.IAcceleratorComponent;
import net.minecraft.util.math.BlockPos;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-29.
 */
public abstract class AcceleratorComponent implements IAcceleratorComponent
{
    private final IntSupplier dimSupplier;
    private final Supplier<BlockPos> posSupplier;

    public AcceleratorComponent(IntSupplier dimSupplier, Supplier<BlockPos> posSupplier)
    {
        this.dimSupplier = dimSupplier;
        this.posSupplier = posSupplier;
    }

    @Override
    public int dim()
    {
        return dimSupplier != null ? dimSupplier.getAsInt() : 0;
    }

    //<editor-fold desc="Pos3D">

    @Override
    public BlockPos getPos()
    {
        return posSupplier.get();
    }

    @Override
    public double x()
    {
        return getPos().getX() + 0.5;
    }

    @Override
    public double y()
    {
        return getPos().getY() + 0.5;
    }

    @Override
    public double z()
    {
        return getPos().getZ() + 0.5;
    }

    @Override
    public float xf()
    {
        return getPos().getX() + 0.5f;
    }

    @Override
    public float yf()
    {
        return getPos().getY() + 0.5f;
    }

    @Override
    public float zf()
    {
        return getPos().getZ() + 0.5f;
    }

    @Override
    public int xi()
    {
        return getPos().getX();
    }

    @Override
    public int yi()
    {
        return getPos().getY();
    }

    @Override
    public int zi()
    {
        return getPos().getZ();
    }
    //</editor-fold>
}
