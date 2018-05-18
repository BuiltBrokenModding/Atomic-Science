package com.builtbroken.atomic.content.machines.steam.generator;

import cofh.api.energy.IEnergyProvider;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Version of the steam generator that outputs RF power
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class TileEntitySteamGenRF extends TileEntitySteamGenerator implements IEnergyProvider
{
    @Override
    public boolean canConnectEnergy(ForgeDirection from)
    {
        return from == ForgeDirection.UP;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        return getPowerToOutput();
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        return getPowerToOutput();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        return getPowerToOutput() * 10;
    }
}
