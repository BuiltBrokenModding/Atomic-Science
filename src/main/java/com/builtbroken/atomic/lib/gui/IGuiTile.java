package com.builtbroken.atomic.lib.gui;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Collection;

/**
 * Simple internal interface to note that a TileEntity has a GUI
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 1/8/2015
 * <p>
 * copied from VoltzEngine and merged with IPlayerUsing also from VoltzEngine
 */
public interface IGuiTile extends IPlayerUsing
{
    /**
     * Returns a Server side Container to be displayed to the user.
     *
     * @param ID     The Gui ID Number
     * @param player The player viewing the Gui
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */
    Object getServerGuiElement(int ID, EntityPlayer player);

    /**
     * Returns a Container to be displayed to the user. On the client side, this
     * needs to return a instance of GuiScreen On the server side, this needs to
     * return a instance of Container
     *
     * @param ID     The Gui ID Number
     * @param player The player viewing the Gui
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */
    Object getClientGuiElement(int ID, EntityPlayer player);
}
