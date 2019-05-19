package com.builtbroken.atomic.content.machines.container.item;

import com.builtbroken.atomic.config.content.ConfigContent;
import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.prefab.TileEntityMachine;
import com.builtbroken.atomic.lib.power.Battery;
import com.builtbroken.atomic.lib.timer.TickTimerTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
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
    //NBT tags
    public static final String NBT_INVENTORY = "inventory";
    public static final String NBT_ITEM = "item";
    public static final String NBT_ENERGY = "energy";
    public static final String NBT_FLUID = "fluid";


    //Render cache
    private ItemStack _cache;

    //Render rotation
    public float rotation = 0;

    //Capabilities
    //Only inventory should be exposed to external, tank and battery are for internal recipes
    public final Battery internalBattery = new Battery(() -> ConfigContent.POWER_USAGE.PARTICLE_CAPTURE_COST + ConfigContent.POWER_USAGE.PARTICLE_CONTAINMENT_COST * 10);
    protected final FluidTank internalTank = new FluidTank(Fluid.BUCKET_VOLUME / 10);
    private final ItemContainerInventory inventory = new ItemContainerInventory(this);

    public TileEntityItemContainer()
    {
        tickServer.add(TickTimerTileEntity.newSimple((tick) -> consumeEnergy()));
        tickServer.add(TickTimerTileEntity.newSimple((tick) -> exportFluid()));
    }

    public void consumeEnergy()
    {
        if (hasFluidStored())
        {
            final int consumed = internalBattery.extractEnergy(ConfigContent.POWER_USAGE.PARTICLE_CONTAINMENT_COST, false); //TODO scale with particle count
            if (consumed != ConfigContent.POWER_USAGE.PARTICLE_CONTAINMENT_COST)
            {
                FluidStack fluidStack = internalTank.drain(Integer.MAX_VALUE, true);
                if (fluidStack != null)
                {
                    world.newExplosion(null, x(), y(), zi(), fluidStack.amount / 5, true, false);
                }
            }
        }
    }

    //<editor-fold desc="fluid">

    /**
     * Adds antimmater to the internal tank from a recipe.
     * <p>
     * Do not use this long term as it will be replaced
     *
     * @param amount  - amount to add
     * @param eatItem - true to eat items if left inside
     * @param explode - true to explode if item was left inside
     * @return amount added
     */
    public int addAntimatter(int amount, boolean eatItem, boolean explode, boolean consumeEnergy)
    {
        if (consumeEnergy)
        {
            final int consumed = internalBattery.extractEnergy(ConfigContent.POWER_USAGE.PARTICLE_CONTAINMENT_COST, false);
            if (consumed != ConfigContent.POWER_USAGE.PARTICLE_CONTAINMENT_COST)
            {
                world.newExplosion(null, x(), y(), zi(), amount / 5, true, false);
                return amount;
            }
        }
        //Destroy item held
        if (!getHeldItem().isEmpty())
        {
            if (eatItem)
            {
                setHeldItem(ItemStack.EMPTY);
            }
            if (explode)
            {
                world.newExplosion(null, x(), y(), zi(), amount / 5, true, false);
            }
            return amount;
        }
        return internalTank.fill(new FluidStack(ASFluids.ANTIMATTER.fluid, amount), true);
    }

    public boolean hasFluidStored()
    {
        return internalTank.getFluid() != null && internalTank.getFluidAmount() > 0;
    }

    public void exportFluid()
    {
        if (hasFluidStored())
        {
            final IFluidHandler handler = getCapabilityOnSide(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN);
            if (handler != null)
            {
                FluidUtil.tryFluidTransfer(handler, internalTank, internalTank.getFluidAmount(), true);
            }
        }
    }
    //<editor-fold>

    //<editor-fold desc="inventory">
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
    //</editor-fold>

    //<editor-fold desc="network">
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
    //</editor-fold>

    //<editor-fold desc="capability">
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
    //</editor-fold>

    //<editor-fold desc="save">
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        inventory.deserializeNBT(compound.getCompoundTag(NBT_INVENTORY));
        internalTank.readFromNBT(compound.getCompoundTag(NBT_FLUID));
        internalBattery.setEnergy(compound.getInteger(NBT_ENERGY));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag(NBT_INVENTORY, inventory.serializeNBT());
        compound.setTag(NBT_FLUID, internalTank.writeToNBT(new NBTTagCompound()));
        compound.setInteger(NBT_ENERGY, internalBattery.getEnergyStored());
        return super.writeToNBT(compound);
    }

    //</editor-fold>

}
