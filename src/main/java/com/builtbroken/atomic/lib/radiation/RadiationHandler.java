package com.builtbroken.atomic.lib.radiation;

import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.api.radiation.IRadiationResistant;
import com.builtbroken.atomic.config.logic.ConfigRadiation;
import com.builtbroken.atomic.content.effects.effects.FloatSupplier;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 10/11/2018.
 */
public class RadiationHandler
{
    public static final HashMap<IBlockState, RadiationResistanceSupplier> blockStateToRadiationPercentage = new HashMap();
    public static final HashMap<Block, RadiationResistanceSupplier> blockToRadiationPercentage = new HashMap();
    public static final HashMap<Material, FloatSupplier> materialToRadiationPercentage = new HashMap();

    public static void init()
    {
        //These values are not based on realism in order to make the game enjoyable
        setValue(Material.ROCK, () -> ConfigRadiation.RADIATION_DECAY_STONE);
        setValue(Material.PISTON, () -> ConfigRadiation.RADIATION_DECAY_STONE);

        setValue(Material.GROUND, () -> ConfigRadiation.RADIATION_DECAY_STONE / 2);
        setValue(Material.GRASS, () -> ConfigRadiation.RADIATION_DECAY_STONE / 2);
        setValue(Material.SAND, () -> ConfigRadiation.RADIATION_DECAY_STONE / 2);
        setValue(Material.CLAY, () -> ConfigRadiation.RADIATION_DECAY_STONE / 2);

        setValue(Material.ICE, () -> ConfigRadiation.RADIATION_DECAY_STONE / 3);
        setValue(Material.PACKED_ICE, () -> ConfigRadiation.RADIATION_DECAY_STONE / 3);
        setValue(Material.CRAFTED_SNOW, () -> ConfigRadiation.RADIATION_DECAY_STONE / 3);

        setValue(Material.IRON, () -> ConfigRadiation.RADIATION_DECAY_METAL);

        setValue(Blocks.BRICK_BLOCK, (world, pos, state) -> ConfigRadiation.RADIATION_DECAY_STONE * 1.2f);
        setValue(Blocks.NETHER_BRICK, (world, pos, state) -> ConfigRadiation.RADIATION_DECAY_STONE * 1.2f);
        setValue(Blocks.STONEBRICK, (world, pos, state) -> ConfigRadiation.RADIATION_DECAY_STONE * 1.1f);
        setValue(Blocks.QUARTZ_BLOCK, (world, pos, state) -> ConfigRadiation.RADIATION_DECAY_STONE * 1.4f);
        setValue(Blocks.DIAMOND_BLOCK, (world, pos, state) -> ConfigRadiation.RADIATION_DECAY_STONE * 2f);
        setValue(Blocks.GOLD_BLOCK, (world, pos, state) -> ConfigRadiation.RADIATION_DECAY_STONE * 1.3f);
        setValue(Blocks.OBSIDIAN, (world, pos, state) -> ConfigRadiation.RADIATION_DECAY_STONE * 1.5f);
        setValue(Blocks.COBBLESTONE, (world, pos, state) -> ConfigRadiation.RADIATION_DECAY_STONE * 0.7f);
        setValue(Blocks.MOSSY_COBBLESTONE, (world, pos, state) -> ConfigRadiation.RADIATION_DECAY_STONE * 0.8f);
        setValue(Blocks.IRON_DOOR, (world, pos, state) -> ConfigRadiation.RADIATION_DECAY_METAL / 3);
        setValue(Blocks.IRON_BARS, (world, pos, state) -> ConfigRadiation.RADIATION_DECAY_METAL / 4);
        setValue(Blocks.CONCRETE, (world, pos, state) -> ConfigRadiation.RADIATION_DECAY_METAL * 2);

        //TODO add JSON data to allow users to customize values

        //Sources to base values on, do not use real values as 1m dirt can block radiation easily
        //https://en.wikipedia.org/wiki/Radiation_material_science
        //https://en.wikipedia.org/wiki/Radiation_protection
        //https://en.wikipedia.org/wiki/Half-value_layer
    }

    public static void setValue(Block block, RadiationResistanceSupplier supplier)
    {
        blockToRadiationPercentage.put(block, supplier);
    }

    public static void setValue(IBlockState blockState, RadiationResistanceSupplier supplier)
    {
        blockStateToRadiationPercentage.put(blockState, supplier);
    }

    public static void setValue(Material material, FloatSupplier supplier)
    {
        materialToRadiationPercentage.put(material, supplier);
    }

    /**
     * Called to get the generalized radiation resistance of a block at the position
     *
     * @param world - location
     * @param xi-   location
     * @param yi-   location
     * @param zi-   location
     * @return value between 0.0 and 1.0
     */
    public static float getReduceRadiationForBlock(World world, int xi, int yi, int zi) //TODO make a directional version
    {
        final BlockPos pos = new BlockPos(xi, yi, zi);
        final IBlockState blockState = world.getBlockState(pos);
        final Block block = blockState.getBlock();

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity.hasCapability(AtomicScienceAPI.RADIATION_RESISTANT_CAPABILITY, null))
        {
            IRadiationResistant radiationResistant = tileEntity.getCapability(AtomicScienceAPI.RADIATION_RESISTANT_CAPABILITY, null);
            if (radiationResistant != null)
            {
                return radiationResistant.getRadiationResistance();
            }
        }
        else if (blockStateToRadiationPercentage.containsKey(blockState))
        {
            return blockStateToRadiationPercentage.get(blockState).getAsFloat(world, pos, blockState);
        }
        else if (blockToRadiationPercentage.containsKey(block))
        {
            return blockToRadiationPercentage.get(block).getAsFloat(world, pos, blockState);
        }
        else if (!block.isAir(blockState, world, pos))
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
                    return ConfigRadiation.RADIATION_DECAY_PER_BLOCK;
                }
                return ConfigRadiation.RADIATION_DECAY_PER_BLOCK / 2;
            }
            else if (blockState.getMaterial().isLiquid())
            {
                return ConfigRadiation.RADIATION_DECAY_PER_FLUID;
            }
        }
        return 0;
    }

    /**
     * Called to reduce the radiation value
     *
     * @param world - location
     * @param xi    - location
     * @param yi    - location
     * @param zi    - location
     * @param power - radiation value
     * @return reduced value
     */
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
     * @param power - starting value
     * @return distance
     */
    public static double getDecayRange(int power)
    {
        return Math.sqrt(power + 1 / 0.5);
    }
}
