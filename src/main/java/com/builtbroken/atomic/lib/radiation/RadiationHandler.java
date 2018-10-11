package com.builtbroken.atomic.lib.radiation;

import com.builtbroken.atomic.config.logic.ConfigRadiation;
import com.builtbroken.atomic.content.effects.effects.FloatSupplier;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/11/2018.
 */
public class RadiationHandler
{
    public static final HashMap<Block, FloatSupplier> blockToRadiationPercentage = new HashMap();
    public static final HashMap<Material, FloatSupplier> materialToRadiationPercentage = new HashMap();

    public static void init()
    {
        setValue(Material.ROCK, () -> ConfigRadiation.RADIATION_DECAY_STONE);

        setValue(Material.GROUND, () -> ConfigRadiation.RADIATION_DECAY_STONE / 2);
        setValue(Material.GRASS, () -> ConfigRadiation.RADIATION_DECAY_STONE / 2);
        setValue(Material.SAND, () -> ConfigRadiation.RADIATION_DECAY_STONE / 2);
        setValue(Material.CLAY, () -> ConfigRadiation.RADIATION_DECAY_STONE / 2);

        setValue(Material.ICE, () -> ConfigRadiation.RADIATION_DECAY_STONE / 3);
        setValue(Material.PACKED_ICE, () -> ConfigRadiation.RADIATION_DECAY_STONE / 3);
        setValue(Material.CRAFTED_SNOW, () -> ConfigRadiation.RADIATION_DECAY_STONE / 3);

        setValue(Material.IRON, () -> ConfigRadiation.RADIATION_DECAY_METAL);

        //TODO add JSON data to allow users to customize values
    }

    public static void setValue(Block block, FloatSupplier supplier)
    {
        blockToRadiationPercentage.put(block, supplier);
    }

    public static void setValue(Material material, FloatSupplier supplier)
    {
        materialToRadiationPercentage.put(material, supplier);
    }

    public static float getReduceRadiationForBlock(World world, int xi, int yi, int zi) //TODO move to handler
    {
        //TODO add registry that allows decay per block & meta
        //TODO add interface to define radiation based on tile data


        //Decay power per block
        final BlockPos pos = new BlockPos(xi, yi, zi);
        final IBlockState blockState = world.getBlockState(pos);
        final Block block = blockState.getBlock();

        if (!block.isAir(blockState, world, pos))
        {
            if (blockState.getMaterial().isSolid())
            {
                if (blockState.isOpaqueCube())
                {
                    final Material material = blockState.getMaterial();
                    if (materialToRadiationPercentage.containsKey(material))
                    {
                        return materialToRadiationPercentage.get(material).getAsFloat();
                    }
                    else
                    {
                        return ConfigRadiation.RADIATION_DECAY_PER_BLOCK;
                    }
                }
                else
                {
                    return ConfigRadiation.RADIATION_DECAY_PER_BLOCK / 2;
                }
            }
            else if (blockState.getMaterial().isLiquid())
            {
                return ConfigRadiation.RADIATION_DECAY_PER_FLUID;
            }
        }
        return 0;
    }

    public static double reduceRadiationForBlock(World world, int xi, int yi, int zi, double power)
    {
        //Get reduction
        float reduction = getReduceRadiationForBlock(world, xi, yi, zi);

        //TODO add system to allow per block flat limit, then apply greater (limit or percentage)
        //TODO add an upper limit, how much radiation a block can stop, pick small (limit or percentage)
        //Flat line
        if (power < reduction * 1000)
        {
            return 0;
        }

        //Reduce if not flat
        power -= power * reduction;


        //Calculate radiation
        return power;
    }

    /**
     * Converts material value to rad
     *
     * @param material_amount - amount of material
     * @return rad value
     */
    public static int getRadFromMaterial(int material_amount)
    {
        return (int) Math.ceil(material_amount * ConfigRadiation.MAP_VALUE_TO_MILI_RAD);
    }

    /**
     * Gets radiation value for the given distance
     *
     * @param power      - ordinal power at 1 meter
     * @param distanceSQ - distance to get current
     * @return distance reduced value, if less than 1 will return full
     */
    public static double getRadForDistance(double power, double distanceSQ)
    {
        //its assumed power is measured at 1 meter from source
        return getRadForDistance(power, 1, distanceSQ);
    }

    /**
     * Gets radiation value for the given distance
     *
     * @param power            - ordinal power at 1 meter
     * @param distanceSourceSQ - distance from source were the power was measured
     * @param distanceSQ       - distance to get current
     * @return distance reduced value, if less than 1 will return full
     */
    public static double getRadForDistance(double power, double distanceSourceSQ, double distanceSQ)
    {
        //http://www.nde-ed.org/GeneralResources/Formula/RTFormula/InverseSquare/InverseSquareLaw.htm
        if (distanceSQ < distanceSourceSQ)
        {
            return power;
        }

        //I_2 = I * D^2 / D_2^2
        return (power * distanceSourceSQ) / distanceSQ;
    }

    /**
     * At what point does radiation power drop below 1
     *
     * @param value - starting value
     * @return distance
     */
    public static double getDecayRange(int value)
    {
        double power = value;
        double distance = 1;
        while (power > 1)
        {
            distance += 0.5;
            power = getRadForDistance(value, distance);
        }
        return distance;
    }
}
