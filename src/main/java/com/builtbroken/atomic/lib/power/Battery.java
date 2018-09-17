package com.builtbroken.atomic.lib.power;

import net.minecraftforge.energy.EnergyStorage;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/17/2018.
 */
public class Battery extends EnergyStorage
{
    public Battery(int capacity)
    {
        super(capacity);
    }

    public Battery(int capacity, int maxTransfer)
    {
        super(capacity, maxTransfer);
    }

    public Battery(int capacity, int maxReceive, int maxExtract)
    {
        super(capacity, maxReceive, maxExtract);
    }

    public Battery(int capacity, int maxReceive, int maxExtract, int energy)
    {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        int prev_energy = energy;
        int receive = super.receiveEnergy(maxReceive, simulate);
        if (simulate && prev_energy != energy)
        {
            onEnergyChanged(prev_energy, energy);
        }
        return receive;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        int prev_energy = energy;
        int extract = super.extractEnergy(maxExtract, simulate);
        if (simulate && prev_energy != energy)
        {
            onEnergyChanged(prev_energy, energy);
        }
        return extract;
    }

    protected void onEnergyChanged(int prev, int current)
    {

    }

    public void setEnergy(int energy)
    {
        this.energy = energy;
    }
}
