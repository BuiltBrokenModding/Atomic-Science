package com.builtbroken.atomic.content.prefab;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/11/2018.
 */
public abstract class BlockMachine extends BlockPrefab
{
    public BlockMachine(Material mat)
    {
        super(mat);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, ROTATION_PROP);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(ROTATION_PROP, EnumFacing.byIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(ROTATION_PROP).ordinal();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return getDefaultState().withProperty(ROTATION_PROP, placer.getHorizontalFacing());
    }

    public static EnumFacing getPlacement(EnumFacing blockSide, float hitX, float hitY, float hitZ)
    {
        final float spacing = 0.3f;
        EnumFacing placement;

        if (blockSide == EnumFacing.UP || blockSide == EnumFacing.DOWN)
        {
            //WEST
            boolean left = hitX <= spacing;
            //EAST
            boolean right = hitX >= (1 - spacing);
            //NORTH
            boolean up = hitZ <= spacing;
            //SOUTH
            boolean down = hitZ >= (1 - spacing);

            if (!up && !down && (left || right))
            {
                placement = left ? EnumFacing.WEST : EnumFacing.EAST;
            }
            else if (!left && !right && (up || down))
            {
                placement = up ? EnumFacing.NORTH : EnumFacing.SOUTH;
            }
            else if (!left && !right && !up && !down)
            {
                placement = blockSide;
            }
            else
            {
                placement = blockSide.getOpposite();
            }
        }
        else
        {
            boolean z = blockSide.getAxis() == EnumFacing.Axis.Z;
            boolean left = (z ? hitX : hitZ) <= spacing;
            boolean right = (z ? hitX : hitZ) >= (1 - spacing);

            boolean down = hitY <= spacing;
            boolean up = hitY >= (1 - spacing);

            if (!up && !down && (left || right))
            {
                if (z)
                {
                    placement = left ? EnumFacing.WEST : EnumFacing.EAST;
                }
                else
                {
                    placement = left ? EnumFacing.NORTH : EnumFacing.SOUTH;
                }
            }
            else if (!left && !right && (up || down))
            {
                placement = up ? EnumFacing.UP : EnumFacing.DOWN;
            }
            else if (!left && !right && !up && !down)
            {
                placement = blockSide;
            }
            else
            {
                placement = blockSide.getOpposite();
            }
        }
        return placement;
    }
}
