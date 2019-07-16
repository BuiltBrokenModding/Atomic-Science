package com.builtbroken.atomic.lib.thermal;

import com.builtbroken.atomic.content.effects.effects.FloatSupplier;
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

    public static final BlockDataMap<HeatCalc> heatMoveRate = new BlockDataMap<HeatCalc>((block, heat) -> (heat * 5) / 100);
    public static final BlockDataMap<IntSupplier> heatCapacity = new BlockDataMap<IntSupplier>(() -> 100);
    public static final BlockDataMap<IntSupplier> heatLossFlat = new BlockDataMap<IntSupplier>(() -> 10);
    public static final BlockDataMap<HeatCalc> heatLossPercentage = new BlockDataMap<HeatCalc>((block, heat) -> 0);


    public static void init()
    {
        //Materials
        setHeatMoveRate(Material.IRON, 40, 400, 2);

        //Blocks
        setHeatMoveRate(Blocks.GOLD_BLOCK, 50, 400, 1);
        setHeatMoveRate(Blocks.WATER, 20, 1000, 0);
        setHeatMoveRate(Blocks.FLOWING_WATER, 20, 1000, 0);

        //Inpassible
        setHeatMoveRate(Blocks.BEDROCK, 0, 0, Integer.MAX_VALUE);
        setHeatMoveRate(Blocks.BARRIER, 0, 0, Integer.MAX_VALUE);
    }

    public static void setHeatMoveRate(Block block, int give, int cap, int loss)
    {
        heatMoveRate.add(block, (bs, heat) -> (give * heat) / 100);
        heatCapacity.add(block, () -> cap);
        heatLossFlat.add(block, () -> loss);
    }

    public static void setHeatMoveRate(Material material, int give, int cap, int loss)
    {
        heatMoveRate.add(material, (bs, heat) -> (give * heat) / 100);
        heatCapacity.add(material, () -> cap);
        heatLossFlat.add(material, () -> loss);
    }

    public static int getHeatLost(IBlockState state, int heat)
    {
        return heatLossFlat.get(state).getAsInt() + heatLossPercentage.get(state).calc(state, heat);
    }

    public static int getBlockCapacity(IBlockState state)
    {
        return heatCapacity.get(state).getAsInt();
    }

    public static float getTransferRate(IBlockState state)
    {
        return heatMoveRate.get(state).calc(state, -1);
    }

    public static int getHeatMoved(IBlockState state, int heat)
    {
        return heatMoveRate.get(state).calc(state, heat);
    }

    private static class BlockDataMap<A>
    {

        private final Map<Material, A> materials = new HashMap();
        private final Map<Block, A> blocks = new HashMap();
        private final A defaultValue;

        public BlockDataMap(A defaultValue)
        {
            this.defaultValue = defaultValue;
        }

        public A get(IBlockState state)
        {
            return blocks.getOrDefault(state.getBlock(),
                    materials.getOrDefault(state.getMaterial(),
                            defaultValue));
        }

        public void add(Block block, A value)
        {
            blocks.put(block, value);
        }

        public void add(Material material, A value)
        {
            materials.put(material, value);
        }
    }

    private static interface HeatCalc
    {
        int calc(IBlockState block, int heat);
    }
}
