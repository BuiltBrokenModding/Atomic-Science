package com.builtbroken.atomic.content.effects.type;

import com.builtbroken.atomic.api.effect.IIndirectEffectSource;
import com.builtbroken.atomic.content.effects.IndirectEffectType;
import com.builtbroken.atomic.content.effects.RadiationEntityEventHandler;
import com.builtbroken.atomic.content.effects.events.IndirectEffectEntityEvent;
import net.minecraft.entity.Entity;

/**
 * Damage taken from external radiation sources. This is not for internal
 * damage caused by radiation exposure.
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/30/2018.
 */
public class IETRadiationDamage extends IndirectEffectType
{
    public IETRadiationDamage()
    {
        super("radiation_damage");
    }

    @Override
    public void applyIndirectEffect(IIndirectEffectSource source, Entity target, float power)
    {
        IndirectEffectEntityEvent.Pre effectEntityEvent = new IndirectEffectEntityEvent.Pre(source, this, target, power, power);
        if (!effectEntityEvent.isCanceled())
        {
            target.attackEntityFrom(RadiationEntityEventHandler.radiationDeathDamage, effectEntityEvent.appliedPower);
        }
    }
}
