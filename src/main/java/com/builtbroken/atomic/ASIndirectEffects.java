package com.builtbroken.atomic;

import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.api.effect.IIndirectEffectSource;
import com.builtbroken.atomic.config.ConfigRadiation;
import com.builtbroken.atomic.content.effects.IndirectEffectType;
import net.minecraft.entity.Entity;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/20/2018.
 */
public class ASIndirectEffects
{
    public static final String NBT_RADS = AtomicScience.PREFIX + "rads";

    public static void register()
    {
        AtomicScienceAPI.RADIATION = new IndirectEffectType("radiation")
        {
            @Override
            public void applyIndirectEffect(IIndirectEffectSource source, Entity target, float power)
            {
                float rads = target.getEntityData().getFloat(NBT_RADS);
                rads += power;
                target.getEntityData().setFloat(NBT_RADS, Math.max(0, Math.min(ConfigRadiation.MAX_RADS, rads)));
            }
        };
    }

    //TODO track entities and tiles
    //TODO use tracking data to cause radiation to spawn from entities
    //TODO create function list to allow entities to control logic
    //TODO create ban list to disable running on some entities (mainly for entities that handle logic themselves)
}
