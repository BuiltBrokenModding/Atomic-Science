package com.builtbroken.atomic.content.effects.effects;

import net.minecraft.entity.EntityLivingBase;

import java.util.function.BiConsumer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/30/2018.
 */
public class RadiationEffectOutcome
{
    public final FloatSupplier radiationStartPoint;
    public BiConsumer<EntityLivingBase, Float> action;

    public RadiationEffectOutcome(FloatSupplier radiationStartPoint)
    {
        this.radiationStartPoint = radiationStartPoint;
    }

    public RadiationEffectOutcome(FloatSupplier radiationStartPoint, BiConsumer<EntityLivingBase, Float> action)
    {
        this.radiationStartPoint = radiationStartPoint;
        this.action = action;
    }

    public void applyEffects(EntityLivingBase entity, float currentRems)
    {
        if (action != null)
        {
            action.accept(entity, currentRems);
        }
    }
}
