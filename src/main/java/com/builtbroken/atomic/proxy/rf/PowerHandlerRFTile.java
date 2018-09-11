package com.builtbroken.atomic.proxy.rf;

import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import com.builtbroken.atomic.lib.power.PowerHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing

/**
 * Handles RF API for tiles
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class PowerHandlerRFTile extends PowerHandler
{
    @Override
    public boolean canHandle(EnumFacing side, TileEntity tile)
    {
        return (tile instanceof IEnergyProvider || tile instanceof IEnergyReceiver) && ((IEnergyConnection) tile).canConnectEnergy(side);
    }

    @Override
    public int addPower(EnumFacing side, TileEntity tile, int power, boolean doAction)
    {
        if (tile instanceof IEnergyReceiver)
        {
            return ProxyRedstoneFlux.toUE(((IEnergyReceiver) tile).receiveEnergy(side, ProxyRedstoneFlux.toRF(power), !doAction));
        }
        return 0;
    }

    @Override
    public int removePower(EnumFacing side, TileEntity tile, int power, boolean doAction)
    {
        if (tile instanceof IEnergyProvider)
        {
            return ProxyRedstoneFlux.toUE(((IEnergyProvider) tile).extractEnergy(side, ProxyRedstoneFlux.toRF(power), !doAction));
        }
        return 0;
    }
}
