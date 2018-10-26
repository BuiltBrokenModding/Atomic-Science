package com.builtbroken.atomic.content.machines.reactor.pipe.inv;

import com.builtbroken.atomic.content.machines.reactor.pipe.TileEntityRodPipe;
import com.builtbroken.atomic.content.machines.reactor.pipe.inv.gui.ContainerRodPipe;
import com.builtbroken.atomic.content.machines.reactor.pipe.inv.gui.GuiRodPipe;
import com.builtbroken.atomic.lib.timer.TickTimer;
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
public class TileEntityRodPipeInv extends TileEntity implements ITickable, IGuiTile
{
    public static final String NBT_INVENTORY = "inventory";

    public static final int SLOT_ROD = 0;

    private ItemStackHandler inventory;
    private final TickTimer inventoryMoveTimer = new TickTimer(10, (IntConsumer) ticks -> moveItems());

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
        if (tile != null && TileEntityRodPipe.canSupport(tile) && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP))
        {
            IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
            if (inventory != null)
            {
                ItemStack stackToInsert = getItem().copy();
                for (int slot = 0; slot < inventory.getSlots() && !stackToInsert.isEmpty(); slot++)
                {
                    if (inventory.isItemValid(slot, stackToInsert))
                    {
                        stackToInsert = inventory.insertItem(slot, stackToInsert, false);
                    }
                }
                setItem(stackToInsert);
            }
        }
    }

    protected ItemStack getItem()
    {
        return getInventory().getStackInSlot(SLOT_ROD);
    }

    protected void setItem(ItemStack stack)
    {
        getInventory().setStackInSlot(SLOT_ROD, stack);
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

                @Override
                protected void onContentsChanged(int slot)
                {
                    TileEntityRodPipeInv.this.markDirty();
                }
            };
        }
        return inventory;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (facing == EnumFacing.UP && TileEntityRodPipe.canSupport(capability))
        {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.UP)
        {
            return (T) getInventory();
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
        getInventory().deserializeNBT(compound.getCompoundTag(NBT_INVENTORY));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag(NBT_INVENTORY, getInventory().serializeNBT());
        return super.writeToNBT(compound);
    }
}
