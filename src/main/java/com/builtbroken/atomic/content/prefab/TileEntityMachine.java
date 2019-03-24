package com.builtbroken.atomic.content.prefab;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/24/2019.
 */
public class TileEntityMachine extends TileEntityActive
{
    public EnumFacing getDirection()
    {
        IBlockState state = world.getBlockState(getPos());
        if(state.getPropertyKeys().contains(BlockMachine.ROTATION_PROP))
        {
            return state.getValue(BlockMachine.ROTATION_PROP);
        }
        return EnumFacing.UP;
    }
}
