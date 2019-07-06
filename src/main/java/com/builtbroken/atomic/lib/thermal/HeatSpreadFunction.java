package com.builtbroken.atomic.lib.thermal;

import net.minecraft.block.state.IBlockState;

/**
 * Created by Dark(DarkGuardsman, Robert) on 7/5/2019.
 */
@FunctionalInterface
public interface HeatSpreadFunction
{
    int getSpreadWeight(IBlockState self, IBlockState target);
}
