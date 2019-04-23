package com.builtbroken.atomic.lib.gui.tip;

import net.minecraft.inventory.Slot;

import java.awt.*;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/31/2018.
 */
public class ToolTipSlot extends ToolTip
{
    public final Slot tank;

    public ToolTipSlot(Slot tank, String string)
    {
        super(new Rectangle(tank.xPos, tank.yPos, 18, 18), string, true);
        this.tank = tank;
    }

    @Override
    public boolean shouldShow()
    {
        return !tank.getHasStack();
    }
}
