package com.builtbroken.atomic.lib.vapor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-03.
 */
public class VaporPathData implements IVaporPathData
{
    @Override
    public boolean canVaporPassThrough(IBlockAccess world, BlockPos pos, IBlockState state)
    {
        return true;
    }
}
