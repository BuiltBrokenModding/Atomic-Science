package com.builtbroken.atomic.lib.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Prefab for GUI containers to use
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert)
 */
public class ContainerBase<H extends Object> extends Container
{
    protected int slotCount = 0;

    /** Inventory for this container, can be null */
    public final IInventory inventory;
    /** Player accessing this GUI, can be null in rare cases */
    public final EntityPlayer player;
    /** Object hosting the container, can be null in rare cases */
    public final H host;

    public ContainerBase(EntityPlayer player, H node)
    {
        //Assign host
        host = node;

        //handle inventory
        if (node instanceof IInventory)
        {
            inventory = (IInventory) node;
        }
        else
        {
            throw new RuntimeException("ContainerBase >> GUI host  '" + node + "' failed to provide an inventory for the container to use");
        }

        //Handle player
        this.player = player;
        if (node instanceof IPlayerUsing)
        {
            ((IPlayerUsing) node).addPlayerUsingGui(player);
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        if (host instanceof IPlayerUsing && entityplayer.openContainer != this)
        {
            ((IPlayerUsing) host).removePlayerUsingGui(entityplayer);
        }
        super.onContainerClosed(entityplayer);
    }

    public void addPlayerInventory(EntityPlayer player)
    {
        addPlayerInventory(player, 8, 84);
    }

    public void addPlayerInventory(EntityPlayer player, int x, int y)
    {
        if (this.inventory instanceof IPlayerUsing)
        {
            ((IPlayerUsing) this.inventory).addPlayerUsingGui(player);
        }

        //Inventory
        for (int row = 0; row < 3; ++row)
        {
            for (int slot = 0; slot < 9; ++slot)
            {
                this.addSlotToContainer(new Slot(player.inventory, slot + row * 9 + 9, slot * 18 + x, row * 18 + y));
            }
        }

        //Hot bar
        for (int slot = 0; slot < 9; ++slot)
        {
            this.addSlotToContainer(new Slot(player.inventory, slot, slot * 18 + x, 58 + y));
        }
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot_id)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slot_id);

        if (slot != null && slot.getHasStack())
        {
            ItemStack slotStack = slot.getStack();
            itemstack = slotStack.copy();

            if (slot_id < this.slotCount)
            {
                /**
                 * The item is inside the block inventory, trying to move an item out.
                 */
                if (!mergeItemStack(slotStack, this.slotCount, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else
            {
                /**
                 * We are outside the block inventory, trying to move an item in.
                 *
                 * Only merge the inventory if it is valid for the specific slot!
                 */
                boolean foundValid = false;

                for (int i = 0; i < slotCount; i++)
                {
                    if (inventory.isItemValidForSlot(i, slotStack))
                    {
                        if (!mergeItemStack(slotStack, i, i + 1, false))
                        {
                            return null;
                        }

                        foundValid = true;
                    }
                }

                if (!foundValid)
                {
                    return null;
                }
            }

            if (slotStack.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return this.inventory.isUseableByPlayer(entityplayer);
    }
}