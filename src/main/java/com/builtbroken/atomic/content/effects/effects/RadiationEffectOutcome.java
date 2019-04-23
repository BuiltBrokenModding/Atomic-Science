package com.builtbroken.atomic.content.effects.effects;

import net.minecraft.entity.EntityLivingBase;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/30/2018.
 */
public class RadiationEffectOutcome
{
    public final FloatSupplier radiationStartPoint;
    public RadConsumer action;

    public RadiationEffectOutcome(FloatSupplier radiationStartPoint)
    {
        this.radiationStartPoint = radiationStartPoint;
    }

    public RadiationEffectOutcome(FloatSupplier radiationStartPoint, RadConsumer action)
    {
        this.radiationStartPoint = radiationStartPoint;
        this.action = action;
    }

    public void applyEffects(EntityLivingBase entity, float currentRems, float exposure)
    {
        if (action != null)
        {
            action.accept(entity, currentRems, exposure);
        }
    }
}
