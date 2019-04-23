package com.builtbroken.atomic.lib.power;

import net.minecraftforge.energy.IEnergyStorage;

import java.util.function.IntSupplier;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/17/2018.
 */
public class Battery implements IEnergyStorage
{
    protected int energy;
    protected IntSupplier capacityFunction;

    public Battery(int capacity)
    {
        capacityFunction = () -> capacity;
    }

    public Battery(IntSupplier capacityFunction)
    {
        this.capacityFunction = capacityFunction;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        //Check if we can receive
        if (!canReceive())
        {
            return 0;
        }

        //Record previous energy
        final int prev_energy = energy;

        //Do receive
        int energyReceived = Math.min(getMaxEnergyStored() - energy, Math.min(this.getReceiveLimit(), maxReceive));
        if (!simulate)
        {
            energy += energyReceived;
        }

        //Check for changes
        if (simulate && prev_energy != energy)
        {
            onEnergyChanged(prev_energy, energy);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        //Check if we can extract
        if (!canExtract())
        {
            return 0;
        }

        //Record previous
        final int prev_energy = energy;

        //Do extraction
        int energyExtracted = Math.min(energy, Math.min(this.getExtractLimit(), maxExtract));
        if (!simulate)
        {
            energy -= energyExtracted;
        }

        //Check for changes
        if (simulate && prev_energy != energy)
        {
            onEnergyChanged(prev_energy, energy);
        }
        return energyExtracted;
    }

    protected void onEnergyChanged(int prev, int current)
    {

    }

    public void setEnergy(int energy)
    {
        this.energy = energy;
    }

    @Override
    public int getEnergyStored()
    {
        return energy;
    }

    @Override
    public int getMaxEnergyStored()
    {
        return capacityFunction.getAsInt();
    }

    @Override
    public boolean canExtract()
    {
        return this.getExtractLimit() > 0;
    }

    @Override
    public boolean canReceive()
    {
        return this.getReceiveLimit() > 0;
    }

    public int getExtractLimit()
    {
        return getMaxEnergyStored();
    }

    public int getReceiveLimit()
    {
        return getMaxEnergyStored();
    }
}
