package com.builtbroken.atomic.proxy.bc;

import buildcraft.api.mj.MjAPI;
import com.builtbroken.atomic.config.mods.ConfigMod;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/30/2018.
 */
public class MjCapabilityProvider implements ICapabilityProvider
{
    private final TileEntity tile;
    private final MjEnergyWrapper[] wrappers = new MjEnergyWrapper[6];

    public MjCapabilityProvider(TileEntity tile)
    {
        this.tile = tile;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (ConfigMod.BUILDCRAFT.ENABLE_BUILDCRAFT)
        {
            return capability == MjAPI.CAP_RECEIVER || capability == MjAPI.CAP_CONNECTOR || capability == MjAPI.CAP_PASSIVE_PROVIDER;
        }
        return false;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (hasCapability(capability, facing))
        {
            if (wrappers[facing.ordinal()] == null)
            {
                wrappers[facing.ordinal()] = new MjEnergyWrapper(tile, facing);
            }
            return (T) wrappers[facing.ordinal()];
        }
        return null;
    }
}
