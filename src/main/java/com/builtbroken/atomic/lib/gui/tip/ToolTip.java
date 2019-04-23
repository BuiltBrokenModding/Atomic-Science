package com.builtbroken.atomic.lib.gui.tip;

import com.builtbroken.atomic.lib.LanguageUtility;

import java.awt.*;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/30/2018.
 */
public class ToolTip
{
    public final Rectangle area;
    public final String string;
    public final boolean translate;

    public ToolTip(Rectangle area, String string, boolean translate)
    {
        this.area = area;
        this.string = string.trim();
        this.translate = translate;
    }

    public String getString()
    {
        if (translate)
        {
            return translate(string);
        }
        return string;
    }

    protected String translate(String key)
    {
        String translation = LanguageUtility.getLocal(key);
        if (translation != null && !translation.isEmpty())
        {
            return translation;
        }
        return key;
    }

    public boolean isInArea(int x, int y)
    {
        return area.contains(x, y);
    }

    public boolean shouldShow()
    {
        return true;
    }
}
