package com.builtbroken.atomic.map.exposure.wrapper;

import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.api.radiation.IRadioactiveItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;

/**
 * Wrappers an entity item as a source
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public class RadSourceEntityItem implements IRadiationSource
{
    public final EntityItem entityItem;

    public RadSourceEntityItem(EntityItem entity)
    {
        this.entityItem = entity;
    }

    @Override
    public int getRadioactiveMaterial()
    {
        if (entityItem.getEntityItem() != null && entityItem.getEntityItem().getItem() instanceof IRadioactiveItem)
        {
            return ((IRadioactiveItem) entityItem.getEntityItem().getItem()).getRadioactiveMaterial(entityItem.getEntityItem());
        }
        return 0;
    }

    @Override
    public World world()
    {
        return entityItem.worldObj;
    }

    @Override
    public double z()
    {
        return entityItem.posZ;
    }

    @Override
    public double x()
    {
        return entityItem.posX;
    }

    @Override
    public double y()
    {
        return entityItem.posY;
    }
}
