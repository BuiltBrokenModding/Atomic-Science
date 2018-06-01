package com.builtbroken.atomic.content.machines.processing.extractor.gui;

import com.builtbroken.atomic.content.items.wrench.WrenchColor;
import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import com.builtbroken.atomic.lib.gui.ContainerBase;
import com.builtbroken.atomic.lib.gui.slot.MachineSlot;
import com.builtbroken.atomic.lib.gui.slot.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public class ContainerExtractor extends ContainerBase<TileEntityChemExtractor>
{
    public ContainerExtractor(EntityPlayer player, TileEntityChemExtractor tile)
    {
        super(player, tile);
        addSlotToContainer(new MachineSlot(tile, TileEntityChemExtractor.SLOT_FLUID_INPUT, 25, 52).setColor(WrenchColor.BLUE.getColor()));
        addSlotToContainer(new MachineSlot(tile, TileEntityChemExtractor.SLOT_FLUID_OUTPUT, 136, 52).setColor(WrenchColor.GREEN.getColor()));

        addSlotToContainer(new SlotOutput(tile, TileEntityChemExtractor.SLOT_ITEM_OUTPUT, 100, 30).setColor(WrenchColor.ORANGE.getColor()));

        int x = 50;
        addSlotToContainer(new MachineSlot(tile, TileEntityChemExtractor.SLOT_ITEM_INPUT, x, 30).setColor(WrenchColor.RED.getColor()));
        addSlotToContainer(new MachineSlot(tile, TileEntityChemExtractor.SLOT_BATTERY, x, 52).setColor(WrenchColor.PURPLE.getColor()));
        addPlayerInventory(player);
    }
}
