package com.builtbroken.atomic.content.machines.accelerator.magnet;

import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.content.machines.accelerator.tube.powered.TileEntityAcceleratorTubePowered;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

/**
 * Created by Dark(DarkGuardsman, Robert) on 11/10/2018.
 */
public class TileEntityMagnet extends TileEntity
{
    private TileEntity owner;

    private final CapabilityMagnet capabilityMagnet = new CapabilityMagnet(this);

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (capability == AtomicScienceAPI.ACCELERATOR_MAGNET_CAPABILITY || capability == CapabilityEnergy.ENERGY && getOwner() != null)
        {
            return true;
        }
        else if (getOwner() != null && facing != null && capability == CapabilityEnergy.ENERGY) //TODO add a can support capability option
        {
            return getOwner().hasCapability(capability, facing);
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == AtomicScienceAPI.ACCELERATOR_MAGNET_CAPABILITY)
        {
            return (T) capabilityMagnet;
        }
        else if (capability == CapabilityEnergy.ENERGY)
        {
            return getOwner() instanceof TileEntityAcceleratorTubePowered ? (T) ((TileEntityAcceleratorTubePowered) getOwner()).battery : null;
        }
        else if (getOwner() != null && facing != null && capability == CapabilityEnergy.ENERGY)
        {
            return getOwner().getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }

    public TileEntity getOwner()
    {
        if (owner != null && owner.isInvalid())
        {
            owner = null;
        }
        return owner;
    }

    public void setOwner(TileEntity owner)
    {
        this.owner = owner;
    }
}
