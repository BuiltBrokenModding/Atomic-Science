package com.builtbroken.atomic.map.exposure.node;

import com.builtbroken.atomic.api.radiation.IRadioactiveItem;
import com.builtbroken.atomic.config.logic.ConfigRadiation;
import com.builtbroken.atomic.lib.RadItemHandler;
import net.minecraft.entity.item.EntityItem;

/**
 * Wrappers an entity item as a source
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public class RadSourceEntityItem extends RadSourceEntity<EntityItem>
{
    public RadSourceEntityItem(EntityItem entity)
    {
        super(entity);
    }

    @Override
    public int getRadioactiveMaterial()
    {
        return RadItemHandler.getRadiationForItem(host.getItem());
    }

    public static RadSourceEntityItem build(EntityItem entityItem)
    {
        if (ConfigRadiation.ENABLE_ENTITY_ITEMS)
        {
            if (entityItem.getItem() != null && entityItem.getItem().getItem() instanceof IRadioactiveItem) //TODO change over to capability
            {
                return new RadSourceEntityItem(entityItem);
            }
        }
        return null;
    }
}
