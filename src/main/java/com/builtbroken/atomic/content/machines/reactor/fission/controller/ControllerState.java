package com.builtbroken.atomic.content.machines.reactor.fission.controller;

import net.minecraft.util.IStringSerializable;

/**
 *
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
