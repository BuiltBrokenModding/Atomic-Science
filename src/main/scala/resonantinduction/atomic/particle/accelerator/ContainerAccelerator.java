package resonantinduction.atomic.particle.accelerator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import resonant.lib.gui.ContainerBase;
import resonantinduction.atomic.Atomic;

/** Accelerator container */
public class ContainerAccelerator extends ContainerBase
{
    private TileAccelerator tileEntity;

    public ContainerAccelerator(InventoryPlayer par1InventoryPlayer, TileAccelerator tileEntity)
    {
        super(tileEntity);
        this.tileEntity = tileEntity;
        // Inputs
        addSlotToContainer(new Slot(tileEntity, 0, 132, 26));
        addSlotToContainer(new Slot(tileEntity, 1, 132, 51));
        // Output
        addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, tileEntity, 2, 132, 75));
        addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, tileEntity, 3, 106, 75));
        addPlayerInventory(par1InventoryPlayer.player);
    }

    /** Called to transfer a stack from one inventory to the other eg. when shift clicking. */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = null;
        Slot var3 = (Slot) this.inventorySlots.get(par1);

        if (var3 != null && var3.getHasStack())
        {
            ItemStack itemStack = var3.getStack();
            var2 = itemStack.copy();

            if (par1 > 2)
            {
                if (itemStack.itemID == Atomic.itemCell.itemID)
                {
                    if (!this.mergeItemStack(itemStack, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (!this.mergeItemStack(itemStack, 0, 1, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemStack, 3, 36 + 3, false))
            {
                return null;
            }

            if (itemStack.stackSize == 0)
            {
                var3.putStack((ItemStack) null);
            }
            else
            {
                var3.onSlotChanged();
            }

            if (itemStack.stackSize == var2.stackSize)
            {
                return null;
            }

            var3.onPickupFromSlot(par1EntityPlayer, itemStack);
        }

        return var2;
    }
}
