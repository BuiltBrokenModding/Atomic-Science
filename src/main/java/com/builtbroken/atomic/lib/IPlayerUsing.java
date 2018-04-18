package com.builtbroken.atomic.lib;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashSet;

public interface IPlayerUsing
{
    public HashSet<EntityPlayer> getPlayersUsing();
}
