package com.builtbroken.atomic.lib.thermal;

import com.builtbroken.atomic.api.thermal.IHeatSource;
import com.builtbroken.atomic.config.ConfigPower;
import com.builtbroken.atomic.lib.MassHandler;
import com.builtbroken.atomic.lib.placement.PlacementQueue;
import com.builtbroken.atomic.map.MapHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
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
        setValue(Blocks.water, 0.58f, 4.187f, 2257, 373.15f);
        setValue(Blocks.ice, 2.18f, 2.108f, 334, 273.15f, Blocks.water, 0);
        setValue(Blocks.air, 0.024f, 0.718f, -1, -1);
        setValue(Blocks.iron_block, 55f, 0.444f, -1, 1811.15f);
        setValue(Blocks.gold_block, 315f, 0.129f, -1, 1337.15f);
        //Melting point of stone is 1473.15 Kelvin
        //http://www.physicsclassroom.com/class/thermalP/Lesson-1/Rates-of-Heat-Transfer
    }

    public static void setValue(Block block, float rate, float specificHeat, float changeHeat, float changeTemp)
    {
        setValue(block, rate, specificHeat, changeHeat, changeTemp, null, 0);
    }

    public static void setValue(Block block, float rate, float specificHeat, float changeHeat, float changeTemp, Block changeBlock, int changeMeta)
    {
        blockThermalDataMap.put(block, new ThermalData(rate, specificHeat, changeHeat, changeTemp, changeBlock, changeMeta));
    }

    public static ThermalData getThermalData(World world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);
        if (blockThermalDataMap.containsKey(block))
        {
            return blockThermalDataMap.get(block);
        }
        return null;
    }

    /**
     * Can the block change states due to the thermal system
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return true if it is possible to change states
     */
    public static boolean canChangeStates(World world, int x, int y, int z)
    {
        ThermalData data = getThermalData(world, x, y, z);
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
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return energy cost in joules to change states (e.g. ice -> water)
     */
    public static long energyCostToChangeStates(World world, int x, int y, int z)
    {
        ThermalData data = getThermalData(world, x, y, z);
        if (data != null)
        {
            float mass = MassHandler.getMass(world, x, y, z);
            return (long) (data.energyToChangeStates(mass) + data.energyToGetToStateChange(mass));
        }
        return 0;
    }


    /**
     * Gets the specific heat of the block a the location
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return specific heat J/kgK (joules / kilo-grams kelvin)
     */
    public static float getSpecificHeat(World world, int x, int y, int z)
    {
        ThermalData data = getThermalData(world, x, y, z);
        if (data != null)
        {
            return data.specificHeat;
        }
        return 0.2f;
    }

    public static double getHeatTransferRate(World world, int x, int y, int z)
    {
        ThermalData data = getThermalData(world, x, y, z);
        if (data != null)
        {
            return data.heatMovementRate;
        }
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IHeatSource)
        {
            return 1000;
        }
        return 0.1;
    }

    public static void changeStates(World world, int x, int y, int z)
    {
        ThermalData data = getThermalData(world, x, y, z);
        if (data != null && data.changeBlock != null)
        {
            float mass = MassHandler.getMass(world, x, y, z);
            double stateChangeEnergy = data.energyToChangeStates(mass);
            double energyToGetToChange = data.energyToGetToStateChange(mass);
            PlacementQueue.queue(new ThermalPlacement(world, x, y, z, data, (long) (stateChangeEnergy + energyToGetToChange)).delay(1 + (int) (Math.random() * 10)));
        }
    }

    /**
     * Get amount of vapor produced per tick in mb
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return vapor in mb
     */
    public static int getVaporRate(World world, int x, int y, int z)
    {
        return getVaporRate(world, x, y, z, MapHandler.THERMAL_MAP.getActualJoules(world, x, y, z));
    }

    /**
     * Get amount of vapor produced per tick in mb
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return vapor in mb
     */
    public static int getVaporRate(World world, int x, int y, int z, long heat)
    {
        Block block = world.getBlock(x, y, z);
        double temperature = MapHandler.THERMAL_MAP.getTemperature(world, x, y, z, heat);
        if (block == Blocks.water)
        {
            if (temperature > 373)
            {
                return (int) Math.min(ConfigPower.WATER_VAPOR_MAX_RATE, Math.ceil(ConfigPower.WATER_VAPOR_RATE * (temperature / 373)));
            }
        }
        else if (block == Blocks.flowing_water)
        {
            if (temperature > 373)
            {
                return (int) Math.min(ConfigPower.WATER_VAPOR_MAX_RATE, Math.ceil(ConfigPower.WATER_FLOWING_VAPOR_RATE * (temperature / 373)));
            }
        }
        return 0;
    }
}
