package com.builtbroken.atomic.content.machines.reactor.pipe;

import com.builtbroken.atomic.content.machines.reactor.pipe.gui.ContainerRodPipe;
import com.builtbroken.atomic.content.machines.reactor.pipe.gui.GuiRodPipe;
import com.builtbroken.atomic.lib.TileTimer;
import com.builtbroken.atomic.lib.gui.IGuiTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.IntConsumer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
public class TileEntityRodPipeInv extends TileEntityRodPipe implements ITickable, IGuiTile
{
    public static final String NBT_INVENTORY = "inventory";

    private ItemStackHandler inventory;
    private final TileTimer inventoryMoveTimer = new TileTimer(10, (IntConsumer) ticks -> moveItems());

    private final List<EntityPlayer> playersGUI = new ArrayList();

    @Override
    public void update()
    {
        if (!world.isRemote)
        {
            inventoryMoveTimer.tick();
        }
    }

    protected void moveItems()
    {
        TileEntity tile = world.getTileEntity(getPos().down());
        if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP))
        {
            IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
            if (inventory != null)
            {
                setItem(ItemHandlerHelper.insertItem(inventory, getItem(), false));
            }
        }
    }

    protected ItemStack getItem()
    {
        return getInventory().getStackInSlot(0);
    }

    protected void setItem(ItemStack stack)
    {
        getInventory().setStackInSlot(0, stack);
    }

    public ItemStackHandler getInventory()
    {
        if (inventory == null)
        {
            inventory = new ItemStackHandler(1)
            {
                @Override
                public int getSlotLimit(int slot)
                {
                    return 1;
                }
            };
        }
        return inventory;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.UP)
        {
            return (T) inventory;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player)
    {
        return new ContainerRodPipe(player, this);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player)
    {
        return new GuiRodPipe(player, this);
    }

    @Override
    public Collection<EntityPlayer> getPlayersUsingGui()
    {
        return playersGUI;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        compound.setTag(NBT_INVENTORY, getInventory().serializeNBT());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        getInventory().deserializeNBT(compound.getCompoundTag(NBT_INVENTORY));
        return super.writeToNBT(compound);
    }
}
