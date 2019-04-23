package com.builtbroken.atomic.api.effect;

import net.minecraft.entity.Entity;

/**
 * Similar in function to {@link net.minecraft.util.DamageSource} but used for indirect effects that do not cause damage.
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2018.
 */
public interface IIndirectEffectInstance
{
    /**
     * Type of effect
     *
     * @return
     */
    IIndirectEffectType getIndirectEffectType();

    /**
     * Source of the effect
     *
     * @return
     */
    IIndirectEffectSource getIndirectEffectSource();

    /**
     * Power of the effect instance
     *
     * @return
     */
    float getIndirectEffectPower();

    /**
     * Called to trigger the effect
     *
     * @param entity - target
     */
    default void applyIndirectEffect(Entity entity)
    {
        getIndirectEffectType().applyIndirectEffect(getIndirectEffectSource(), entity, getIndirectEffectPower());
    }
}
