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

    public ConfigPower()
    {
        super("config.power");
    }

    @Override
    public void preInit()
    {
        Configuration configuration = new Configuration(new File(AtomicScience.configFolder, "Power.cfg"), AtomicScience.VERSION);
    }
}
