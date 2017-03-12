package resonantinduction.atomic.process.fission;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import resonant.lib.gui.ContainerBase;
import resonant.lib.prefab.slot.SlotEnergyItem;
import resonantinduction.atomic.Atomic;

/** Centrifuge container */
public class ContainerCentrifuge extends ContainerBase
{
    private static final int slotCount = 4;
    private TileCentrifuge tileEntity;

    public ContainerCentrifuge(InventoryPlayer par1InventoryPlayer, TileCentrifuge tileEntity)
    {
        super(tileEntity);
        this.tileEntity = tileEntity;
        // Electric Item
        this.addSlotToContainer(new SlotEnergyItem(tileEntity, 0, 131, 26));
        // Uranium Gas Tank
        this.addSlotToContainer(new Slot(tileEntity, 1, 25, 50));
        // Output Uranium 235
        this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, tileEntity, 2, 81, 26));
        // Output Uranium 238
        this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, tileEntity, 3, 101, 26));
        this.addPlayerInventory(par1InventoryPlayer.player);
        tileEntity.openChest();
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        this.tileEntity.getPlayersUsing().remove(entityplayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
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

            if (par1 >= slotCount)
            {
                if (this.getSlot(0).isItemValid(itemStack))
                {
                    if (!this.mergeItemStack(itemStack, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (Atomic.isItemStackUraniumOre(itemStack))
                {
                    if (!this.mergeItemStack(itemStack, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (Atomic.isItemStackEmptyCell(itemStack))
                {
                    if (!this.mergeItemStack(itemStack, 3, 4, false))
                    {
                        return null;
                    }
                }
                else if (par1 < 27 + slotCount)
                {
                    if (!this.mergeItemStack(itemStack, 27 + slotCount, 36 + slotCount, false))
                    {
                        return null;
                    }
                }
                else if (par1 >= 27 + slotCount && par1 < 36 + slotCount && !this.mergeItemStack(itemStack, 4, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemStack, slotCount, 36 + slotCount, false))
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
