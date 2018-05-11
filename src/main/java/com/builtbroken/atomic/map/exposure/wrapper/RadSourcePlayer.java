package com.builtbroken.atomic.map.exposure.wrapper;

import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.lib.RadItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Wrappers an entity item as a source
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public class RadSourcePlayer implements IRadiationSource
{
    public final EntityPlayer player;

    public RadSourcePlayer(EntityPlayer player)
    {
        this.player = player;
    }

    @Override
    public int getRadioactiveMaterial()
    {
        if (player.isEntityAlive())
        {
           int rad = 0;

           for(int slot = 0; slot < player.inventory.getSizeInventory(); slot++)
           {
               ItemStack slotStack = player.inventory.getStackInSlot(slot);
               if(slotStack != null)
               {
                    rad += RadItemHandler.getRadiationForItem(slotStack);
               }
           }

           return rad;
        }
        return 0;
    }

    @Override
    public boolean isRadioactive()
    {
        return getRadioactiveMaterial() > 0;
    }

    @Override
    public World world()
    {
        return player.worldObj;
    }

    @Override
    public double z()
    {
        return player.posZ;
    }

    @Override
    public double x()
    {
        return player.posX;
    }

    @Override
    public double y()
    {
        return player.posY;
    }
}
