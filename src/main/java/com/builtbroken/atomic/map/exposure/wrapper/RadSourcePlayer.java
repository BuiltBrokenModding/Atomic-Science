package com.builtbroken.atomic.map.exposure.wrapper;

import com.builtbroken.atomic.lib.RadItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Wrappers an entity item as a source
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
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
        if (host.isEntityAlive())
        {
            int rad = 0;

            for (int slot = 0; slot < host.inventory.getSizeInventory(); slot++)
            {
                ItemStack slotStack = host.inventory.getStackInSlot(slot);
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
