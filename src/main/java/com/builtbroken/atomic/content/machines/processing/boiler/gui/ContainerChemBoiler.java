package com.builtbroken.atomic.content.machines.processing.boiler.gui;

import com.builtbroken.atomic.content.items.wrench.WrenchColor;
import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import com.builtbroken.atomic.lib.gui.ContainerBase;
import com.builtbroken.atomic.lib.gui.slot.SlotEnergy;
import com.builtbroken.atomic.lib.gui.slot.SlotFluid;
import com.builtbroken.atomic.lib.gui.slot.SlotMachine;
import com.builtbroken.atomic.lib.gui.slot.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2018.
 */
public class ContainerChemBoiler extends ContainerBase<TileEntityChemBoiler>
{
    public ContainerChemBoiler(EntityPlayer player, TileEntityChemBoiler tile)
    {
        super(player, tile); //order of slots matter, do not move
        int x = 50;

        addSlotToContainer(new SlotFluid(tile.getInventory(), "gui.tooltip.slot.tank.input.blue", TileEntityChemBoiler.SLOT_FLUID_INPUT, 25, 52)
                .setColor(WrenchColor.BLUE.getColor()));

        addSlotToContainer(new SlotMachine(tile.getInventory(), TileEntityChemBoiler.SLOT_ITEM_INPUT, x, 30)
                .setColor(WrenchColor.RED.getColor()).setToolTip("gui.tooltip.slot.input"));

        addSlotToContainer(new SlotOutput(tile.getInventory(), TileEntityChemBoiler.SLOT_ITEM_OUTPUT, 100, 30)
                .setColor(WrenchColor.ORANGE.getColor()).setToolTip("gui.tooltip.slot.output"));

        addSlotToContainer(new SlotEnergy(tile.getInventory(), TileEntityChemBoiler.SLOT_BATTERY, x, 52, "gui.tooltip.slot.energy.input").setColor(WrenchColor.PURPLE.getColor()));

        addSlotToContainer(new SlotFluid(tile.getInventory(), "gui.tooltip.slot.tank.output.green", TileEntityChemBoiler.SLOT_WASTE_FLUID, 134, 57)
                .setColor(WrenchColor.GREEN.getColor()));
        addSlotToContainer(new SlotFluid(tile.getInventory(), "gui.tooltip.slot.tank.output.yellow", TileEntityChemBoiler.SLOT_HEX_FLUID, 134 + 18, 57)
                .setColor(WrenchColor.YELLOW.getColor()));

        addPlayerInventory(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        final int invStart = 0;
        final int invEnd = TileEntityChemBoiler.INVENTORY_SIZE;

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
                if (host.getRecipeList().isComponent(host, itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityChemBoiler.SLOT_ITEM_INPUT, TileEntityChemBoiler.SLOT_ITEM_INPUT + 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (host.isInputFluid(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityChemBoiler.SLOT_FLUID_INPUT, TileEntityChemBoiler.SLOT_FLUID_INPUT + 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (host.isEmptyFluidContainer(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityChemBoiler.SLOT_HEX_FLUID, TileEntityChemBoiler.SLOT_HEX_FLUID + 1, false))
                    {
                        if (!this.mergeItemStack(itemstack1, TileEntityChemBoiler.SLOT_WASTE_FLUID, TileEntityChemBoiler.SLOT_WASTE_FLUID + 1, false))
                        {
                            return ItemStack.EMPTY;
                        }
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
