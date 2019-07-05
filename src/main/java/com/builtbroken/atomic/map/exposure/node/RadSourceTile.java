package com.builtbroken.atomic.map.exposure.node;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.ref.WeakReference;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public class RadSourceTile<E extends TileEntity> extends RadiationSource<E>
{
    private final IntSupplier radFunction;
    private final BooleanSupplier activeFunction;

    private final WeakReference<E> hostReference;

    public RadSourceTile(E host, IntSupplier radFunction, BooleanSupplier activeFunction)
    {
        hostReference = new WeakReference(host);
        this.radFunction = radFunction;
        this.activeFunction = activeFunction;
    }

    @Override
    public boolean isRadioactive()
    {
        return super.isRadioactive() && activeFunction.getAsBoolean();
    }

    @Override
    public int getRadioactiveMaterial()
    {
        return radFunction.getAsInt();
    }

    @Override
    public boolean doesSourceExist()
    {
        return world() != null
                && getHost() != null
                && !getHost().isInvalid()
                //Fix for chunk ghosting
                && world().getTileEntity(getPos()) == getHost();
    }

    @Override
    public World world()
    {
        return getHost().getWorld();
    }

    @Override
    public double z()
    {
        return getHost().getPos().getZ() + 0.5;
    }

    @Override
    public double x()
    {
        return getHost().getPos().getX() + 0.5;
    }

    @Override
    public double y()
    {
        return getHost().getPos().getY() + 0.5;
    }

    @Override
    public int zi()
    {
        return getHost().getPos().getZ();
    }

    @Override
    public int xi()
    {
        return getHost().getPos().getX();
    }

    @Override
    public int yi()
    {
        return getHost().getPos().getY();
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
            if(getHost() != null && ((RadSourceTile) object).getHost() != null)
            {
                return getHost().getWorld() == ((TileEntity) ((RadSourceTile) object).getHost()).getWorld() && getHost().getPos() == ((TileEntity) ((RadSourceTile) object).getHost()).getPos();
            }
            return ((RadSourceTile) object).getHost() == getHost();
        }
        return false;
    }

    @Override
    protected String getDebugName()
    {
        return "RadSourceTile";
    }

    @Override
    public E getHost()
    {
        return hostReference.get();
    }
}
