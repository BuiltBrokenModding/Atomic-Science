package com.builtbroken.atomic.map.exposure.wrapper;

import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.api.radiation.IRadioactiveItem;
import com.builtbroken.atomic.config.ConfigRadiation;
import com.builtbroken.atomic.lib.RadItemHandler;
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
        return RadItemHandler.getRadiationForItem(entityItem.getEntityItem());
    }

    @Override
    public boolean isRadioactive()
    {
        return getRadioactiveMaterial() > 0;
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

    public static RadSourceEntityItem build(EntityItem entityItem)
    {
        if (ConfigRadiation.ENABLE_ENTITY_ITEMS)
        {
            if (entityItem.getEntityItem() != null && entityItem.getEntityItem().getItem() instanceof IRadioactiveItem)
            {
                return new RadSourceEntityItem(entityItem);
            }
        }
        return null;
    }

}
