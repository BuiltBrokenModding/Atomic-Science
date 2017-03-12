package resonantinduction.atomic.fusion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import resonant.lib.gui.ContainerBase;
import resonant.lib.prefab.slot.SlotEnergyItem;
import resonant.lib.prefab.slot.SlotSpecific;
import resonantinduction.atomic.Atomic;
import resonantinduction.atomic.process.fission.TileNuclearBoiler;

/** Nuclear boiler container */
public class ContainerNuclearBoiler extends ContainerBase
{
    private static final int slotCount = 4;
    private TileNuclearBoiler tileEntity;

    public ContainerNuclearBoiler(InventoryPlayer par1InventoryPlayer, TileNuclearBoiler tileEntity)
    {
        super(tileEntity);
        this.tileEntity = tileEntity;
        // Battery
        this.addSlotToContainer(new SlotEnergyItem(tileEntity, 0, 56, 26));
        // Water Input
        this.addSlotToContainer(new Slot(tileEntity, 1, 25, 50));
        // Gas Output
        this.addSlotToContainer(new Slot(tileEntity, 2, 136, 50));
        // Yellowcake Input
        this.addSlotToContainer(new SlotSpecific(tileEntity, 3, 81, 26, new ItemStack(Atomic.itemYellowCake), new ItemStack(Atomic.blockUraniumOre)));
        this.addPlayerInventory(par1InventoryPlayer.player);
        tileEntity.openChest();
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
    }

    /** Called to transfer a stack from one inventory to the other eg. when shift clicking. */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotID)
    {
        ItemStack var2 = null;
        Slot slot = (Slot) this.inventorySlots.get(slotID);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemStack = slot.getStack();
            var2 = itemStack.copy();

            if (slotID >= slotCount)
            {
                if (this.getSlot(0).isItemValid(itemStack))
                {
                    if (!this.mergeItemStack(itemStack, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (Atomic.FLUIDSTACK_WATER.isFluidEqual(FluidContainerRegistry.getFluidForFilledItem(itemStack)))
                {
                    if (!this.mergeItemStack(itemStack, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (this.getSlot(3).isItemValid(itemStack))
                {
                    if (!this.mergeItemStack(itemStack, 3, 4, false))
                    {
                        return null;
                    }
                }
                else if (slotID < 27 + slotCount)
                {
                    if (!this.mergeItemStack(itemStack, 27 + slotCount, 36 + slotCount, false))
                    {
                        return null;
                    }
                }
                else if (slotID >= 27 + slotCount && slotID < 36 + slotCount && !this.mergeItemStack(itemStack, 4, 30, false))
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
                slot.putStack((ItemStack) null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemStack.stackSize == var2.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemStack);
        }

        return var2;
    }

}
