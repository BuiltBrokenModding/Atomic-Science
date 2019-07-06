package com.builtbroken.atomic.lib.thermal;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntSupplier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 5/11/2018.
 */
public class ThermalHandler
{
    private static final Map<Block, HeatSpreadFunction> blockHeatFunction = new HashMap();

    private static final Map<Material, IntSupplier> materialGiveRate = new HashMap();
    private static final Map<Material, IntSupplier> materialReceiveRate = new HashMap();

    private static final Map<Block, IntSupplier> blockGiveRate = new HashMap();
    private static final Map<Block, IntSupplier> blockReceiveRate = new HashMap();


    public static void init()
    {
        setHeatMoveRate(Blocks.IRON_BLOCK, 5000);
        setHeatMoveRate(Blocks.WATER, 100, 1000);
        setHeatMoveRate(Blocks.FLOWING_WATER, 100, 1000);
        blockHeatFunction.put(Blocks.WATER, (self, target) -> {
            if (self.getMaterial() == target.getMaterial())
            {
                return 1000;
            }
            return -1;
        });
        blockHeatFunction.put(Blocks.FLOWING_WATER, (self, target) -> {
            if (self.getMaterial() == target.getMaterial())
            {
                return 1000;
            }
            return -1;
        });
    }

    public static void setHeatMoveRate(Block block, int heat)
    {
        setHeatMoveRate(block, heat, heat);
    }

    public static void setHeatMoveRate(Block block, int give, int receive)
    {
        blockGiveRate.put(block, () -> give);
        blockReceiveRate.put(block, () -> receive);
    }

    public static int getHeatMoveWeight(IBlockState self, IBlockState target)
    {
        final Block selfBlock = self.getBlock();
        if (blockHeatFunction.containsKey(selfBlock))
        {
            int weight = blockHeatFunction.get(selfBlock).getSpreadWeight(self, target);
            if (weight >= 0)
            {
                return weight;
            }
        }
        return (int) Math.min(getBlockReceiveWeight(target), getBlockGiveWeight(self));
    }

    public static int getBlockGiveWeight(IBlockState state)
    {
        final Block block = state.getBlock();
        if (blockGiveRate.containsKey(block))
        {
            return blockGiveRate.get(block).getAsInt();
        }

        final Material material = state.getMaterial();
        if (materialGiveRate.containsKey(material))
        {
            return materialGiveRate.get(material).getAsInt();
        }
        return 100;
    }

    public static float getBlockReceiveWeight(IBlockState state)
    {
        final Block block = state.getBlock();
        if (blockReceiveRate.containsKey(block))
        {
            return blockReceiveRate.get(block).getAsInt();
        }

        final Material material = state.getMaterial();
        if (materialReceiveRate.containsKey(material))
        {
            return materialReceiveRate.get(material).getAsInt();
        }
        return 1;
    }

    /**
     * Can the block change states due to the thermal system
     *
     * @param world - location
     * @param pos   - location
     * @return true if it is possible to change states
     */
    public static boolean canChangeStates(World world, BlockPos pos)
    {
        return false;
    }

    public static void changeStates(World world, BlockPos pos)
    {
        /*
        ThermalData data = getThermalData(world, pos);
        if (data != null && data.blockFactory != null)
        {
            float mass = MassHandler.getMass(world, pos);
            double stateChangeEnergy = data.energyToChangeStates(mass);
            double energyToGetToChange = data.energyToGetToStateChange(mass);
            PlacementQueue.queue(new ThermalPlacement(world, pos, data, (long) (stateChangeEnergy + energyToGetToChange)).delay(1 + (int) (Math.random() * 10)));
        }
        */
    }

}
