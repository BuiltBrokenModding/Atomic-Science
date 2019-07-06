package com.builtbroken.atomic.lib.vapor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-03.
 */
@FunctionalInterface
public interface VaporCalculation
{
    int calculateVapor(World world, BlockPos pos, IBlockState state, int heat);
}
