package com.builtbroken.atomicscienceclassic.content.extractor;

import com.builtbroken.mc.prefab.gui.ContainerBase;
import com.builtbroken.mc.prefab.gui.slot.SlotEnergyItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

/** Chemical extractor container */
public class ContainerChemicalExtractor extends ContainerBase
{
    private static final int slotCount = 5;
    private TileChemicalExtractor tileEntity;

    public ContainerChemicalExtractor(EntityPlayer player, TileChemicalExtractor tileEntity)
    {
        super(tileEntity);
        this.tileEntity = tileEntity;
        // Battery
        addSlotToContainer(new SlotEnergyItem(tileEntity, 0, 80, 50));
        // Process Input (Cell or Uranium)
        addSlotToContainer(new Slot(tileEntity, 1, 53, 25));
        // Process Output
        addSlotToContainer(new SlotFurnace(player, tileEntity, 2, 107, 25));

        // Fluid input fill
        addSlotToContainer(new Slot(tileEntity, 3, 25, 19));
        // Fluid input drain
        addSlotToContainer(new Slot(tileEntity, 4, 25, 50));

        // Fluid output fill
        addSlotToContainer(new Slot(tileEntity, 5, 135, 19));
        // Fluid output drain
        addSlotToContainer(new Slot(tileEntity, 6, 135, 50));

        addPlayerInventory(player);
    }
}
