package com.builtbroken.atomic.proxy.rf;

import cofh.api.energy.IEnergyReceiver;
import com.builtbroken.atomic.content.machines.power.TileEntityPowerBus;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public class TileEntityPowerBusRF extends TileEntityPowerBus implements IEnergyReceiver
{
    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        return getPowerNetwork().addEnergy(PowerHandlerRF.toUE(maxReceive), simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        return 0;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        return Short.MAX_VALUE;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from)
    {
        return true;
    }
}
