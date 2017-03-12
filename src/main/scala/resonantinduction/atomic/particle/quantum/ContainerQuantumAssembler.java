package resonantinduction.atomic.particle.quantum;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import resonantinduction.atomic.Atomic;

/* Atomic assembler container */
public class ContainerQuantumAssembler extends Container
{
    private TileQuantumAssembler tileEntity;

    public ContainerQuantumAssembler(InventoryPlayer par1InventoryPlayer, TileQuantumAssembler tileEntity)
    {
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new Slot(tileEntity, 0, 80, 40));
        this.addSlotToContainer(new Slot(tileEntity, 1, 53, 56));
        this.addSlotToContainer(new Slot(tileEntity, 2, 107, 56));
        this.addSlotToContainer(new Slot(tileEntity, 3, 53, 88));
        this.addSlotToContainer(new Slot(tileEntity, 4, 107, 88));
        this.addSlotToContainer(new Slot(tileEntity, 5, 80, 103));
        this.addSlotToContainer(new Slot(tileEntity, 6, 80, 72));

        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 148 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 206));
        }

        this.tileEntity.getPlayersUsing().add(par1InventoryPlayer.player);
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

            if (par1 > 6)
            {
                if (itemStack.itemID == Atomic.itemDarkMatter.itemID)
                {
                    if (!this.mergeItemStack(itemStack, 0, 6, false))
                    {
                        return null;
                    }
                }
                else if (!this.getSlot(6).getHasStack())
                {
                    if (!this.mergeItemStack(itemStack, 6, 7, false))
                    {
                        return null;
                    }
                }
            }
            else if (!this.mergeItemStack(itemStack, 7, 36 + 7, false))
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
