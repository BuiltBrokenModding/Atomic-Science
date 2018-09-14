package com.builtbroken.atomic.content.machines.steam.generator;

import com.builtbroken.atomic.config.ConfigPower;
import com.builtbroken.atomic.config.mods.ConfigIC2;
import com.builtbroken.atomic.content.machines.steam.TileEntitySteamInput;
import com.builtbroken.atomic.lib.power.PowerSystem;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Simple tile to convert steam flow rate into power
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
@Optional.InterfaceList({
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "ic2")
})
public class TileEntitySteamGenerator extends TileEntitySteamInput implements IEnergySource
{
    public float _clientTurbineRotation = 0;
    public float _clientPrevRotation = 0;

    private IEnergyStorage energyStorage = new FakeEnergyStorage();
    private boolean hasValidated = false;

    @Override
    public void firstTick()
    {
        super.firstTick();
    }

    @Override
    protected void update(int ticks)
    {
        super.update(ticks);
        if (isClient())
        {
            _clientTurbineRotation += getRotationSpeed();
            if (_clientTurbineRotation > 360)
            {
                _clientTurbineRotation -= 360f;
                _clientPrevRotation -= 360f;
            }
        }
        PowerSystem.outputPower(world, getPos(), EnumFacing.UP, getPowerToOutput(), true);
    }

    //<editor-fold desc="caps">
    @Override
    @Nullable
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
        {
            return (T) energyStorage;
        }
        return super.getCapability(capability, facing);
    }
    //</editor-fold>

    @SideOnly(Side.CLIENT)
    public float rotate(float delta)
    {
        _clientPrevRotation = _clientPrevRotation + (_clientTurbineRotation - _clientPrevRotation) * delta;
        return _clientPrevRotation;
    }

    public float getRotationSpeed()
    {
        return (getSteamGeneration() / 1000f) * 10;
    }

    public int getPowerToOutput()
    {
        return getSteamGeneration() * ConfigPower.STEAM_TO_UE_POWER;
    }

    //<editor-fold desc="save">
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        return super.writeToNBT(nbt);
    }
    //</editor-fold>

    //<editor-fold desc="ic2 power">
    protected boolean enableIC2Support()
    {
        return ConfigIC2.ENABLE_IC2;
    }


    @Override
    @Optional.Method(modid = "ic2")
    public double getOfferedEnergy()
    {
        if (enableIC2Support())
        {
            return getPowerToOutput() / ConfigIC2.FE_PER_EU; //TODO add config for output limit
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = "ic2")
    public void drawEnergy(double amount)
    {
        //int energy = (int) Math.ceil(amount * ConfigIC2.FE_PER_EU); TODO add back if energy storage changes
        //energyStorage.extractEnergy(energy, false);
    }

    @Override
    @Optional.Method(modid = "ic2")
    public int getSourceTier()
    {
        return 1; //Might need increased to allow more power flow
    }

    @Override
    @Optional.Method(modid = "ic2")
    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side)
    {
        return enableIC2Support()
                && hasCapability(CapabilityEnergy.ENERGY, side);
    }
    //</editor-fold>

    @Override
    public void invalidate()
    {
        super.invalidate();
        if (hasValidated)
        {
            hasValidated = false;
            PowerSystem.forEach(proxy -> proxy.onTileInvalidate(this));
        }
    }

    @Override
    public void validate()
    {
        boolean wasInvalid = tileEntityInvalid;
        super.validate();
        if (!hasValidated && !wasInvalid)
        {
            hasValidated = true;
            PowerSystem.forEach(proxy -> proxy.onTileValidate(this));
        }
    }
}
