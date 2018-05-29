package com.builtbroken.atomic.content.items.wrench;

import java.awt.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/29/2018.
 */
public enum WrenchColor
{
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    RED(Color.RED),
    BLUE(Color.BLUE),
    PURPLE(Color.MAGENTA);

    final Color color;

    WrenchColor(Color color)
    {
        this.color = color;
    }

    public static WrenchColor get(int value)
    {
        if (value >= 0 && value < values().length)
        {
            return values()[value];
        }
        return RED;
    }

    public int getColorInt()
    {
        return color.getRGB();
    }

    public WrenchColor next()
    {
        int i = ordinal() + 1;
        if (i >= values().length)
        {
            i = 0;
        }
        return values()[i];
    }

    public WrenchColor prev()
    {
        int i = ordinal() - 1;
        if (i < 0)
        {
            i = values().length - 1;
        }
        return values()[i];
    }
}
