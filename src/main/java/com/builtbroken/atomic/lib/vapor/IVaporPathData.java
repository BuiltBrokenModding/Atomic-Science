package com.builtbroken.atomic.lib.vapor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-03.
 */
public interface IVaporPathData
{

    /**
     * Checks if the block will allow vapor to pass through it
     *
     * @param world
     * @param pos
     * @param state
     * @return true to allow
     */
    boolean canVaporPassThrough(IBlockAccess world, BlockPos pos, IBlockState state);
}
