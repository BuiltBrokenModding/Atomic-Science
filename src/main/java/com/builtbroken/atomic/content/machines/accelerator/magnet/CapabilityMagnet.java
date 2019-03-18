package com.builtbroken.atomic.content.machines.accelerator.magnet;

import com.builtbroken.atomic.api.accelerator.IAcceleratorMagnet;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/17/2019.
 */
public class CapabilityMagnet implements IAcceleratorMagnet
{
    public final TileEntityMagnet magnet;

    public CapabilityMagnet(TileEntityMagnet magnet)
    {
        this.magnet = magnet;
    }

    @Override
    public BlockPos getPosition()
    {
        return magnet.getPos();
    }
}
