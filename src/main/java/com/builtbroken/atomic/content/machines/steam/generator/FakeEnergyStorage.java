package com.builtbroken.atomic.content.machines.steam.generator;

import net.minecraftforge.energy.IEnergyStorage;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/14/2018.
 */
public class FakeEnergyStorage implements IEnergyStorage //TODO consider making a proper output system
{
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        return 0;
    }

    @Override
    public int getEnergyStored()
    {
        return 0;
    }

    @Override
    public int getMaxEnergyStored()
    {
        return Integer.MAX_VALUE / 2;
    }

    @Override
    public boolean canExtract()
    {
        return true;
    }

    @Override
    public boolean canReceive()
    {
        return false;
    }
}
