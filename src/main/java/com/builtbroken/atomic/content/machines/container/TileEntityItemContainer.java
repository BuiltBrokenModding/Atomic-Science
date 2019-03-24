package com.builtbroken.atomic.content.machines.container;

import com.builtbroken.atomic.content.prefab.TileEntityMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/24/2019.
 */
public class TileEntityItemContainer extends TileEntityMachine
{
    private final IItemHandler inventory = new ItemStackHandler(1);

    private ItemStack _cache;

    public float rotation = 0;

    public IItemHandler getInventory()
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

    @Override
    protected void firstTick(boolean isClient)
    {

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
}
