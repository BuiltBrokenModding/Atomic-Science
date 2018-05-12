package com.builtbroken.atomic.lib;

import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/11/2018.
 */
public class MassHandler
{
    /**
     * Gets the mass of the object in the world
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return mass in kilograms
     */
    public static int getMass(World world, int x, int y, int z)
    {
        //https://www.simetric.co.uk/si_materials.htm
        return 1000;
    }
}
