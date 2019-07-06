package com.builtbroken.atomic.lib.vapor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-03.
 */
public class VaporData implements IVaporData
{

    public IntSupplier heatMin;
    public IntSupplier vaporMax;
    public IntSupplier vaporMin;
    public VaporCalculation calculation;


    public VaporData(IntSupplier heat, IntSupplier vaporMin, IntSupplier vaporMax)
    {
        this.heatMin = heat;
        this.vaporMin = vaporMin;
        this.vaporMax = vaporMax;
    }

    @Override
    public int getVapor(World world, BlockPos pos, IBlockState state, int heat)
    {
        final double heatMin = getHeatRequired(world, pos, state);
        if (heat >= heatMin &&  heatMin > 0)
        {
            if (calculation != null)
            {
                return calculation.calculateVapor(world, pos, state, heat);
            }
            return (int) Math.min(getMax(world, pos, state), Math.ceil(getMin(world, pos, state) * (heat / heatMin)));
        }
        return 0;
    }

    @Override
    public int getMin(World world, BlockPos pos, IBlockState state)
    {
        if (vaporMin != null)
        {
            return vaporMin.getAsInt();
        }
        return 0;
    }

    @Override
    public int getMax(World world, BlockPos pos, IBlockState state)
    {
        if (vaporMax != null)
        {
            return vaporMax.getAsInt();
        }
        return 0;
    }

    @Override
    public int getHeatRequired(World world, BlockPos pos, IBlockState state)
    {
        if (heatMin != null)
        {
            return heatMin.getAsInt();
        }
        return 0;
    }

    @Override
    public boolean isSupported(IBlockAccess world, BlockPos pos, IBlockState state)
    {
        return true;
    }
}
