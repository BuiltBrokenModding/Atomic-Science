package com.builtbroken.atomic.content.machines.reactor.fission.core;

import net.minecraft.util.IStringSerializable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/13/2018.
 */
public enum ReactorStructureType implements IStringSerializable
{
    NORMAL,
    TOP,
    MIDDLE,
    BOTTOM,
    ROD;

    public static ReactorStructureType get(int meta)
    {
        if (meta >= 0 && meta < values().length)
        {
            return values()[meta];
        }
        return NORMAL;
    }

    @Override
    public String getName()
    {
        return name().toLowerCase();
    }
}
