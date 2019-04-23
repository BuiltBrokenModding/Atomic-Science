package com.builtbroken.atomic.lib;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/29/2018.
 */
public class SideSettings
{
    private boolean[] sides = new boolean[6]; //TODO bitshift

    public SideSettings(boolean init)
    {
        for (int i = 0; i < sides.length; i++)
        {
            sides[i] = init;
        }
    }

    public void set(EnumFacing side, boolean b)
    {
        set(side.ordinal(), b);
    }

    public void set(int side, boolean b)
    {
        sides[side] = b;
    }

    public boolean get(EnumFacing side)
    {
        return get(side.ordinal());
    }

    public boolean get(int side)
    {
        return sides[side];
    }

    public void toggle(int side)
    {
        set(side, !get(side));
    }

    public void toggle(EnumFacing side)
    {
        toggle(side.ordinal());
    }

    public NBTTagCompound save(NBTTagCompound nbt)
    {
        for (int i = 0; i < sides.length; i++)
        {
            nbt.setBoolean("" + i, sides[i]);
        }
        return nbt;
    }

    public SideSettings load(NBTTagCompound nbt)
    {
        for (int i = 0; i < sides.length; i++)
        {
            sides[i] = nbt.getBoolean("" + i);
        }
        return this;
    }
}
