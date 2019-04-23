package com.builtbroken.atomic.content.machines;

import com.builtbroken.atomic.content.prefab.TileEntityMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Prefab used for any machine with an inventory
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public abstract class TileEntityInventoryMachine<I extends IItemHandlerModifiable> extends TileEntityMachine
{

    public static final String NBT_INVENTORY = "inventory";

    private IItemHandlerModifiable inventory;
    private I inventoryWrapper;

    public IItemHandlerModifiable getInventory()
    {
        if (inventory == null)
        {
            inventory = createInternalInventory();
        }
        return inventory;
    }

    protected IItemHandlerModifiable createInternalInventory()
    {
        return new ItemStackHandler(inventorySize());
    }

    /**
     * Creates an inventory
     *
     * @return
     */
    @Nonnull
    protected abstract I createInventory();

    protected abstract int inventorySize();

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return canInventoryConnect(facing);
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (inventoryWrapper == null)
            {
                inventoryWrapper = createInventory();
            }
            return (T) inventoryWrapper;
        }
        return super.getCapability(capability, facing);
    }

    protected boolean canInventoryConnect(EnumFacing side)
    {
        return true;
    }

    @Deprecated //Doesn't get called
    protected void onSlotStackChanged(ItemStack prev, ItemStack stack, int slot)
    {
        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);


        //Clear inventory
        for (int slotIndex = 0; slotIndex < getInventory().getSlots(); ++slotIndex)
        {
            getInventory().setStackInSlot(slotIndex, ItemStack.EMPTY);
        }

        NBTTagList nbttaglist = nbt.getTagList(NBT_INVENTORY, 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound save = nbttaglist.getCompoundTagAt(i);
            int slotIndex = save.getByte("Slot") & 255;

            if (slotIndex >= 0 && slotIndex < getInventory().getSlots())
            {
                getInventory().setStackInSlot(slotIndex, new ItemStack(save));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        NBTTagList nbttaglist = new NBTTagList();
        for (int slotIndex = 0; slotIndex < getInventory().getSlots(); ++slotIndex)
        {
            ItemStack stack = getInventory().getStackInSlot(slotIndex);
            if (!stack.isEmpty())
            {
                NBTTagCompound save = new NBTTagCompound();
                save.setByte("Slot", (byte) slotIndex);
                stack.writeToNBT(save);
                nbttaglist.appendTag(save);
            }
        }
        nbt.setTag(NBT_INVENTORY, nbttaglist);

        return super.writeToNBT(nbt);
    }

}
