package com.builtbroken.visualization.component;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Forces a component to keep a size that is 1:1
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/12/2018.
 */
public class ResizeListenerBoxSize implements ComponentListener
{
    @Override
    public void componentResized(ComponentEvent e)
    {
        Dimension dimension = e.getComponent().getSize();
        if (dimension.height > dimension.width)
        {
            e.getComponent().setSize(new Dimension(dimension.width, dimension.width));
        }
        else
        {
            e.getComponent().setSize(new Dimension(dimension.height, dimension.height));
        }
    }

    @Override
    public void componentMoved(ComponentEvent e)
    {

    }

    @Override
    public void componentShown(ComponentEvent e)
    {

    }

    @Override
    public void componentHidden(ComponentEvent e)
    {

    }
}
