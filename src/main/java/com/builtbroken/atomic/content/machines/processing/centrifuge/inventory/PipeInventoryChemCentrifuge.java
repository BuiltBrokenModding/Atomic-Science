package com.builtbroken.atomic.content.machines.processing.centrifuge.inventory;

import com.builtbroken.atomic.content.machines.processing.centrifuge.TileEntityChemCentrifuge;
import com.builtbroken.atomic.lib.inventory.ItemStackHandlerWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2018.
 */
public class PipeInventoryChemCentrifuge extends ItemStackHandlerWrapper
{
    private TileEntityChemCentrifuge tile;

    public PipeInventoryChemCentrifuge(TileEntityChemCentrifuge tile)
    {
        super(tile.getInventory());
        this.tile = tile;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (slot == TileEntityChemCentrifuge.SLOT_ITEM_OUTPUT || !isItemValid(slot, getStackInSlot(slot)))
        {
            return super.extractItem(slot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }
}
