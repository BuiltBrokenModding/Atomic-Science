package com.builtbroken.atomic.content.effects;

import com.builtbroken.atomic.api.effect.IIndirectEffectSource;
import com.builtbroken.atomic.api.effect.IIndirectEffectType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

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
    public void applyIndirectEffect(IIndirectEffectSource source, Entity target, float power)
    {
        if (target instanceof EntityPlayer)
        {
            ((EntityPlayer) target).sendMessage(new TextComponentString("Tag[" + id + "]"));
        }
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
