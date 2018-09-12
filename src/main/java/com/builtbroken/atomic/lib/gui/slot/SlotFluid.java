package com.builtbroken.atomic.lib.gui.slot;

import com.builtbroken.atomic.lib.gui.GuiContainerBase;
import com.builtbroken.atomic.lib.gui.ISlotRender;
import com.builtbroken.atomic.lib.gui.tip.ISlotToolTip;
import com.builtbroken.atomic.lib.gui.tip.ToolTip;
import com.builtbroken.atomic.lib.gui.tip.ToolTipSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/30/2018.
 */
public class SlotFluid extends SlotMachine implements ISlotRender, ISlotToolTip
{
    private final String toolTip;

    public SlotFluid(IItemHandler inventory, String toolTip, int index, int x, int y)
    {
        super(inventory, index, x, y);
        this.toolTip = toolTip;
    }

    @Override
    protected void drawIcon(Gui gui, int x, int y)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(GuiContainerBase.GUI_COMPONENTS);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        gui.drawTexturedModalRect(x, y, 0, 18 * 2, 18, 18);
    }

    @Override
    public ToolTip getToolTip()
    {
        return new ToolTipSlot(this, toolTip);
    }
}
