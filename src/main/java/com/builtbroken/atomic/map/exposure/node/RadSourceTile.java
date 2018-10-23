package com.builtbroken.atomic.map.exposure.node;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.function.IntSupplier;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public class RadSourceTile<E extends TileEntity> extends RadiationSource<E>
{
    private final IntSupplier radFunction;

    public RadSourceTile(E host, IntSupplier radFunction)
    {
        super(host);
        this.radFunction = radFunction;
    }

    @Override
    public int getRadioactiveMaterial()
    {
        return radFunction.getAsInt();
    }

    @Override
    public boolean isStillValid()
    {
        return super.isStillValid() && !host.isInvalid();
    }

    @Override
    public World world()
    {
        return host.getWorld();
    }

    @Override
    public double z()
    {
        return host.getPos().getZ() + 0.5;
    }

    @Override
    public double x()
    {
        return host.getPos().getX() + 0.5;
    }

    @Override
    public double y()
    {
        return host.getPos().getY() + 0.5;
    }

    @Override
    public int zi()
    {
        return host.getPos().getZ();
    }

    @Override
    public int xi()
    {
        return host.getPos().getX();
    }

    @Override
    public int yi()
    {
        return host.getPos().getY();
    }

    @Override
    public boolean equals(Object object)
    {
        if(object == this)
        {
            return true;
        }
        if(object instanceof RadSourceTile)
        {
            if(host != null && ((RadSourceTile)object).host != null)
            {
                return host.getWorld() == ((TileEntity)((RadSourceTile)object).host).getWorld() && host.getPos() == ((TileEntity)((RadSourceTile)object).host).getPos();
            }
            return ((RadSourceTile)object).host == host;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "RadSourceTile[W: " + world().getWorldInfo().getWorldName() + " | D: " + dim() + " | Pos(" + xi() + ", " + yi() + ", " + zi() + ")]@" + hashCode();
    }
}
