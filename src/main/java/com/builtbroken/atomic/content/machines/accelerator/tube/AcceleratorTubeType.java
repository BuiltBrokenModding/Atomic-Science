package com.builtbroken.atomic.content.machines.accelerator.tube;

import net.minecraft.util.IStringSerializable;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 11/10/2018.
 */
@Deprecated //TODO convert to a boolean state
public enum AcceleratorTubeType implements IStringSerializable
{
    NORMAL,
    POWERED;

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
