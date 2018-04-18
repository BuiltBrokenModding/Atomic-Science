package com.builtbroken.atomic.api.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

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

    /**
     * Are all armor peaces needed in order for the protection to work. This changes how damage is
     * applied. If all parts are needed then damage is averaged. If one part is needed then damage
     * is reduced per peace of armor.
     *
     * @param armorStack - the armor item
     * @param entity     - entity wearing the armor
     * @param source     - source of the damage
     * @param data       - array of extra data, depends on what is doing the damage. It should always
     *                   start with a string of what the data is going to contain. An example is "Electrical", Voltage
     * @return true if all parts are required
     */
    @Deprecated //Will be replaced with a method not using array of objects or damage source
    default boolean areAllArmorPartsNeeded(ItemStack armorStack, EntityLivingBase entity, DamageSource source, Object... data) //TODO see if data is needed
    {
        return true;
    }
}
