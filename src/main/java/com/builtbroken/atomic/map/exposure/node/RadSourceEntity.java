package com.builtbroken.atomic.map.exposure.node;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public abstract class RadSourceEntity<E extends Entity> extends RadiationSource<E>
{
    public RadSourceEntity(E host)
    {
        super(host);
    }

    @Override
    public boolean isStillValid()
    {
        return super.isStillValid() && !host.isDead;
    }

    @Override
    public World world()
    {
        return host.world;
    }

    @Override
    public double z()
    {
        return host.posZ;
    }

    @Override
    public double x()
    {
        return host.posX;
    }

    @Override
    public double y()
    {
        return host.posY;
    }
}
