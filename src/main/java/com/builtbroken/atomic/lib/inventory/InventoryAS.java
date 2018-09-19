package com.builtbroken.atomic.lib.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/19/2018.
 */
public abstract class InventoryAS extends ItemStackHandler
{
    public InventoryAS()
    {
        this(1);
    }

    public InventoryAS(int size)
    {
        super(size);
    }

    public InventoryAS(NonNullList<ItemStack> stacks)
    {
        super(stacks);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        if (isItemValid(slot, stack))
        {
            return super.insertItem(slot, stack, simulate);
        }
        return stack;
    }
}
