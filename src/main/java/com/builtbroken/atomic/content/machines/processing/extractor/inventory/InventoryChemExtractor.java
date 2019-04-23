package com.builtbroken.atomic.content.machines.processing.extractor.inventory;

import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import com.builtbroken.atomic.lib.inventory.InventoryAS;
import com.builtbroken.atomic.lib.power.PowerSystem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/19/2018.
 */
public class InventoryChemExtractor extends InventoryAS
{
    private TileEntityChemExtractor tileEntityChemExtractor;

    public InventoryChemExtractor(TileEntityChemExtractor tileEntityChemExtractor)
    {
        super(TileEntityChemExtractor.INVENTORY_SIZE);
        this.tileEntityChemExtractor = tileEntityChemExtractor;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        if (slot == TileEntityChemExtractor.SLOT_FLUID_INPUT || slot == TileEntityChemExtractor.SLOT_FLUID_OUTPUT || slot == TileEntityChemExtractor.SLOT_BATTERY)
        {
            return 1;
        }
        return super.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        if (slot == TileEntityChemExtractor.SLOT_FLUID_INPUT)
        {
            return tileEntityChemExtractor.isInputFluid(stack);
        }
        else if (slot == TileEntityChemExtractor.SLOT_FLUID_OUTPUT)
        {
            return tileEntityChemExtractor.isEmptyFluidContainer(stack);
        }
        else if (slot == TileEntityChemExtractor.SLOT_ITEM_INPUT)
        {
            return tileEntityChemExtractor.getRecipeList().isComponent(tileEntityChemExtractor, stack);
        }
        else if (slot == TileEntityChemExtractor.SLOT_BATTERY)
        {
            return PowerSystem.getHandler(stack) != null;
        }
        return false;
    }
}
