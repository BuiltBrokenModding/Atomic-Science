package com.builtbroken.atomic.lib;

import com.builtbroken.atomic.api.radiation.IRadioactiveItem;
import net.minecraft.item.ItemStack;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/11/2018.
 */
public class RadItemHandler
{
    public static int getRadiationForItem(ItemStack stack)
    {
        int rad = 0;
        if (stack != null && stack.getItem() instanceof IRadioactiveItem)
        {
            return ((IRadioactiveItem) stack.getItem()).getRadioactiveMaterial(stack);
        }
        //TODO read NBT for rad material
        return rad;
    }

}
