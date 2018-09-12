package com.builtbroken.atomic.content.effects.events;

import com.builtbroken.atomic.api.effect.IIndirectEffectSource;
import com.builtbroken.atomic.api.effect.IIndirectEffectType;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Base type for all indirect effects (radiation)
 * <p>
 * Currently set as internal while events are being developed. Do not use until they are in the API package.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/21/2018.
 */
public class IndirectEffectEvent extends Event
{
    /** Source of the radiation */
    public final IIndirectEffectSource source;
    /** Type of effect */
    public final IIndirectEffectType type;
    /** Power level of the radiation */
    public final float power;

    public IndirectEffectEvent(IIndirectEffectSource source, IIndirectEffectType type, float power)
    {
        this.source = source;
        this.type = type;
        this.power = power;
    }
}
