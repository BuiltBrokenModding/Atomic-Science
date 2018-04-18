package com.builtbroken.atomic.api.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Apply this to all item armors and it will prevent the player from receiving a specific type of
 * poison.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) & Calclavia
 */
public interface IAntiPoisonArmor extends IArmorSet
{
    /**
     * Use this to block indirect damage types from going through the armor (radiation, poison, etc)
     * <p>
     * If you want to only block a percentage of the damage. First block the entire damage then use
     * {@link #onArmorProtectFromSource(ItemStack, EntityLivingBase, String, float)} to apply the reduced value
     *
     * @param itemStack    - armor
     * @param entityLiving - entity wearing the armor
     * @param sourceType   - type of damage
     * @return true to block the source from going through the armor
     */
    boolean doesArmorProtectFromSource(ItemStack itemStack, EntityLivingBase entityLiving, String sourceType, float value); //TODO add source object to get position, distance, and replace source + damage

    /**
     * Called after indirect damage has been blocked by the armor.
     * <p>
     * Use this to damage the armor or apply secondary effects.
     *
     * @param itemStack    - armor
     * @param entityLiving - entity wearing the armor
     * @param sourceType   - type of damage
     * @param value        - amount of damage (not in health points)
     */
    void onArmorProtectFromSource(ItemStack itemStack, EntityLivingBase entityLiving, String sourceType, float value); //TODO add source object to get position, distance, and replace source + damage

}
