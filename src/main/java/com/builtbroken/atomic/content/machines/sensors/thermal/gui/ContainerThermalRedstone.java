package com.builtbroken.atomic.content.machines.sensors.thermal.gui;

import com.builtbroken.atomic.content.machines.sensors.thermal.TileEntityThermalRedstone;
import com.builtbroken.atomic.lib.gui.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
public class ContainerThermalRedstone extends ContainerBase<TileEntityThermalRedstone>
{
    public ContainerThermalRedstone(EntityPlayer player, TileEntityThermalRedstone node)
    {
        super(player, node);
        if (node.isServer())
        {
            node.sendGuiPacket(player);
        }
    }
}
