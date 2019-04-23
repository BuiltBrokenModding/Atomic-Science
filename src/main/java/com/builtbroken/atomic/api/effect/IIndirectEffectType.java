package com.builtbroken.atomic.api.effect;

import net.minecraft.entity.Entity;

/**
 * Form of indirect damage created by a machine or entity. Does not do damage directly to the entity.
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2018.
 */
public interface IIndirectEffectType
{
    /**
     * Unique ID of the effect
     * Example: mod_id:name
     *
     * @return id, lower cased
     */
    String getEffectTypeID();

    /**
     * Called to apply the effect type to the source
     *
     * @param source
     * @param target
     * @param power
     */
    void applyIndirectEffect(IIndirectEffectSource source, Entity target, float power);
}
