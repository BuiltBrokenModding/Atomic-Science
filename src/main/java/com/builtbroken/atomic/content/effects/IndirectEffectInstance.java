package com.builtbroken.atomic.content.effects;

import com.builtbroken.atomic.api.effect.IIndirectEffectInstance;
import com.builtbroken.atomic.api.effect.IIndirectEffectSource;
import com.builtbroken.atomic.api.effect.IIndirectEffectType;

/**
 * Similar in function to {@link net.minecraft.util.DamageSource} but used for indirect effects that do not cause damage.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2018.
 */
public class IndirectEffectInstance implements IIndirectEffectInstance
{
    public final IIndirectEffectType type;
    public final IIndirectEffectSource source;
    public float value;

    public IndirectEffectInstance(IIndirectEffectType type, IIndirectEffectSource source, float value)
    {
        this.type = type;
        this.source = source;
        this.value = value;
    }

    @Override
    public IIndirectEffectType getIndirectEffectType()
    {
        return type;
    }

    @Override
    public IIndirectEffectSource getIndirectEffectSource()
    {
        return source;
    }

    @Override
    public float getIndirectEffectPower()
    {
        return value;
    }
}
