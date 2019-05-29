package com.builtbroken.jlib.data.vector;

import net.minecraft.util.math.BlockPos;

/**
 * Useful interface to define that an object has a 3D location.
 *
 * @author DarkGuardsman
 */
public interface IPos3D extends IPos2D
{

    double z();

    default float zf()
    {
        return (float) z();
    }

    default int zi()
    {
        return (int) Math.floor(z());
    }

    /**
     * Converts the position into a block pos.
     * <p>
     * Do not call this often as it may create
     * a new position each time its called. Which
     * can waste memory
     *
     * @return BlockPos using integer based coordinates of the position
     */
    default BlockPos getPos()
    {
        return new BlockPos(xi(), yi(), zi());
    }
}
