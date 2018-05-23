package com.builtbroken.atomic.content.machines.processing.centrifuge;

import com.builtbroken.atomic.lib.gui.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/23/2018.
 */
public class ContainerChemCentrifuge extends ContainerBase<TileEntityChemCentrifuge>
{
    public ContainerChemCentrifuge(EntityPlayer player, TileEntityChemCentrifuge tile)
    {
        super(player, tile);
        addSlotToContainer(new Slot(tile, TileEntityChemCentrifuge.SLOT_FLUID_INPUT, 25, 52));
        addSlotToContainer(new Slot(tile, TileEntityChemCentrifuge.SLOT_FLUID_OUTPUT, 136, 52));

        addSlotToContainer(new SlotFurnace(player, tile, TileEntityChemCentrifuge.SLOT_ITEM_OUTPUT, 100, 30));

        int x = 50;
        addSlotToContainer(new Slot(tile, TileEntityChemCentrifuge.SLOT_BATTERY, x, 52));
        addPlayerInventory(player);
    }
}
