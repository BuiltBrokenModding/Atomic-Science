package com.builtbroken.atomic.content.machines.processing.extractor.gui;

import com.builtbroken.atomic.content.items.wrench.WrenchColor;
import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import com.builtbroken.atomic.lib.gui.ContainerBase;
import com.builtbroken.atomic.lib.gui.slot.SlotMachine;
import com.builtbroken.atomic.lib.gui.slot.SlotEnergy;
import com.builtbroken.atomic.lib.gui.slot.SlotFluid;
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
        addSlotToContainer(new SlotFluid(tile.getInventory(), "gui.tooltip.slot.tank.input.blue", TileEntityChemExtractor.SLOT_FLUID_INPUT, 25, 52).setColor(WrenchColor.BLUE.getColor()));
        addSlotToContainer(new SlotFluid(tile.getInventory(), "gui.tooltip.slot.tank.output.green", TileEntityChemExtractor.SLOT_FLUID_OUTPUT, 136, 52).setColor(WrenchColor.GREEN.getColor()));

        addSlotToContainer(new SlotOutput(tile.getInventory(), TileEntityChemExtractor.SLOT_ITEM_OUTPUT, 100, 30)
                .setColor(WrenchColor.ORANGE.getColor()).setToolTip("gui.tooltip.slot.output"));

        int x = 50;
        addSlotToContainer(new SlotMachine(tile.getInventory(), TileEntityChemExtractor.SLOT_ITEM_INPUT, x, 30)
                .setColor(WrenchColor.RED.getColor()).setToolTip("gui.tooltip.slot.input"));
        addSlotToContainer(new SlotEnergy(tile.getInventory(), TileEntityChemExtractor.SLOT_BATTERY, x, 52, "gui.tooltip.slot.energy.input").setColor(WrenchColor.PURPLE.getColor()));
        addPlayerInventory(player);
    }
}
