package com.builtbroken.atomic.content.machines.processing.boiler.gui;

import com.builtbroken.atomic.content.items.wrench.WrenchColor;
import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import com.builtbroken.atomic.lib.gui.ContainerBase;
import com.builtbroken.atomic.lib.gui.slot.SlotEnergy;
import com.builtbroken.atomic.lib.gui.slot.SlotFluid;
import com.builtbroken.atomic.lib.gui.slot.SlotMachine;
import com.builtbroken.atomic.lib.gui.slot.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2018.
 */
public class ContainerChemBoiler extends ContainerBase<TileEntityChemBoiler>
{
    public ContainerChemBoiler(EntityPlayer player, TileEntityChemBoiler tile)
    {
        super(player, tile);
        addSlotToContainer(new SlotFluid(tile.getInventory(), "gui.tooltip.slot.tank.input.blue", TileEntityChemBoiler.SLOT_FLUID_INPUT, 25, 52)
                .setColor(WrenchColor.BLUE.getColor()));
        addSlotToContainer(new SlotFluid(tile.getInventory(), "gui.tooltip.slot.tank.output.green", TileEntityChemBoiler.SLOT_WASTE_FLUID, 134, 57)
                .setColor(WrenchColor.GREEN.getColor()));
        addSlotToContainer(new SlotFluid(tile.getInventory(),"gui.tooltip.slot.tank.output.yellow",  TileEntityChemBoiler.SLOT_HEX_FLUID, 134 + 18, 57)
                .setColor(WrenchColor.YELLOW.getColor()));

        addSlotToContainer(new SlotOutput(tile.getInventory(), TileEntityChemBoiler.SLOT_ITEM_OUTPUT, 100, 30)
                .setColor(WrenchColor.ORANGE.getColor()).setToolTip("gui.tooltip.slot.output"));

        int x = 50;
        addSlotToContainer(new SlotMachine(tile.getInventory(), TileEntityChemBoiler.SLOT_ITEM_INPUT, x, 30)
                .setColor(WrenchColor.RED.getColor()).setToolTip("gui.tooltip.slot.input"));
        addSlotToContainer(new SlotEnergy(tile.getInventory(), TileEntityChemBoiler.SLOT_BATTERY, x, 52, "gui.tooltip.slot.energy.input").setColor(WrenchColor.PURPLE.getColor()));
        addPlayerInventory(player);
    }
}
