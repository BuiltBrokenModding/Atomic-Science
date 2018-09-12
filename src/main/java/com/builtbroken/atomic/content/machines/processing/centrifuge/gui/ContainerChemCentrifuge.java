package com.builtbroken.atomic.content.machines.processing.centrifuge.gui;

import com.builtbroken.atomic.content.items.wrench.WrenchColor;
import com.builtbroken.atomic.content.machines.processing.centrifuge.TileEntityChemCentrifuge;
import com.builtbroken.atomic.lib.gui.ContainerBase;
import com.builtbroken.atomic.lib.gui.slot.SlotEnergy;
import com.builtbroken.atomic.lib.gui.slot.SlotFluid;
import com.builtbroken.atomic.lib.gui.slot.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/23/2018.
 */
public class ContainerChemCentrifuge extends ContainerBase<TileEntityChemCentrifuge>
{
    public ContainerChemCentrifuge(EntityPlayer player, TileEntityChemCentrifuge tile)
    {
        super(player, tile);
        addSlotToContainer(new SlotFluid(tile.getInventory(), "gui.tooltip.slot.tank.input.blue", TileEntityChemCentrifuge.SLOT_FLUID_INPUT, 25, 52).setColor(WrenchColor.BLUE.getColor()));
        addSlotToContainer(new SlotFluid(tile.getInventory(), "gui.tooltip.slot.tank.output.green", TileEntityChemCentrifuge.SLOT_FLUID_OUTPUT, 136, 52).setColor(WrenchColor.GREEN.getColor()));

        addSlotToContainer(new SlotOutput(tile.getInventory(), TileEntityChemCentrifuge.SLOT_ITEM_OUTPUT, 100, 30)
                .setColor(WrenchColor.ORANGE.getColor()).setToolTip("gui.tooltip.slot.output"));

        int x = 50;
        addSlotToContainer(new SlotEnergy(tile.getInventory(), TileEntityChemCentrifuge.SLOT_BATTERY, x, 52, "gui.tooltip.slot.energy.input").setColor(WrenchColor.PURPLE.getColor()));
        addPlayerInventory(player);
    }
}
