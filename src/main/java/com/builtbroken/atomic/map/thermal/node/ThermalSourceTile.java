package com.builtbroken.atomic.map.thermal.node;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.ref.WeakReference;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public class ThermalSourceTile<E extends TileEntity> extends ThermalSource<E>
{
    private final IntSupplier heatFunction;
    private final BooleanSupplier activeFunction;

    private final WeakReference<E> hostReference;

    public ThermalSourceTile(E host, IntSupplier heatFunction, BooleanSupplier activeFunction)
    {
        this.hostReference = new WeakReference(host);
        this.heatFunction = heatFunction;
        this.activeFunction = activeFunction;
    }

    @Override
    public boolean canGeneratingHeat()
    {
        return getHeatGenerated() > 0 && activeFunction.getAsBoolean();
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
    public int getHeatGenerated()
    {
        return heatFunction.getAsInt();
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
        if(object instanceof ThermalSourceTile)
        {
            if(getHost() != null && ((ThermalSourceTile) object).getHost() != null)
            {
                return getHost().getWorld() == ((TileEntity) ((ThermalSourceTile) object).getHost()).getWorld() && getHost().getPos() == ((TileEntity) ((ThermalSourceTile) object).getHost()).getPos();
            }
            return ((ThermalSourceTile) object).getHost() == getHost();
        }
        return false;
    }

    @Override
    protected String getDebugName()
    {
        return "ThermalSourceTile";
    }

    @Override
    public E getHost()
    {
        return hostReference.get();
    }
}
