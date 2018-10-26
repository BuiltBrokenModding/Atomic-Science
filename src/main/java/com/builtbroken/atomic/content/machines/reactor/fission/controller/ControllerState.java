package com.builtbroken.atomic.content.machines.reactor.fission.controller;

import net.minecraft.util.IStringSerializable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/26/2018.
 */
public enum ControllerState implements IStringSerializable
{
    ON,
    OFF;

    public static ControllerState get(int meta)
    {
        if (meta >= 0 && meta < values().length)
        {
            return values()[meta];
        }
        return OFF;
    }


    @Override
    public String getName()
    {
        return name().toLowerCase();
    }
}
