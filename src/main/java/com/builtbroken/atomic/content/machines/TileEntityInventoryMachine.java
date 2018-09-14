package com.builtbroken.atomic.content.machines;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public abstract class TileEntityInventoryMachine<I extends IItemHandlerModifiable> extends TileEntityMachine
{
    public static final String NBT_INVENTORY = "inventory";

    private I inventory;

    public I getInventory()
    {
        if(inventory == null)
        {
            inventory = createInventory();
        }
        return inventory;
    }

    protected abstract I createInventory();

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

        return  super.writeToNBT(nbt);
    }

}
