package com.builtbroken.atomic.content.machines.processing.centrifuge.inventory;

import com.builtbroken.atomic.content.machines.processing.centrifuge.TileEntityChemCentrifuge;
import com.builtbroken.atomic.lib.inventory.InventoryAS;
import com.builtbroken.atomic.lib.power.PowerSystem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/19/2018.
 */
public class InventoryChemCentrifuge extends InventoryAS
{
    private final TileEntityChemCentrifuge tile;

    public InventoryChemCentrifuge(TileEntityChemCentrifuge chemCentrifuge)
    {
        super(TileEntityChemCentrifuge.INVENTORY_SIZE);
        this.tile = chemCentrifuge;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        if (slot == TileEntityChemCentrifuge.SLOT_FLUID_INPUT || slot == TileEntityChemCentrifuge.SLOT_FLUID_OUTPUT || slot == TileEntityChemCentrifuge.SLOT_BATTERY)
        {
            return 1;
        }
        return super.getSlotLimit(slot);
    }


    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {
        if (slot == TileEntityChemCentrifuge.SLOT_FLUID_INPUT)
        {
            return tile.isInputFluid(stack);
        }
        else if (slot == TileEntityChemCentrifuge.SLOT_FLUID_OUTPUT)
        {
            return tile.isEmptyFluidContainer(stack);
        }
        else if (slot == TileEntityChemCentrifuge.SLOT_BATTERY)
        {
            return PowerSystem.getHandler(stack) != null;
        }
        return false;
    }

}
