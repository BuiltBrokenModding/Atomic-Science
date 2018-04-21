package com.builtbroken.atomic.content.effects.type;

import com.builtbroken.atomic.api.effect.IIndirectEffectSource;
import com.builtbroken.atomic.config.ConfigRadiation;
import com.builtbroken.atomic.content.ASIndirectEffects;
import com.builtbroken.atomic.content.effects.IndirectEffectType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

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
    public IETRadiation()
    {
        super("radiation");
    }

    @Override
    public void applyIndirectEffect(IIndirectEffectSource source, Entity target, float power)
    {
        //As a note, the value is actually REM and not RAD.
        //  This is done to track radiation from different sources
        //  As each source of radiation will cause different scales of damage
        //  However, to keep things simple, all values are converted to REM

        NBTTagCompound radiation_data = ASIndirectEffects.getRadiationData(target, true);
        //Get last RAD value
        float rads = radiation_data.getFloat(ASIndirectEffects.NBT_RADS);

        //Add
        rads += power;

        //TODO fire radiation receive event to block value
        //TODO fire events to allow changing value

        //Set new value
        radiation_data.setFloat(ASIndirectEffects.NBT_RADS, Math.max(0, Math.min(ConfigRadiation.RADIATION_DEATH_POINT, rads)));

        //Track last time value was set
        radiation_data.setLong(ASIndirectEffects.NBT_RADS_ADD, System.currentTimeMillis());

        //TODO sync entity data to client
    }
}
