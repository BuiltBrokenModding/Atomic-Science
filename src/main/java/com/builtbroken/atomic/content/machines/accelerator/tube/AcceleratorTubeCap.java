package com.builtbroken.atomic.content.machines.accelerator.tube;

import com.builtbroken.atomic.api.accelerator.IAcceleratorNode;
import com.builtbroken.atomic.api.accelerator.IAcceleratorTube;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/20/2019.
 */
public class AcceleratorTubeCap implements IAcceleratorTube
{
    Supplier<BlockPos> posSupplier;
    Supplier<IAcceleratorNode> nodeSupplier;

    public AcceleratorTubeCap(Supplier<BlockPos> posSupplier, Supplier<IAcceleratorNode> nodeSupplier)
    {
        this.posSupplier = posSupplier;
        this.nodeSupplier = nodeSupplier;
    }

    @Nonnull
    @Override
    public BlockPos getPosition()
    {
        return posSupplier.get();
    }

    @Nonnull
    @Override
    public IAcceleratorNode getNode()
    {
        return nodeSupplier.get();
    }
}
