package com.builtbroken.atomic.config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */
public class ConfigPower
{
    /** How much power to create per mb of steam flow */
    public static int STEAM_TO_UE_POWER = 4;

    /** mb of steam produced per tick per heat % */
    public static int WATER_VAPOR_RATE = 120;
    /** mb of steam produced per tick per heat % */
    public static int WATER_FLOWING_VAPOR_RATE = 40;
    /** Max amount of steam that can be produced per tick from a water source */
    public static int WATER_VAPOR_MAX_RATE = 1000;
}
