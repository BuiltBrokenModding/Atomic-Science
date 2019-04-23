package com.builtbroken.atomic.api.armor;

import com.builtbroken.atomic.api.effect.IIndirectEffectInstance;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Apply this to all item armors and it will prevent the player from receiving a specific type of
 * poison.
 *
 *
 * Created by Dark(DarkGuardsman, Robert) & Calclavia
 */
@Deprecated //Being replaced with a capability system
public interface IAntiPoisonArmor extends IArmorSet //TODO convert to capability TODO allow adding to mods via configs
{
    /**
     * Use this to block indirect damage types from going through the armor (radiation, poison, etc)
     * <p>
     * If you want to only block a percentage of the damage. First block the entire damage then use
     * {@link #onArmorProtectFromSource(ItemStack, EntityLivingBase, IIndirectEffectInstance)} to apply the reduced value
     * <p>
     * If the armor requires a set to function this calls should check the set.
     *
     * @param itemStack      - armor
     * @param entityLiving   - entity wearing the armor
     * @param effectInstance - type of damage, source, and power
     * @return true to block the source from going through the armor
     */
    boolean doesArmorProtectFromSource(ItemStack itemStack, EntityLivingBase entityLiving, IIndirectEffectInstance effectInstance);

    /**
     * Called after indirect damage has been blocked by any part of an armor set.
     * <p>
     * Use this to damage the armor or apply secondary effects. Make sure to check that
     * the effect instance is supported. So not to take damage for a type that would not normally
     * be blocked.
     *
     * @param itemStack      - armor
     * @param entityLiving   - entity wearing the armor
     * @param effectInstance - type of damage, source, and power
     */
    void onArmorProtectFromSource(ItemStack itemStack, EntityLivingBase entityLiving, IIndirectEffectInstance effectInstance);


    /**
     * Checks if all armor components are needed to work at protection.
     *
     * @param itemStack      - armor
     * @param entityLiving   - entity wearing the armor
     * @param effectInstance - type of damage, source, and power
     * @return true if all parts are required
     */
    default boolean isFullArmorSetNeeded(ItemStack itemStack, EntityLivingBase entityLiving, IIndirectEffectInstance effectInstance)
    {
        return true;
    }

}
