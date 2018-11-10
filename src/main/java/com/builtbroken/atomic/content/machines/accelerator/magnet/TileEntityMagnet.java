package com.builtbroken.atomic.content.machines.accelerator.magnet;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/10/2018.
 */
public class TileEntityMagnet extends TileEntity
{
    private TileEntity owner;

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if(getOwner() != null)
        {
            return getOwner().hasCapability(capability, facing);
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if(getOwner() != null)
        {
            return getOwner().getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }

    public TileEntity getOwner()
    {
        if(owner != null && owner.isInvalid())
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
