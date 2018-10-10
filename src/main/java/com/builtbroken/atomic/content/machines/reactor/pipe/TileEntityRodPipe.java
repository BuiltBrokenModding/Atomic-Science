package com.builtbroken.atomic.content.machines.reactor.pipe;

import com.builtbroken.atomic.content.machines.reactor.fission.controller.TileEntityReactorController;
import com.builtbroken.atomic.content.machines.reactor.fission.core.TileEntityReactorCell;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
public class TileEntityRodPipe extends TileEntity
{
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (facing == EnumFacing.UP && canSupport(capability))
        {
            TileEntity tile = world.getTileEntity(getPos().down());
            if (tile != null && canSupport(tile) && tile.hasCapability(capability, facing))
            {
                return true;
            }
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (facing == EnumFacing.UP && canSupport(capability))
        {
            TileEntity tile = world.getTileEntity(getPos().down());
            if (tile != null && canSupport(tile))
            {
                T r = tile.getCapability(capability, facing);
                if (r != null)
                {
                    return r;
                }
            }
        }
        return super.getCapability(capability, facing);
    }

    protected boolean canSupport(Capability capability)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    protected boolean canSupport(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntityReactorCell
                || tileEntity instanceof TileEntityReactorController
                || tileEntity instanceof TileEntityRodPipe
                || tileEntity instanceof TileEntityRodPipeInv;
    }

}
