package com.builtbroken.atomic.content.machines.reactor.pipe.inv.gui;

import com.builtbroken.atomic.content.machines.reactor.pipe.inv.TileEntityRodPipeInv;
import com.builtbroken.atomic.lib.gui.GuiContainerBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
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
