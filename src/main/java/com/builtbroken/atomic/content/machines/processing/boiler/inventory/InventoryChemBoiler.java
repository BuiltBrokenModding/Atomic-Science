package com.builtbroken.atomic.content.machines.processing.boiler.inventory;

import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import com.builtbroken.atomic.lib.inventory.InventoryAS;
import com.builtbroken.atomic.lib.power.PowerSystem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/19/2018.
 */
public class InventoryChemBoiler extends InventoryAS
{
    private TileEntityChemBoiler tile;

    public InventoryChemBoiler(TileEntityChemBoiler tileEntityChemBoiler)
    {
        super(TileEntityChemBoiler.INVENTORY_SIZE);
        this.tile = tileEntityChemBoiler;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        if (slot == TileEntityChemBoiler.SLOT_FLUID_INPUT || slot == TileEntityChemBoiler.SLOT_HEX_FLUID || slot == TileEntityChemBoiler.SLOT_WASTE_FLUID || slot == TileEntityChemBoiler.SLOT_BATTERY)
        {
            return 1;
        }
        return super.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        if (slot == TileEntityChemBoiler.SLOT_FLUID_INPUT)
        {
            return tile.isInputFluid(stack);
        }
        else if (slot == TileEntityChemBoiler.SLOT_HEX_FLUID)
        {
            return tile.isEmptyFluidContainer(stack);
        }
        else if (slot == TileEntityChemBoiler.SLOT_WASTE_FLUID)
        {
            return tile.isEmptyFluidContainer(stack);
        }
        else if (slot == TileEntityChemBoiler.SLOT_ITEM_INPUT)
        {
            return tile.getRecipeList().isComponent(tile, stack);
        }
        else if (slot == TileEntityChemBoiler.SLOT_BATTERY)
        {
            return PowerSystem.getHandler(stack) != null;
        }
        return false;
    }
}
