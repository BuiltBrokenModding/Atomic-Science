package com.builtbroken.atomic.lib.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2018.
 */
public class ItemStackHandlerWrapper implements IItemHandlerModifiable
{
    public final IItemHandlerModifiable inventory;

    public ItemStackHandlerWrapper(IItemHandlerModifiable inventory)
    {
        this.inventory = inventory;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack)
    {
        inventory.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots()
    {
        return inventory.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return inventory.getStackInSlot(slot);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        if (isItemValid(slot, stack))
        {
            return inventory.insertItem(slot, stack, simulate);
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        return inventory.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return inventory.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        return inventory.isItemValid(slot, stack);
    }
}
