package com.builtbroken.atomic.content.effects.events;

import com.builtbroken.atomic.api.effect.IIndirectEffectSource;
import com.builtbroken.atomic.api.effect.IIndirectEffectType;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * Radiation event involving an entity
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/21/2018.
 */
public class IndirectEffectEntityEvent extends IndirectEffectEvent
{
    /** Entity being effected by the radiation */
    public final Entity target;

    public IndirectEffectEntityEvent(IIndirectEffectSource source, IIndirectEffectType type, Entity target, float power)
    {
        super(source, type, power);
        this.target = target;
    }

    /**
     * Called before an effect is applied to an entity. Can be used to change the applied
     * power value or to cancel the action.
     * <p>
     * Is {@link Cancelable}
     * <p>
     * Fired on {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}
     */
    @Cancelable
    public static class Pre extends IndirectEffectEntityEvent
    {
        /** Applied power value, for radiation this will be REMs */
        public float appliedPower;

        public Pre(IIndirectEffectSource source, IIndirectEffectType type, Entity target, float power, float appliedPower)
        {
            super(source, type, target, power);
            this.appliedPower = appliedPower;
        }
    }
}
