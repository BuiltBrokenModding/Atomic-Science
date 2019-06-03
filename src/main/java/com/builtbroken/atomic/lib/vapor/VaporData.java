package com.builtbroken.atomic.lib.vapor;

import com.builtbroken.atomic.config.logic.ConfigLogic;
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

    public DoubleSupplier tempatureKelvin;
    public IntSupplier vaporMax;
    public IntSupplier vaporMin;
    public VaporCalculation calculation;


    public VaporData(DoubleSupplier tempatureKelvin, IntSupplier vaporMin, IntSupplier vaporMax)
    {
        this.tempatureKelvin = tempatureKelvin;
        this.vaporMin = vaporMin;
        this.vaporMax = vaporMax;
    }

    @Override
    public int getVapor(World world, BlockPos pos, IBlockState state, double temperature)
    {
        final double tempMin = getTemperature(world, pos, state);
        if (temperature > tempMin &&  tempMin > 0)
        {
            if (calculation != null)
            {
                return calculation.calculateVapor(world, pos, state, temperature);
            }
            return (int) Math.min(getMax(world, pos, state), Math.ceil(getMin(world, pos, state) * (temperature / tempMin)));
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
    public double getTemperature(World world, BlockPos pos, IBlockState state)
    {
        if (tempatureKelvin != null)
        {
            return tempatureKelvin.getAsDouble();
        }
        return 0;
    }

    @Override
    public boolean isSupported(IBlockAccess world, BlockPos pos, IBlockState state)
    {
        return true;
    }
}
