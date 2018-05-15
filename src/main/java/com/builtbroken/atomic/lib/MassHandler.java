package com.builtbroken.atomic.lib;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
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
        blockToMass.put(Blocks.iron_block, 7870f);
        materialToMass.put(Material.iron, 7870f);

        blockToMass.put(Blocks.air, 1.225f);
        materialToMass.put(Material.air, 1.225f);

        blockToMass.put(Blocks.stone, 1120f);
        materialToMass.put(Material.rock, 1120f);

        materialToMass.put(Material.water, 1000f);
    }

    /**
     * Gets the mass of the object in the world
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return mass in kilograms
     */
    public static float getMass(World world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);
        if (blockToMass.containsKey(block))
        {
            return blockToMass.get(block);
        }
        else if (materialToMass.containsKey(block.getMaterial()))
        {
            return materialToMass.get(block.getMaterial());
        }
        //https://www.simetric.co.uk/si_materials.htm
        return 1000;
    }
}
