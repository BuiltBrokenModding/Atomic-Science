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
    //Turn
    CORNER_RIGHT,
    //Turn
    CORNER_LEFT,
    //Join two inputs to a single output, with only 1 being a turn
    T_RIGHT,
    //Join two inputs to a single output, with only 1 being a turn
    T_LEFT,
    //Join two inputs to a single output, with two turns
    T_JOIN,
    //Join 3 inputs to a single output
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
