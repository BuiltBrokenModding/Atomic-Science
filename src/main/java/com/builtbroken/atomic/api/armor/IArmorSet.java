package com.builtbroken.atomic.api.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * Applied to Item to note that it is part of a set of armor
 *
 *
 * Created by Dark(DarkGuardsman, Robert)
 */
@Deprecated //Being replaced with a capability system
public interface IArmorSet
{
    /**
     * Type of armor by slot index
     *
     * @return enum of equipment slot
     */
    EntityEquipmentSlot getArmorSlot();

    /**
     * Checks if the armor is part of the same set
     *
     * @param armorStack   - armor
     * @param compareStack - stack to check
     * @return true if its part of the set
     */
    default boolean isArmorPartOfSet(ItemStack armorStack, ItemStack compareStack)
    {
        return armorStack.getItem() == compareStack.getItem();
    }
}
