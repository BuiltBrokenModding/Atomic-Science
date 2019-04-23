package com.builtbroken.atomic.proxy.bc;

import buildcraft.api.mj.IMjConnector;
import buildcraft.api.mj.IMjPassiveProvider;
import buildcraft.api.mj.IMjReceiver;
import com.builtbroken.atomic.config.mods.ConfigMod;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 6/30/2018.
 */
public class MjEnergyWrapper implements IMjReceiver, IMjPassiveProvider
{
    private final TileEntity tile;
    private final EnumFacing side;

    public MjEnergyWrapper(TileEntity tile, EnumFacing side)
    {
        this.tile = tile;
        this.side = side;
    }

    @Override
    public long getPowerRequested()
    {
        if (ConfigMod.BUILDCRAFT.ENABLE_BUILDCRAFT)
        {
            IEnergyStorage energyStorage = tile.getCapability(CapabilityEnergy.ENERGY, side);
            if (energyStorage != null)
            {
                int energyNeeded = energyStorage.receiveEnergy(Integer.MAX_VALUE, true); //TODO consider a max load to prevent issues
                return (long) Math.floor(PowerHandlerMJ.toBuildcraftEnergy(energyNeeded));
            }
        }
        return 0;
    }

    @Override
    public long receivePower(long microJoules, boolean simulate)
    {
        if (ConfigMod.BUILDCRAFT.ENABLE_BUILDCRAFT)
        {
            IEnergyStorage energyStorage = tile.getCapability(CapabilityEnergy.ENERGY, side);
            if (energyStorage != null && energyStorage.canReceive())
            {
                //Convert to FE
                int energy = (int) Math.floor(PowerHandlerMJ.toForgeEnergy(microJoules));

                //Get amount received
                int taken = energyStorage.receiveEnergy(energy, simulate);

                //Convert
                long taken_mj = (long) Math.ceil(PowerHandlerMJ.toBuildcraftEnergy(taken));

                //Return remain
                return microJoules - taken_mj;
            }
        }
        return microJoules;
    }

    @Override
    public boolean canConnect(@Nonnull IMjConnector other)
    {
        return ConfigMod.BUILDCRAFT.ENABLE_BUILDCRAFT;
    }

    @Override
    public long extractPower(long min, long max, boolean simulate)
    {
        if (ConfigMod.BUILDCRAFT.ENABLE_BUILDCRAFT)
        {
            IEnergyStorage energyStorage = tile.getCapability(CapabilityEnergy.ENERGY, side);
            if (energyStorage != null && energyStorage.canExtract())
            {
                int fe = (int) Math.floor(PowerHandlerMJ.toForgeEnergy(max));
                fe = energyStorage.extractEnergy(fe, true);

                long energy = (long) Math.floor(PowerHandlerMJ.toBuildcraftEnergy(fe));

                if (energy > min)
                {
                    energy = Math.min(energy, max);
                    if (!simulate)
                    {
                        fe = (int) Math.ceil(PowerHandlerMJ.toForgeEnergy(energy));
                        energyStorage.extractEnergy(fe, false);
                    }
                    return fe;
                }
            }
        }
        return 0;
    }
}
