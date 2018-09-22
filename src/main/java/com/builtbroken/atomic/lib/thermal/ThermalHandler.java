package com.builtbroken.atomic.lib.thermal;

import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.config.logic.ConfigLogic;
import com.builtbroken.atomic.lib.MassHandler;
import com.builtbroken.atomic.lib.placement.PlacementQueue;
import com.builtbroken.atomic.map.MapHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/11/2018.
 */
public class ThermalHandler
{
    //Thermal data about blocks
    private static final HashMap<Block, ThermalData> blockThermalDataMap = new HashMap();

    public static void init()
    {
        setValue(Blocks.WATER, 0.58f, 4.187f, 2257, 373.15f);
        setValue(Blocks.ICE, 2.18f, 2.108f, 334, 273.15f, Blocks.WATER.getDefaultState());
        setValue(Blocks.AIR, 0.024f, 0.718f, -1, -1);
        setValue(Blocks.IRON_BLOCK, 55f, 0.444f, -1, 1811.15f);
        setValue(Blocks.GOLD_BLOCK, 315f, 0.129f, -1, 1337.15f);
        //Melting point of stone is 1473.15 Kelvin
        //http://www.physicsclassroom.com/class/thermalP/Lesson-1/Rates-of-Heat-Transfer
    }

    public static void setValue(Block block, float rate, float specificHeat, float changeHeat, float changeTemp)
    {
        setValue(block, rate, specificHeat, changeHeat, changeTemp, null);
    }

    public static void setValue(Block block, float rate, float specificHeat, float changeHeat, float changeTemp, IBlockState changeBlock)
    {
        blockThermalDataMap.put(block, new ThermalData(rate, specificHeat, changeHeat, changeTemp, changeBlock));
    }

    public static ThermalData getThermalData(World world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();
        if (blockThermalDataMap.containsKey(block))
        {
            return blockThermalDataMap.get(block);
        }
        //TODO add IBlockState support
        return null;
    }

    /**
     * Can the block change states due to the thermal system
     *
     * @param world - location
     * @param pos - location
     * @return true if it is possible to change states
     */
    public static boolean canChangeStates(World world, BlockPos pos)
    {
        ThermalData data = getThermalData(world, pos);
        if (data != null)
        {
            return data.changeBlock != null;
        }
        return false;
    }

    /**
     * Can the block change states due to the thermal system
     *
     * @param world - location
     * @param pos - location
     * @return energy cost in joules to change states (e.g. ice -> water)
     */
    public static long energyCostToChangeStates(World world, BlockPos pos)
    {
        ThermalData data = getThermalData(world, pos);
        if (data != null)
        {
            float mass = MassHandler.getMass(world, pos);
            return (long) (data.energyToChangeStates(mass) + data.energyToGetToStateChange(mass));
        }
        return 0;
    }


    /**
     * Gets the specific heat of the block a the location
     *
     * @param world - location
     * @param pos - location
     * @return specific heat J/kgK (joules / kilo-grams kelvin)
     */
    public static float getSpecificHeat(World world, BlockPos pos)
    {
        ThermalData data = getThermalData(world, pos);
        if (data != null)
        {
            return data.specificHeat;
        }
        return 0.2f;
    }

    public static double getHeatTransferRate(World world, BlockPos pos)
    {
        ThermalData data = getThermalData(world, pos);
        if (data != null)
        {
            return data.heatMovementRate;
        }
        TileEntity tile = world.getTileEntity(pos);
        if (tile.hasCapability(AtomicScienceAPI.THERAMAL_CAPABILITY, null))
        {
            return 1000;
        }
        return 0.1;
    }

    public static void changeStates(World world, BlockPos pos)
    {
        ThermalData data = getThermalData(world, pos);
        if (data != null && data.changeBlock != null)
        {
            float mass = MassHandler.getMass(world, pos);
            double stateChangeEnergy = data.energyToChangeStates(mass);
            double energyToGetToChange = data.energyToGetToStateChange(mass);
            PlacementQueue.queue(new ThermalPlacement(world, pos, data, (long) (stateChangeEnergy + energyToGetToChange)).delay(1 + (int) (Math.random() * 10)));
        }
    }

    /**
     * Get amount of vapor produced per tick in mb
     *
     * @param world - location
     * @param pos - location
     * @return vapor in mb
     */
    public static int getVaporRate(World world, BlockPos pos)
    {
        return getVaporRate(world, pos, MapHandler.THERMAL_MAP.getActualJoules(world, pos));
    }

    /**
     * Get amount of vapor produced per tick in mb
     *
     * @param world - location
     * @param pos - location
     * @return vapor in mb
     */
    public static int getVaporRate(World world, BlockPos pos, long heat)
    {
        Block block = world.getBlockState(pos).getBlock();
        double temperature = MapHandler.THERMAL_MAP.getTemperature(world, pos, heat);
        if (block == Blocks.WATER)
        {
            if (temperature > 373)
            {
                return (int) Math.min(ConfigLogic.STEAM.WATER_VAPOR_MAX_RATE, Math.ceil(ConfigLogic.STEAM.WATER_VAPOR_RATE * (temperature / 373)));
            }
        }
        else if (block == Blocks.FLOWING_WATER)
        {
            if (temperature > 373)
            {
                return (int) Math.min(ConfigLogic.STEAM.WATER_VAPOR_MAX_RATE, Math.ceil(ConfigLogic.STEAM.WATER_FLOWING_VAPOR_RATE * (temperature / 373)));
            }
        }
        return 0;
    }
}
