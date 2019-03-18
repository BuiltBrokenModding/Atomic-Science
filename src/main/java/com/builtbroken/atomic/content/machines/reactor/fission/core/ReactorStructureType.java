package com.builtbroken.atomic.content.machines.reactor.fission.core;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/13/2018.
 */
public enum ReactorStructureType implements IStringSerializable
{
    //Casings
    CORE,
    CORE_TOP,
    CORE_MIDDLE,
    CORE_BOTTOM,

    //Rods
    ROD,
    ROD_TOP,
    ROD_MIDDLE,
    ROD_BOTTOM;

    public IBlockState rodState;

    public static ReactorStructureType get(int meta)
    {
        if (meta >= 0 && meta < values().length)
        {
            return values()[meta];
        }
        return CORE;
    }

    @Override
    public String getName()
    {
        return name().toLowerCase();
    }
}
