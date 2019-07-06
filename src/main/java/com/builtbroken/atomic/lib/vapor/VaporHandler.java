package com.builtbroken.atomic.lib.vapor;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.config.logic.ConfigLogic;
import com.builtbroken.atomic.map.MapHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.function.IntSupplier;

/**
 * Handles vapor data for blocks.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-03.
 */
public class VaporHandler
{
    public static final int WATER_HEAT_MIN = 100;

    private static final HashMap<Block, IVaporData> vaporDataMap = new HashMap();
    private static final HashMap<Block, IVaporPathData> vaporPathMap = new HashMap();

    static
    {
        setVaporData(Blocks.FLOWING_WATER, () -> WATER_HEAT_MIN, () -> ConfigLogic.STEAM.WATER_FLOWING_VAPOR_RATE, () -> ConfigLogic.STEAM.WATER_VAPOR_MAX_RATE);
        setVaporData(Blocks.WATER, () -> WATER_HEAT_MIN, () -> ConfigLogic.STEAM.WATER_VAPOR_RATE, () -> ConfigLogic.STEAM.WATER_VAPOR_MAX_RATE);

        allowVaporThrough(Blocks.TRAPDOOR);
        allowVaporThrough(Blocks.IRON_BARS);
    }

    /**
     * Called to add simple vapor data for a block
     *
     * @param block    - block to specify for and all its subtypes
     * @param minHeat  - minimal heat before vapor is generated at a noticeable rate
     * @param vaporMin - smallest amount of vapor to produce
     * @param vaporMax - largest amount of vapor to produce
     */
    public static void setVaporData(Block block, IntSupplier minHeat, IntSupplier vaporMin, IntSupplier vaporMax)
    {
        if (!vaporDataMap.containsKey(block))
        {
            vaporDataMap.put(block, new VaporData(minHeat, vaporMin, vaporMax));
        }
        else
        {
            AtomicScience.logger.error("VaporHandler: Something tried to override vapor data for block: " + block);
        }
    }

    /**
     * Sets a block and its subtype to allow vapor to pass through it
     *
     * @param block
     */
    public static void allowVaporThrough(Block block)
    {
        if (!vaporPathMap.containsKey(block))
        {
            vaporPathMap.put(block, new VaporPathData());
        }
    }

    /**
     * Get amount of vapor produced per tick in mb
     *
     * @param world - location
     * @param pos   - location
     * @return vapor in mb
     */
    public static int getVaporRate(World world, BlockPos pos)
    {
        return getVaporRate(world, pos, MapHandler.THERMAL_MAP.getStoredHeat(world, pos));
    }

    /**
     * Get amount of vapor produced per tick in mb
     *
     * @param world - location
     * @param pos   - location
     * @return vapor in mb
     */
    public static int getVaporRate(World world, BlockPos pos, int heat)
    {
        final IBlockState blockState = world.getBlockState(pos);
        final Block block = blockState.getBlock();
        if (vaporDataMap.containsKey(block))
        {
            return vaporDataMap.get(block).getVapor(world, pos, blockState, heat);
        }
        return 0;
    }

    /**
     * Called to check if the fluid is supported by the vaporization system
     *
     * @param world
     * @param pos
     * @return
     */
    public static boolean isSupportedVaporFluid(IBlockAccess world, BlockPos pos)
    {
        final IBlockState state = world.getBlockState(pos);
        if (state != null)
        {
            final Block block = world.getBlockState(pos).getBlock();
            if (vaporDataMap.containsKey(block))
            {
                return vaporDataMap.get(block).isSupported(world, pos, state);
            }
        }
        return false;
    }

    /**
     * Called to check if steam can pass through the block
     *
     * @param world
     * @param pos
     * @return
     */
    public static boolean canSteamPassThrough(IBlockAccess world, BlockPos pos)
    {
        final IBlockState state = world.getBlockState(pos);
        if (state != null)
        {
            final Block block = state.getBlock();
            if (vaporPathMap.containsKey(block))
            {
                return vaporPathMap.get(block).canVaporPassThrough(world, pos, state);
            }
            else if (state.getMaterial() == Material.WATER)
            {
                return true;
            }
            else if (block.isAir(state, world, pos))
            {
                return true;
            }
            else if (!block.isCollidable())
            {
                return true;
            }
            final AxisAlignedBB bb = state.getCollisionBoundingBox(world, pos);
            return bb == null || !(bb.minX <= 0 && bb.maxX >= 1 && bb.minZ <= 0 && bb.maxX >= 1);
        }
        return false;
    }


}
