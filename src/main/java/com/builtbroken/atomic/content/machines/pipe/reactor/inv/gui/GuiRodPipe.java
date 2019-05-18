package com.builtbroken.atomic.content.machines.pipe.reactor.inv.gui;

import com.builtbroken.atomic.content.machines.pipe.reactor.inv.TileEntityRodPipeInv;
import com.builtbroken.atomic.lib.gui.GuiContainerBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
public class GuiRodPipe extends GuiContainerBase<TileEntityRodPipeInv>
{
    public GuiRodPipe(EntityPlayer player, TileEntityRodPipeInv host)
    {
        super(new ContainerRodPipe(player, host), host);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
        drawContainerSlots();
    }
}
