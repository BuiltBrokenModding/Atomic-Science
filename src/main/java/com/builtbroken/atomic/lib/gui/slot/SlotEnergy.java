package com.builtbroken.atomic.lib.gui.slot;

import com.builtbroken.atomic.lib.gui.ISlotRender;
import com.builtbroken.atomic.lib.gui.tip.ISlotToolTip;
import com.builtbroken.atomic.lib.gui.tip.ToolTip;
import com.builtbroken.atomic.lib.gui.tip.ToolTipSlot;
import com.builtbroken.atomic.lib.power.PowerSystem;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/30/2018.
 */
public class SlotEnergy extends SlotMachine implements ISlotRender, ISlotToolTip
{
    String toolTip;

    public SlotEnergy(IItemHandler inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);
    }

    public SlotEnergy(IItemHandler inventory, int index, int x, int y, String toolTip)
    {
        super(inventory, index, x, y);
        this.toolTip = toolTip;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        if (stack != null)
        {
            return PowerSystem.getHandler(stack) != null;
        }
        return super.isItemValid(stack);
    }

    @Override
    protected void drawIcon(Gui gui, int x, int y)
    {
        gui.drawTexturedModalRect(x, y, 0, 18, 18, 18);
    }

    @Override
    public ToolTip getToolTip()
    {
        if (toolTip != null)
        {
            return new ToolTipSlot(this, toolTip);
        }
        return null;
    }
}
