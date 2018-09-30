package com.builtbroken.atomic.content.effects.effects;

import net.minecraft.entity.EntityLivingBase;

@FunctionalInterface
public interface RadConsumer {

    void accept(EntityLivingBase base, float entity_rem, float environmental_rem);
}