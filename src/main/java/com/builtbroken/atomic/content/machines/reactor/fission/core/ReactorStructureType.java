package com.builtbroken.atomic.content.machines.reactor.fission.core;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

/**
 *
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
