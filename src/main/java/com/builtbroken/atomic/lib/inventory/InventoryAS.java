package com.builtbroken.atomic.lib.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

/**
 *
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
}
