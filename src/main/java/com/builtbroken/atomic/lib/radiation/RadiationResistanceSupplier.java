package com.builtbroken.atomic.lib.radiation;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

@FunctionalInterface
public interface RadiationResistanceSupplier
{
    /**
     * Gets a result.
     *
     * @return a result
     */
    float getAsFloat(World world, int x, int y, int z, IBlockState state);
}