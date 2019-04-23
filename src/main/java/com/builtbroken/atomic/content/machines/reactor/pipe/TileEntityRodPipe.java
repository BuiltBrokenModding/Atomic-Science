package com.builtbroken.atomic.content.machines.reactor.pipe;

import com.builtbroken.atomic.content.machines.reactor.fission.controller.TileEntityReactorController;
import com.builtbroken.atomic.content.machines.reactor.fission.core.TileEntityReactorCell;
import com.builtbroken.atomic.content.machines.reactor.pipe.inv.TileEntityRodPipeInv;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 *
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

    public static boolean canSupport(Capability capability)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    public static boolean canSupport(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntityReactorCell
                || tileEntity instanceof TileEntityReactorController
                || tileEntity instanceof TileEntityRodPipe
                || tileEntity instanceof TileEntityRodPipeInv;
    }
}
