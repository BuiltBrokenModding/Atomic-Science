package com.builtbroken.atomic;

import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.api.effect.IIndirectEffectSource;
import com.builtbroken.atomic.config.ConfigRadiation;
import com.builtbroken.atomic.content.effects.IndirectEffectType;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/20/2018.
 */
public class ASIndirectEffects
{
    //Radiation is based on -> https://en.wikipedia.org/wiki/Rad_(unit)
    public static final String NBT_RADS = AtomicScience.PREFIX + "rads";
    public static final String NBT_RADS_ADD = AtomicScience.PREFIX + "rads_add_time";
    public static final String NBT_RADS_REMOVE = AtomicScience.PREFIX + "rads_remove_time";

    public static void register()
    {
        MinecraftForge.EVENT_BUS.register(new ASIndirectEffects());

        AtomicScienceAPI.RADIATION = new IndirectEffectType("radiation")
        {
            @Override
            public void applyIndirectEffect(IIndirectEffectSource source, Entity target, float power)
            {
                float rads = target.getEntityData().getFloat(NBT_RADS);
                rads += power;
                target.getEntityData().setFloat(NBT_RADS, Math.max(0, Math.min(ConfigRadiation.MAX_RADS, rads)));
                target.getEntityData().setLong(NBT_RADS_ADD, System.currentTimeMillis());
                //TODO fire events
                //TODO sync to client
            }
        };
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.entityLiving;
        if (entity.getEntityData().hasKey(NBT_RADS))
        {
            //TODO slowly decrease
            //TODO if over a set amount do damage
        }
    }

    //@SubscribeEvent
    public void onInteractEntity(EntityInteractEvent entityInteractEvent)
    {
        //TODO use to allow inserting items into inventories
    }

    //TODO track entities and tiles
    //TODO use tracking data to cause radiation to spawn from entities
    //TODO create function list to allow entities to control logic
    //TODO create ban list to disable running on some entities (mainly for entities that handle logic themselves)
}
