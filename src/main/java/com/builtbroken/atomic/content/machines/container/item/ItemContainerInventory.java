package com.builtbroken.atomic.content.machines.container.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/**
 * Created by Dark(DarkGuardsman, Robert) on 5/19/2019.
 */
class ItemContainerInventory extends ItemStackHandler
{
    private final TileEntityItemContainer tileEntityItemContainer;

    public ItemContainerInventory(TileEntityItemContainer tileEntityItemContainer)
    {
        super(1);
        this.tileEntityItemContainer = tileEntityItemContainer;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return 1;
    }

    @Override
    protected void onContentsChanged(int slot)
    {
        tileEntityItemContainer.syncClientNextTick();
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

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        return !tileEntityItemContainer.hasFluidStored();
    }
}
