package com.builtbroken.atomic.api.accelerator;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/17/2019.
 */
public interface IAcceleratorMagnet
{
    /**
     * Gets raw power output of the magnet
     *
     * @return
     */
    default float getRawMagneticPower()
    {
        return 10;
    }


    /**
     * Gets actual power of the magnet in relation to the tube
     *
     * @param tube
     * @return
     */
    default float getActualMagneticPower(IAcceleratorTube tube) //TODO add current structure as a pass in arg
    {
        final BlockPos tubePos = tube.getPosition();
        final BlockPos magnetPos = getPosition();

        float deltaX = Math.abs(tubePos.getX() - magnetPos.getX());
        float deltaY = Math.abs(tubePos.getY() - magnetPos.getY());
        float deltaZ = Math.abs(tubePos.getZ() - magnetPos.getZ());
        float manhattanDistance = deltaX + deltaY + deltaZ;

        return (1f / manhattanDistance) * getRawMagneticPower();
    }

    /**
     * Wrappers to {@link TileEntity#getPos()}
     *
     * @return
     */
    BlockPos getPosition();
}
