package com.builtbroken.atomic.map.exposure.node;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.lang.ref.WeakReference;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public abstract class RadSourceEntity<E extends Entity> extends RadiationSource<E>
{
    private final WeakReference<E> hostReference;

    public RadSourceEntity(E host)
    {
        hostReference = new WeakReference(host);
    }
    @Override
    public boolean isStillValid()
    {
        return super.isStillValid() && !getHost().isDead;
    }

    @Override
    public boolean doesSourceExist()
    {
        return super.doesSourceExist() && !getHost().isDead;
    }

    @Override
    public World world()
    {
        return getHost().world;
    }

    @Override
    public double z()
    {
        return getHost().posZ;
    }

    @Override
    public double x()
    {
        return getHost().posX;
    }

    @Override
    public double y()
    {
        return getHost().posY;
    }

    @Override
    protected String getDebugName()
    {
        return "RadSourceEntity";
    }

    @Override
    public E getHost()
    {
        return hostReference.get();
    }
}
