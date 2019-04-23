package com.builtbroken.atomic.lib.gui.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/31/2018.
 */
public class SlotOutput extends SlotMachine
{
    public SlotOutput(IItemHandler inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }
}
