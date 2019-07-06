package com.builtbroken.atomic.lib.thermal;

import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.config.logic.ConfigLogic;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.lib.MassHandler;
import com.builtbroken.atomic.lib.placement.PlacementQueue;
import com.builtbroken.atomic.map.MapHandler;
import it.unimi.dsi.fastutil.objects.Object2FloatFunctions;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 5/11/2018.
 */
public class ThermalHandler
{
    private static final Map<Block, HeatSpreadFunction> heatSpreadRate = new HashMap();

    public static void init()
    {
        setHeatMoveRate(Blocks.IRON_BLOCK, 10);
    }

    public static void setHeatMoveRate(Block block, float rate)
    {
        heatSpreadRate.put(block, (self, target) -> rate);
    }

    public static float getHeatMoveRate(IBlockState self, IBlockState target)
    {
        if(heatSpreadRate.containsKey(self.getBlock()))
        {
            return heatSpreadRate.get(self.getBlock()).getSpreadRate(self, target);
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
