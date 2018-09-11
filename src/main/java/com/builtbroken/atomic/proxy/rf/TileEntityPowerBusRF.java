package com.builtbroken.atomic.proxy.rf;

import cofh.api.energy.IEnergyReceiver;
import com.builtbroken.atomic.content.machines.power.TileEntityPowerBus;
import net.minecraft.util.EnumFacing

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public class TileEntityPowerBusRF extends TileEntityPowerBus implements IEnergyReceiver
{
    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate)
    {
        return getPowerNetwork().addEnergy(ProxyRedstoneFlux.toUE(maxReceive), simulate);
    }

    @Override
    public int getEnergyStored(EnumFacing from)
    {
        return 0;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from)
    {
        return Short.MAX_VALUE;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from)
    {
        return true;
    }
}
