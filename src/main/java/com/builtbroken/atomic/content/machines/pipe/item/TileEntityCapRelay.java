package com.builtbroken.atomic.content.machines.pipe.item;

import com.builtbroken.atomic.content.machines.pipe.imp.TileEntityDirectionalPipe;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Generic block that allows passing through any capability to the connected device
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2019.
 */
public class TileEntityCapRelay extends TileEntityDirectionalPipe
{
    private EnumFacing _directionCache;

    @Override
    public EnumFacing getDirection()
    {
        if (_directionCache == null)
        {
            _directionCache = super.getDirection();
        }
        return _directionCache;
    }

    @Override
    public void onDirectionChanged(EnumFacing direction)
    {
        _directionCache = direction;
    }

    @Override
    public boolean canSupportDirection(EnumFacing facing)
    {
        //All but output side
        return getDirection() != facing;
    }

    @Override
    public boolean canSupport(Capability capability)
    {
        return true;
    }
}
