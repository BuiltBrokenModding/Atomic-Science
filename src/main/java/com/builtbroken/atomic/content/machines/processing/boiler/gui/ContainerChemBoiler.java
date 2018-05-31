package com.builtbroken.atomic.content.machines.processing.boiler.gui;

import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import com.builtbroken.atomic.lib.gui.ContainerBase;
import com.builtbroken.atomic.lib.gui.slot.MachineSlot;
import com.builtbroken.atomic.lib.gui.slot.SlotEnergy;
import com.builtbroken.atomic.lib.gui.slot.SlotFluid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.SlotFurnace;

import java.awt.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2018.
 */
public class ContainerChemBoiler extends ContainerBase<TileEntityChemBoiler>
{
    public ContainerChemBoiler(EntityPlayer player, TileEntityChemBoiler tile)
    {
        super(player, tile);
        addSlotToContainer(new SlotFluid(tile, TileEntityChemBoiler.SLOT_FLUID_INPUT, 25, 52, true).setColor(Color.blue));
        addSlotToContainer(new SlotFluid(tile, TileEntityChemBoiler.SLOT_WASTE_FLUID, 134, 57, false).setColor(Color.green));
        addSlotToContainer(new SlotFluid(tile, TileEntityChemBoiler.SLOT_HEX_FLUID, 134 + 18, 57, false).setColor(Color.yellow));

        addSlotToContainer(new SlotFurnace(player, tile, TileEntityChemBoiler.SLOT_ITEM_OUTPUT, 100, 30));

        int x = 50;
        addSlotToContainer(new MachineSlot(tile, TileEntityChemBoiler.SLOT_ITEM_INPUT, x, 30).setColor(Color.RED));
        addSlotToContainer(new SlotEnergy(tile, TileEntityChemBoiler.SLOT_BATTERY, x, 52));
        addPlayerInventory(player);
    }
}
