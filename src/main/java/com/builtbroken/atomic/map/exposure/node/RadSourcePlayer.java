package com.builtbroken.atomic.map.exposure.node;

import com.builtbroken.atomic.lib.RadItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Wrappers an entity item as a source
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public class RadSourcePlayer extends RadSourceEntity<EntityPlayer>
{
    public RadSourcePlayer(EntityPlayer player)
    {
        super(player);
    }

    @Override
    public int getRadioactiveMaterial()
    {
        if (getHost().isEntityAlive())
        {
            int rad = 0;

            for (int slot = 0; slot < getHost().inventory.getSizeInventory(); slot++)
            {
                ItemStack slotStack = getHost().inventory.getStackInSlot(slot);
                if (slotStack != null)
                {
                    rad += RadItemHandler.getRadiationForItem(slotStack);
                }
            }

            return rad;
        }
        return 0;
    }
}
