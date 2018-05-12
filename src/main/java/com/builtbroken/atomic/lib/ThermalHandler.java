package com.builtbroken.atomic.lib;

import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/11/2018.
 */
public class ThermalHandler
{
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
        return 1;
    }
}
