package com.builtbroken.atomic.api.armor;

import net.minecraft.item.ItemStack;

/**
 * Applied to Item to note that it is part of a set of armor
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert)
 */
public interface IArmorSet
{
    /**
     * Type of armor by slot index
     * 0 -> helmet
     * 1 -> chestplate
     * 2 -> leggings
     * 3 -> boots
     *
     * @return armor index 0-3
     */
    int getArmorType(); //TODO convert to enum?

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
