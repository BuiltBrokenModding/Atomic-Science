package com.builtbroken.atomic.content.machines.processing.extractor.gui;

import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import com.builtbroken.atomic.lib.gui.ContainerBase;
import com.builtbroken.atomic.lib.gui.slot.MachineSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.SlotFurnace;

import java.awt.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public class ContainerExtractor extends ContainerBase<TileEntityChemExtractor>
{
    public ContainerExtractor(EntityPlayer player, TileEntityChemExtractor tile)
    {
        super(player, tile);
        addSlotToContainer(new MachineSlot(tile, TileEntityChemExtractor.SLOT_FLUID_INPUT, 25, 52).setColor(Color.blue));
        addSlotToContainer(new MachineSlot(tile, TileEntityChemExtractor.SLOT_FLUID_OUTPUT, 136, 52).setColor(Color.green));

        addSlotToContainer(new SlotFurnace(player, tile, TileEntityChemExtractor.SLOT_ITEM_OUTPUT, 100, 30));

        int x = 50;
        addSlotToContainer(new MachineSlot(tile, TileEntityChemExtractor.SLOT_ITEM_INPUT, x, 30).setColor(Color.red));
        addSlotToContainer(new MachineSlot(tile, TileEntityChemExtractor.SLOT_BATTERY, x, 52));
        addPlayerInventory(player);
    }
}
