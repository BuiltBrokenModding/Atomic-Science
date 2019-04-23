package com.builtbroken.atomic.lib.gui;

import net.minecraft.client.gui.Gui;

/**
 * Applied to slots that render detail in the GUI
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 8/22/2017.
 */
public interface ISlotRender
{
    /**
     * Called to render over the default slot background.
     * <p>
     * Should be used to render slot backgrounds to help the
     * user ID what the slot is used for.
     *
     * @param x
     * @param y
     */
    void renderSlotOverlay(Gui gui, int x, int y);
}
