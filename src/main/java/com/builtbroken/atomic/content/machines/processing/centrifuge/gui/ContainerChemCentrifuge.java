package com.builtbroken.atomic.content.machines.processing.centrifuge.gui;

import com.builtbroken.atomic.content.items.wrench.WrenchColor;
import com.builtbroken.atomic.content.machines.processing.centrifuge.TileEntityChemCentrifuge;
import com.builtbroken.atomic.lib.gui.ContainerBase;
import com.builtbroken.atomic.lib.gui.slot.SlotEnergy;
import com.builtbroken.atomic.lib.gui.slot.SlotFluid;
import com.builtbroken.atomic.lib.gui.slot.SlotOutput;
import com.builtbroken.atomic.lib.power.PowerSystem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/23/2018.
 */
public class ContainerChemCentrifuge extends ContainerBase<TileEntityChemCentrifuge>
{
    public ContainerChemCentrifuge(EntityPlayer player, TileEntityChemCentrifuge tile)
    {
        super(player, tile);
        addSlotToContainer(new SlotFluid(tile.getInventory(), "gui.tooltip.slot.tank.input.blue", TileEntityChemCentrifuge.SLOT_FLUID_INPUT, 25, 52).setColor(WrenchColor.BLUE.getColor()));

        addSlotToContainer(new SlotOutput(tile.getInventory(), TileEntityChemCentrifuge.SLOT_ITEM_OUTPUT, 100, 30)
                .setColor(WrenchColor.ORANGE.getColor()).setToolTip("gui.tooltip.slot.output"));

        int x = 50;
        addSlotToContainer(new SlotEnergy(tile.getInventory(), TileEntityChemCentrifuge.SLOT_BATTERY, x, 52, "gui.tooltip.slot.energy.input").setColor(WrenchColor.PURPLE.getColor()));

        addSlotToContainer(new SlotFluid(tile.getInventory(), "gui.tooltip.slot.tank.output.green", TileEntityChemCentrifuge.SLOT_FLUID_OUTPUT, 136, 52).setColor(WrenchColor.GREEN.getColor()));

        addPlayerInventory(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        final int invStart = 0;
        final int invEnd = TileEntityChemCentrifuge.INVENTORY_SIZE;

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
                if (host.isInputFluid(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityChemCentrifuge.SLOT_FLUID_INPUT, TileEntityChemCentrifuge.SLOT_FLUID_INPUT + 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (host.isEmptyFluidContainer(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityChemCentrifuge.SLOT_FLUID_OUTPUT, TileEntityChemCentrifuge.SLOT_FLUID_OUTPUT + 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(PowerSystem.getEnergyStored(itemstack1) > 0)
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityChemCentrifuge.SLOT_BATTERY, TileEntityChemCentrifuge.SLOT_BATTERY + 1, false))
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
