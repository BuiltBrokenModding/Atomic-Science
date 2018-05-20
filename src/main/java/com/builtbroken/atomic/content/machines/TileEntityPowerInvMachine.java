package com.builtbroken.atomic.content.machines;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

/**
 * Prefab for machines that consume power
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public abstract class TileEntityPowerInvMachine extends TileEntityInventoryMachine
{
    private int energyStored;

    /**
     * Adds energy to the machine
     *
     * @param energy   - energy to add in UE
     * @param doAction - true to do action, false to simulate
     * @return energy added
     */
    public int addEnergy(int energy, boolean doAction)
    {
        int room = getMaxEnergyStored() - getEnergyStored();
        if (room >= energy)
        {
            if (doAction)
            {
                energyStored += energy;
            }
            return energy;
        }
        else
        {
            if (doAction)
            {
                energyStored += room;
            }
            return room;
        }
    }

    protected boolean checkEnergyExtract()
    {
        return getEnergyStored() >= getEnergyUsage();
    }

    protected void extractEnergy()
    {
        energyStored = Math.max(0, energyStored - getEnergyUsage());
    }

    public int getEnergyStored()
    {
        return energyStored;
    }

    public int getMaxEnergyStored()
    {
        return getEnergyUsage() * 10;
    }

    public abstract int getEnergyUsage();

    @Override
    protected void writeGuiPacket(List<Object> dataList, EntityPlayer player)
    {
       dataList.add(energyStored);
    }

    @Override
    protected void readGuiPacket(ByteBuf buf, EntityPlayer player)
    {
        energyStored = buf.readInt();
    }
}
