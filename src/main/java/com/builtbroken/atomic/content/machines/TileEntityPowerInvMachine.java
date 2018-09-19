package com.builtbroken.atomic.content.machines;

import com.builtbroken.atomic.config.content.ConfigContent;
import com.builtbroken.atomic.lib.power.Battery;
import com.builtbroken.atomic.lib.power.PowerSystem;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Prefab for machines that consume power
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public abstract class TileEntityPowerInvMachine<I extends IItemHandlerModifiable> extends TileEntityInventoryMachine<I>
{
    public static final String NBT_ENERGY = "energy";

    private Battery energyStorage;

    /**
     * Gets energy usage of the machine
     * @return
     */
    public abstract int getEnergyUsage();

    /**
     * Energy storage
     * @return
     */
    public Battery getEnergyStorage()
    {
        if (energyStorage == null)
        {
            energyStorage = createEnergyStorage();
        }
        return energyStorage;
    }

    /**
     * Creates energy storage
     * @return
     */
    protected Battery createEnergyStorage()
    {
        return new Battery(getMaxEnergyStored())
        {
            @Override
            protected void onEnergyChanged(int prev, int current)
            {
                syncClientNextTick(); //TODO sync less often, maybe sync (hasEnergy) instead
            }
        };
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
        {
            return ConfigContent.POWER_USAGE.ENABLE;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY && ConfigContent.POWER_USAGE.ENABLE)
        {
            return (T) getEnergyStorage();
        }
        return super.getCapability(capability, facing);
    }

    /**
     * Checks if there is enough energy to run usage cycle
     * @return
     */
    protected boolean checkEnergyExtract()
    {
        return !ConfigContent.POWER_USAGE.ENABLE || getEnergyStored() >= getEnergyUsage();
    }

    /**
     * Extracts usage energy
     */
    protected void extractEnergy()
    {
        if (ConfigContent.POWER_USAGE.ENABLE)
        {
            getEnergyStorage().extractEnergy(getEnergyUsage(), false);
        }
    }

    /**
     * Gets energy stored
     * @return
     */
    public int getEnergyStored()
    {
        return !ConfigContent.POWER_USAGE.ENABLE ? getMaxEnergyStored() : getEnergyStorage().getEnergyStored();
    }

    /**
     * Gets max energy storage
     * @return
     */
    public int getMaxEnergyStored()
    {
        return getEnergyUsage() * 10;
    }

    /**
     * Drains energy items in slot into energy storage
     * @param slot
     */
    protected void drainBattery(int slot)
    {
        if (ConfigContent.POWER_USAGE.ENABLE)
        {
            ItemStack itemStack = getInventory().getStackInSlot(slot);

            //If item has power
            int power = PowerSystem.getEnergyStored(itemStack);
            if (power > 0)
            {
                //Check power pull
                power = PowerSystem.dischargeItem(itemStack, power, false);

                //Add power
                int added = getEnergyStorage().receiveEnergy(power, false);

                //Remove power
                PowerSystem.dischargeItem(itemStack, added, true);

                //Trigger slot update
                getInventory().setStackInSlot(slot, itemStack);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        getEnergyStorage().setEnergy(nbt.getInteger(NBT_ENERGY));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger(NBT_ENERGY, getEnergyStorage().getEnergyStored());
        return super.writeToNBT(nbt);
    }

    @Override
    protected void writeDescPacket(List<Object> dataList, EntityPlayer player)
    {
        dataList.add(getEnergyStored());
    }

    @Override
    protected void readDescPacket(ByteBuf buf, EntityPlayer player)
    {
        getEnergyStorage().setEnergy(buf.readInt());
    }

    @Override
    protected void writeGuiPacket(List<Object> dataList, EntityPlayer player)
    {
        dataList.add(getEnergyStored());
    }

    @Override
    protected void readGuiPacket(ByteBuf buf, EntityPlayer player)
    {
        getEnergyStorage().setEnergy(buf.readInt());
    }
}
