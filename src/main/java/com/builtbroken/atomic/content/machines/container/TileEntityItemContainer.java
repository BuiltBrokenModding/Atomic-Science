package com.builtbroken.atomic.content.machines.container;

import com.builtbroken.atomic.content.prefab.TileEntityMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/24/2019.
 */
public class TileEntityItemContainer extends TileEntityMachine
{
    private final ItemStackHandler inventory = new ItemStackHandler(1)
    {
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
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag("inventory", inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        tagCompound.setTag("item", getHeldItem().serializeNBT());
        return tagCompound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        if (pkt.getNbtCompound().hasKey("item"))
        {
            readFromNBT(pkt.getNbtCompound());
            _cache = new ItemStack(pkt.getNbtCompound().getCompoundTag("item"));
        }
    }
}
