package com.builtbroken.atomic.content.machines.container;

import com.builtbroken.atomic.content.prefab.TileEntityMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/24/2019.
 */
public class TileEntityItemContainer extends TileEntityMachine
{
    public static final String NBT_INVENTORY = "inventory";
    public static final String NBT_ITEM = "item";

    private final ItemStackHandler inventory = new ItemStackHandler(1)
    {
        @Override
        public int getSlotLimit(int slot)
        {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntityItemContainer.this.syncClientNextTick();
        }
    };

    private ItemStack _cache;

    public float rotation = 0;

    public ItemStackHandler getInventory()
    {
        return inventory;
    }

    public ItemStack getHeldItem()
    {
        if (world.isRemote)
        {
            return _cache;
        }
        return inventory.getStackInSlot(0);
    }

    public void setHeldItem(ItemStack stack)
    {
        inventory.setStackInSlot(0, stack);
    }

    public int consumeItems()
    {
        int count = getHeldItem().getCount();
        inventory.extractItem(0, 64, false);
        return count;
    }

    @Override
    protected void writeDescPacket(List<Object> dataList, EntityPlayer player)
    {
        dataList.add(inventory.getStackInSlot(0));
    }

    @Override
    protected void readDescPacket(ByteBuf buf, EntityPlayer player)
    {
        _cache = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return inventory != null;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return (T) inventory;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        inventory.deserializeNBT(compound.getCompoundTag(NBT_INVENTORY));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag(NBT_INVENTORY, inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        tagCompound.setTag(NBT_ITEM, getHeldItem().serializeNBT());
        return tagCompound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        if (pkt.getNbtCompound().hasKey(NBT_ITEM))
        {
            readFromNBT(pkt.getNbtCompound());
            _cache = new ItemStack(pkt.getNbtCompound().getCompoundTag(NBT_ITEM));
        }
    }
}
