package com.builtbroken.atomic.config;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.proxy.ContentProxy;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */
public class ConfigPower extends ContentProxy
{
    /** How much power to create per mb of steam flow */
    public static int STEAM_TO_UE_POWER = 4;

    /** mb of steam produced per tick per heat % */
    public static int WATER_VAPOR_RATE = 120;
    /** mb of steam produced per tick per heat % */
    public static int WATER_FLOWING_VAPOR_RATE = 40;
    /** Max amount of steam that can be produced per tick from a water source */
    public static int WATER_VAPOR_MAX_RATE = 1000;

    public static int FUEL_ROD_RUNTIME = 5 * AtomicScience.TICKS_HOUR;
    public static int BREEDER_ROD_RUNTIME = 2 * AtomicScience.TICKS_HOUR;

    public ConfigPower()
    {
        super("config.power");
    }

    @Override
    public void preInit()
    {
        Configuration configuration = new Configuration(new File(AtomicScience.configFolder, "Power.cfg"), AtomicScience.VERSION);
        configuration.load();
        STEAM_TO_UE_POWER = configuration.getInt("steam_to_power_ratio", "power", STEAM_TO_UE_POWER, 1, 10000,
                "Ratio of milli-buckets of steam (1000 to a bucket) to amount of power in watts to generate. " +
                        "Normally a single source of water can produce 200-400mb of steam every tick.");

        WATER_VAPOR_MAX_RATE = configuration.getInt("max_vapor_rate_water", "vapor", WATER_VAPOR_MAX_RATE, 100, 10000,
                "Max limit a single still wait source can produce in terms of vapor (steam) per tick.");

        WATER_VAPOR_RATE = configuration.getInt("vapor_rate_water", "vapor", WATER_VAPOR_MAX_RATE, 1, 10000,
                "Default amount of vapor a still water source can produce when heated. Scales with temperature but works as a lower limit.");

        WATER_FLOWING_VAPOR_RATE = configuration.getInt("vapor_rate_water_flowing", "vapor", WATER_FLOWING_VAPOR_RATE, 1, 10000,
                "Default amount of vapor a flowing water source can produce when heated. Scales with temperature but works as a lower limit.");

        FUEL_ROD_RUNTIME = configuration.getInt("fuel_rod", "reactor_runtime", FUEL_ROD_RUNTIME, 1, Integer.MAX_VALUE, "How long the fuel rod runs in ticks (20 ticks a second)");
        BREEDER_ROD_RUNTIME = configuration.getInt("breeder_rod", "reactor_runtime", BREEDER_ROD_RUNTIME, 1, Integer.MAX_VALUE, "How long the fuel rod runs in ticks (20 ticks a second)");
        configuration.save();
    }
}
