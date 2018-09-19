package com.builtbroken.atomic.content.machines.processing.extractor.inventory;

import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import com.builtbroken.atomic.lib.inventory.ItemStackHandlerWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2018.
 */
public class PipeInventoryExtractor extends ItemStackHandlerWrapper
{
    private TileEntityChemExtractor tileEntityChemExtractor;

    public PipeInventoryExtractor(TileEntityChemExtractor tileEntityChemExtractor)
    {
        super(tileEntityChemExtractor.getInventory());
        this.tileEntityChemExtractor = tileEntityChemExtractor;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (slot == TileEntityChemExtractor.SLOT_ITEM_OUTPUT)
        {
            return super.extractItem(slot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        if (slot == TileEntityChemExtractor.SLOT_ITEM_INPUT)
        {
            return super.insertItem(slot, stack, simulate);
        }
        return stack;
    }
}
