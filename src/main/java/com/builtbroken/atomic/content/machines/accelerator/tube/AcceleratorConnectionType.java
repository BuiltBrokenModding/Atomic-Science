package com.builtbroken.atomic.content.machines.accelerator.tube;

import net.minecraft.util.IStringSerializable;

/**
 * Handles connection between tubes.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/10/2018.
 */
public enum AcceleratorConnectionType implements IStringSerializable
{
    NORMAL,
    CORNER_CLOCKWISE,
    CORNER_COUNTER_CLOCKWISE,
    T_CLOCKWISE,
    T_COUNTER_CLOCKWISE,
    INTERSECTION;

    public static AcceleratorConnectionType byIndex(int i)
    {
        if(i > 0 && i < values().length)
        {
            return values()[i];
        }
        return NORMAL;
    }

    @Override
    public String getName()
    {
        return name().toLowerCase();
    }
}
