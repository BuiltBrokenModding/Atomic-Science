package com.builtbroken.atomic.content.machines.pipe.reactor.pass;

import com.builtbroken.atomic.content.machines.pipe.imp.TileEntityDirectionalPipe;
import com.builtbroken.atomic.content.machines.reactor.fission.controller.TileEntityReactorController;
import com.builtbroken.atomic.content.machines.reactor.fission.core.TileEntityReactorCell;
import com.builtbroken.atomic.content.machines.pipe.reactor.inv.TileEntityRodPipeInv;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
public class TileEntityRodPipe extends TileEntityDirectionalPipe
{
    @Override
    public boolean canSupportDirection(EnumFacing facing)
    {
        return facing == EnumFacing.UP;
    }

    @Override
    protected EnumFacing getOutDirection(EnumFacing input)
    {
        return EnumFacing.DOWN;
    }

    @Override
    public boolean canSupport(Capability capability)
    {
        return canPipeSupport(capability);
    }

    @Override
    public boolean canSupport(TileEntity tileEntity)
    {
        return canPipeSupport(tileEntity);
    }

    public static boolean canPipeSupport(Capability capability)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    public static boolean canPipeSupport(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntityReactorCell
                || tileEntity instanceof TileEntityReactorController
                || tileEntity instanceof TileEntityRodPipe
                || tileEntity instanceof TileEntityRodPipeInv;
    }
}
