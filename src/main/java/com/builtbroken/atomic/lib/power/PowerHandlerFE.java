package com.builtbroken.atomic.lib.power;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Implementation for Forge Energy {@link net.minecraftforge.energy.IEnergyStorage}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/14/2018.
 */
public class PowerHandlerFE extends PowerHandler
{
    @Override
    public boolean canHandle(EnumFacing side, TileEntity tile)
    {
        return tile.hasCapability(CapabilityEnergy.ENERGY, side);
    }

    @Override
    public int addPower(EnumFacing side, TileEntity tile, int power, boolean doAction)
    {
        if (tile.hasCapability(CapabilityEnergy.ENERGY, side))
        {
            IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, side);
            if (storage != null)
            {
                return storage.receiveEnergy(power, !doAction);
            }
        }
        return 0;
    }

    @Override
    public int removePower(EnumFacing side, TileEntity tile, int power, boolean doAction)
    {
        if (tile.hasCapability(CapabilityEnergy.ENERGY, side))
        {
            IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, side);
            if (storage != null)
            {
                return storage.extractEnergy(power, !doAction);
            }
        }
        return 0;
    }


    @Override
    public boolean canHandle(ItemStack stack)
    {
        return stack.hasCapability(CapabilityEnergy.ENERGY, null);
    }

    @Override
    public int addPower(ItemStack stack, int power, boolean doAction)
    {
        return 0;
    }

    @Override
    public int removePower(ItemStack stack, int power, boolean doAction)
    {
        if (stack.hasCapability(CapabilityEnergy.ENERGY, null))
        {
            IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
            if (storage != null)
            {
                return storage.extractEnergy(power, !doAction);
            }
        }
        return 0;
    }

    @Override
    public int getPowerStored(ItemStack stack)
    {
        if (stack.hasCapability(CapabilityEnergy.ENERGY, null))
        {
            IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
            if (storage != null)
            {
                return storage.getEnergyStored();
            }
        }
        return 0;
    }

    public int getMaxPowerStorage(ItemStack stack)
    {
        if (stack.hasCapability(CapabilityEnergy.ENERGY, null))
        {
            IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
            if (storage != null)
            {
                return storage.getMaxEnergyStored();
            }
        }
        return 0;
    }
}
