package com.builtbroken.atomic.lib.gui;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Collection;

/**
 * Used to track players currently using an object. Primaryly used
 * for GUI handling.
 * Created by robert on 1/12/2015.
 */
public interface IPlayerUsing
{
    /**
     * Collection of player's using the GUI
     * <p>
     * May include players previously using the GUI but have
     * not been cleared from the list yet.
     *
     * @return collection of players
     */
    Collection<EntityPlayer> getPlayersUsingGui();

    /**
     * Called when the GUI container has been opened
     *
     * @param player - player to add
     */
    default boolean addPlayerUsingGui(EntityPlayer player)
    {
        if (!getPlayersUsingGui().contains(player))
        {
            return getPlayersUsingGui().add(player);
        }
        return true; //Already added
    }

    /**
     * Called when the GUI container has been closed
     *
     * @param player - player to remove
     */
    default boolean removePlayerUsingGui(EntityPlayer player)
    {
        if (getPlayersUsingGui().contains(player))
        {
            return getPlayersUsingGui().remove(player);
        }
        return true; //Already removed
    }
}
