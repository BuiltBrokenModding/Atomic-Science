package com.builtbroken.atomic.lib.vapor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-03.
 */
public interface IVaporData
{

    /**
     * Calculates the amount of vapor for the given block.
     * <p>
     * Its recommend for performance to supply a simple function that
     * uses the temperature only. This way runtime of larger setups
     * is maintained without slowing down due to accessing a lot of data.
     *
     * @param world
     * @param pos
     * @param state
     * @param temperature
     * @return
     */
    int getVapor(World world, BlockPos pos, IBlockState state, double temperature);

    /**
     * Smallest amount of vapor for the block
     *
     * @param world
     * @param pos
     * @param state
     * @return
     */
    int getMin(World world, BlockPos pos, IBlockState state);

    /**
     * Largest amount of vapor for the block
     *
     * @param world
     * @param pos
     * @param state
     * @return
     */
    int getMax(World world, BlockPos pos, IBlockState state);

    /**
     * Minimal temperature to start producing noticeable amounts of vapor
     *
     * @param world
     * @param pos
     * @param state
     * @return
     */
    double getTemperature(World world, BlockPos pos, IBlockState state);

    /**
     * Can the block's subtype be supported as a producer of vapor.
     * <p>
     * Use this method to allow subtypes to produce vapor while
     * other subtypes do not. As well provide conditional handling
     * of how vapor is produced based on world location.
     *
     * @param world
     * @param pos
     * @param state
     * @return true if is supported
     */
    boolean isSupported(IBlockAccess world, BlockPos pos, IBlockState state);
}
