package com.builtbroken.atomic.lib;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/11/2018.
 */
public class MassHandler
{
    private static final HashMap<Block, Float> blockToMass = new HashMap();
    private static final HashMap<Material, Float> materialToMass = new HashMap();

    public static void init()
    {
        blockToMass.put(Blocks.IRON_BLOCK, 7870f);
        materialToMass.put(Material.IRON, 7870f);

        blockToMass.put(Blocks.AIR, 1.225f);
        materialToMass.put(Material.AIR, 1.225f);

        blockToMass.put(Blocks.STONE, 1120f);
        materialToMass.put(Material.ROCK, 1120f);

        materialToMass.put(Material.WATER, 1000f);
    }

    /**
     * Gets the mass of the object in the world
     *
     * @param world - location
     * @param pos   - location
     * @return mass in kilograms
     */
    public static float getMass(World world, BlockPos pos)
    {
        IBlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock(); //TODO add IBLockState support
        if (blockToMass.containsKey(block))
        {
            return blockToMass.get(block);
        }
        else if (materialToMass.containsKey(block.getMaterial(blockState)))
        {
            return materialToMass.get(block.getMaterial(blockState));
        }
        //https://www.simetric.co.uk/si_materials.htm
        return 1000;
    }
}
