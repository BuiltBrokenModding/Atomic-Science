package com.builtbroken.atomic.content.effects;

import com.builtbroken.atomic.api.effect.IIndirectEffectType;

/**
 * Implementation of {@link IndirectEffectType}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2018.
 */
public class IndirectEffectType implements IIndirectEffectType
{
    private final String id;

    public IndirectEffectType(String id)
    {
        this.id = id;
    }

    @Override
    public String getEffectTypeID()
    {
        return id;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        else if (object instanceof IndirectEffectType)
        {
            return ((IndirectEffectType) object).getEffectTypeID() == getEffectTypeID();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public String toString()
    {
        return "IndirectEffectType[" + id + "]";
    }
}
