package com.builtbroken.atomic.lib.thermal;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntSupplier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 5/11/2018.
 */
public class ThermalHandler
{
    //Heat weight is treated as a percentage of pressure. For each block contacting the surface heat
    //  spread logic will calculate a total and then break it down into a precentage. So if you have two
    //  blocks with 20 and 80. The total heat weight would be 100. The first block would get 20% and the second 80%.

    //Weight of heat given for a material
    private static final Map<Material, IntSupplier> materialGiveRate = new HashMap();

    //Amount of heat the block can store
    private static final Map<Material, IntSupplier> materialCapacity = new HashMap();
    //Amount of heat lost transferring heat
    private static final Map<Material, IntSupplier> materialLoss = new HashMap();

    //Weight of heat given away
    private static final Map<Block, IntSupplier> blockGiveRate = new HashMap();

    //Amount of heat the block can store
    private static final Map<Block, IntSupplier> blockCapacity = new HashMap();
    //Amount of heat lost transferring heat
    private static final Map<Block, IntSupplier> blockLoss = new HashMap();

    public static void init()
    {
        //Materials
        setHeatMoveRate(Material.IRON, 5000, 50, 2);

        //Blocks
        setHeatMoveRate(Blocks.GOLD_BLOCK, 8000, 200, 1);
        setHeatMoveRate(Blocks.WATER, 100, 1000, 0);
        setHeatMoveRate(Blocks.FLOWING_WATER, 100, 1000, 0);

        //Inpassible
        setHeatMoveRate(Blocks.BEDROCK, 0, 0, Integer.MAX_VALUE);
        setHeatMoveRate(Blocks.BARRIER, 0, 0, Integer.MAX_VALUE);
    }

    public static void setHeatMoveRate(Block block, int give, int cap, int loss)
    {
        blockGiveRate.put(block, () -> give);
        blockCapacity.put(block, () -> cap);
        blockLoss.put(block, () -> loss);
    }

    public static void setHeatMoveRate(Material material, int give, int cap, int loss)
    {
        materialGiveRate.put(material, () -> give);
        materialCapacity.put(material, () -> cap);
        materialLoss.put(material, () -> loss);
    }

    public static int getBlockLoss(IBlockState state)
    {
        final Block block = state.getBlock();
        if (blockLoss.containsKey(block))
        {
            return blockLoss.get(block).getAsInt();
        }

        final Material material = state.getMaterial();
        if (materialLoss.containsKey(material))
        {
            return materialLoss.get(material).getAsInt();
        }
        return 10;
    }

    public static int getBlockCapacity(IBlockState state)
    {
        final Block block = state.getBlock();
        if (blockCapacity.containsKey(block))
        {
            return blockCapacity.get(block).getAsInt();
        }

        final Material material = state.getMaterial();
        if (materialCapacity.containsKey(material))
        {
            return materialCapacity.get(material).getAsInt();
        }
        return 20;
    }

    public static int getHeatMovementWeight(IBlockState state)
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
}
