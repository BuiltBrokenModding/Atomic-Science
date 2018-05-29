package com.builtbroken.atomic.content.items.wrench;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/29/2018.
 */
public enum WrenchMode
{
    ROTATION,
    ITEM,
    FLUID,
    REDSTONE;

    public static WrenchMode get(int value)
    {
        if (value >= 0 && value < values().length)
        {
            return values()[value];
        }
        return ITEM;
    }

    public WrenchMode next()
    {
        int i = ordinal() + 1;
        if (i >= values().length)
        {
            i = 0;
        }
        return values()[i];
    }

    public WrenchMode prev()
    {
        int i = ordinal() - 1;
        if (i < 0)
        {
            i = values().length - 1;
        }
        return values()[i];
    }
}
