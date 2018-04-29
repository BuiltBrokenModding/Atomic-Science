package com.builtbroken.atomic.content.effects.type;

import com.builtbroken.atomic.api.effect.IIndirectEffectSource;
import com.builtbroken.atomic.content.ASIndirectEffects;
import com.builtbroken.atomic.content.effects.IndirectEffectType;
import com.builtbroken.atomic.content.effects.events.IndirectEffectEntityEvent;
import net.minecraft.entity.Entity;

/**
 * Indirect effect type for radiation
 * <p>
 * Acts as a catch-all for any ionization-radiation type that can harm an entity.
 * <p>
 * Subtypes will funnel into this type to still allow tracking individual types.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/21/2018.
 */
public class IETRadiation extends IndirectEffectType
{
    //As a note, the value is actually REM and not RAD.
    //  This is done to track radiation from different sources
    //  As each source of radiation will cause different scales of damage
    //  However, to keep things simple, all values are converted to REM

    public IETRadiation()
    {
        super("radiation");
    }

    @Override
    public void applyIndirectEffect(IIndirectEffectSource source, Entity target, float power)
    {
        IndirectEffectEntityEvent.Pre effectEntityEvent = new IndirectEffectEntityEvent.Pre(source, this, target, power, power);
        if (!effectEntityEvent.isCanceled())
        {
            ASIndirectEffects.addRadiation(target, effectEntityEvent.appliedPower);
        }
    }
}
