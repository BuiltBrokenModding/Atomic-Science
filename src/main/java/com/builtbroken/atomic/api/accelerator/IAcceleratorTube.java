package com.builtbroken.atomic.api.accelerator;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/17/2019.
 */
public interface IAcceleratorTube
{
    /**
     * Wrappers to {@link TileEntity#getPos()}
     *
     * @return
     */
    BlockPos getPosition();
}
