package com.builtbroken.atomic.lib.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/31/2018.
 */
public class SlotOutput extends MachineSlot
{
    public SlotOutput(IInventory inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }
}
