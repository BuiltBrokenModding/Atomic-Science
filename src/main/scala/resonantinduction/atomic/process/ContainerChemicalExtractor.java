package resonantinduction.atomic.process;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import resonant.lib.gui.ContainerBase;
import resonant.lib.prefab.slot.SlotEnergyItem;

/** Chemical extractor container */
public class ContainerChemicalExtractor extends ContainerBase
{
    private static final int slotCount = 5;
    private TileChemicalExtractor tileEntity;

    public ContainerChemicalExtractor(InventoryPlayer par1InventoryPlayer, TileChemicalExtractor tileEntity)
    {
        super(tileEntity);
        this.tileEntity = tileEntity;
        // Battery
        addSlotToContainer(new SlotEnergyItem(tileEntity, 0, 80, 50));
        // Process Input (Cell or Uranium)
        addSlotToContainer(new Slot(tileEntity, 1, 53, 25));
        // Process Output
        addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, tileEntity, 2, 107, 25));

        // Fluid input fill
        addSlotToContainer(new Slot(tileEntity, 3, 25, 19));
        // Fluid input drain
        addSlotToContainer(new Slot(tileEntity, 4, 25, 50));

        // Fluid output fill
        addSlotToContainer(new Slot(tileEntity, 5, 135, 19));
        // Fluid output drain
        addSlotToContainer(new Slot(tileEntity, 6, 135, 50));

        addPlayerInventory(par1InventoryPlayer.player);
    }
}
