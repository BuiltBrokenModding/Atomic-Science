package com.builtbroken.atomic.content.machines.accelerator.tube;

import net.minecraft.util.IStringSerializable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/10/2018.
 */
public enum AcceleratorTubeType implements IStringSerializable
{
    NORMAL,
    JUNCTION,
    DIRECTION;

    public static AcceleratorTubeType byIndex(int meta)
    {
        if(meta > 0 && meta < values().length)
        {
            return values()[meta];
        }
        return NORMAL;
    }

    public AcceleratorTubeType next()
    {
        int index = ordinal() + 1;
        if(index >= values().length)
        {
            index = 0;
        }
        return byIndex(index);
    }

    @Override
    public String getName()
    {
        return name().toLowerCase();
    }
}
