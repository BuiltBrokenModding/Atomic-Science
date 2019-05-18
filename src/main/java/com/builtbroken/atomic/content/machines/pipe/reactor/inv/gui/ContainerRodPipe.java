package com.builtbroken.atomic.content.machines.pipe.reactor.inv.gui;

import com.builtbroken.atomic.content.machines.pipe.reactor.inv.TileEntityRodPipeInv;
import com.builtbroken.atomic.lib.gui.ContainerBase;
import com.builtbroken.atomic.lib.gui.slot.SlotMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
public class ContainerRodPipe extends ContainerBase<TileEntityRodPipeInv>
{
    public ContainerRodPipe(EntityPlayer player, TileEntityRodPipeInv node)
    {
        super(player, node);
        addSlotToContainer(new SlotMachine(node.getInventory(), 0, 80, 40));
        addPlayerInventory(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        final int invStart = 0;
        final int invEnd = 1;

        final int playerStart = invEnd;
        final int playerHotbar = invEnd + 27;
        final int playerEnd = playerHotbar + 9;

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            //Move item from machine to player inventory
            if (index < invEnd)
            {
                if (!this.mergeItemStack(itemstack1, playerStart, playerEnd, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            //Move item from player inventory to machine
            else if (index >= playerStart)
            {
                if(host.getInventory().isItemValid(0, itemstack))
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= playerStart && index < playerHotbar)
                {
                    if (!this.mergeItemStack(itemstack1, playerHotbar, playerEnd, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= playerHotbar && index < playerEnd && !this.mergeItemStack(itemstack1, playerStart, playerHotbar, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, playerStart, playerEnd, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
