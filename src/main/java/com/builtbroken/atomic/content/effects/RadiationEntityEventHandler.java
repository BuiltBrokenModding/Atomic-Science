package com.builtbroken.atomic.content.effects;

import com.builtbroken.atomic.config.ConfigRadiation;
import com.builtbroken.atomic.content.ASIndirectEffects;
import com.builtbroken.atomic.lib.network.netty.PacketSystem;
import com.builtbroken.atomic.lib.network.packet.sync.PacketPlayerRadiation;
import com.builtbroken.atomic.map.RadiationSystem;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Handles evens and tracking radiation on the entities
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/29/2018.
 */
public class RadiationEntityEventHandler
{
    //TODO use Java 8 functions or interface object to trigger effects (makes the code easier to work with)
    //TODO slowly decrease (1 rad per 5 min with an exponential curve, do not remove negative effects)
    //TODO reduce max HP base on rad level (1Hp per 10 rad, max of 10hp [50%])
    //TODO if over 200 start removing hp slowly (simulation radiation poisoning)
    //TODO if over 100 have character suffer potion effects
    //TODO if over 1000, kill character

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (!event.entity.worldObj.isRemote)
        {
            EntityLivingBase entity = event.entityLiving;

            //Increase radiation from environment
            applyExposure(entity);

            //Sync data to client if changes
            if (entity instanceof EntityPlayerMP)
            {
                syncPlayerData((EntityPlayerMP) entity);
            }
        }
    }

    /**
     * Applies exposure and reduction of radiation over time
     *
     * @param entity
     */
    protected void applyExposure(EntityLivingBase entity)
    {
        //Apply exposure if greater than zero
        float remExposure = RadiationSystem.INSTANCE.getRemExposure(entity);
        if (remExposure > 0)
        {
            ASIndirectEffects.addRadiation(entity, remExposure);
        }
        else if (ASIndirectEffects.hasRadiationData(entity))
        {
            //Get data
            final NBTTagCompound data = ASIndirectEffects.getRadiationData(entity, false);
            final long time = System.currentTimeMillis();

            //Only remove after add timer
            final long addTime = data.getLong(ASIndirectEffects.NBT_RADS_ADD);
            if (time - addTime > ConfigRadiation.RAD_REMOVE_DELAY)
            {
                //Only remove after remove timer
                final long removeTime = data.getLong(ASIndirectEffects.NBT_RADS_REMOVE);
                if (time - removeTime > ConfigRadiation.RAD_REMOVE_TIMER)
                {
                    //Remove a percentage of radiation
                    float amountToRemove = ASIndirectEffects.getRadiation(entity) * ConfigRadiation.RAD_REMOVE_PERCENTAGE;
                    ASIndirectEffects.removeRadiation(entity, amountToRemove);

                    //Update remove timer
                    data.setLong(ASIndirectEffects.NBT_RADS_REMOVE, time);
                }
            }
        }
    }

    /**
     * Handles checking if a packet is needed to update the client
     *
     * @param player
     */
    protected void syncPlayerData(EntityPlayerMP player)
    {
        //Only sync if we have data to sync
        if (ASIndirectEffects.hasRadiationData(player))
        {
            //Get data
            NBTTagCompound data = ASIndirectEffects.getRadiationData(player, false);

            //Limit precision errors
            final float syncError = 0.001f;

            //Check exposure change
            float exposure = RadiationSystem.INSTANCE.getRemExposure(player);
            float prev_exposure = data.getFloat(ASIndirectEffects.NBT_RADS_ENVIROMENT_PREV);
            float delta_exposure = Math.abs(prev_exposure - exposure);

            //Check rad change
            float rad = data.getFloat(ASIndirectEffects.NBT_RADS);
            float prev_rad = data.getFloat(ASIndirectEffects.NBT_RADS_PREV);
            float delta_rad = Math.abs(prev_rad - rad);

            //Only sync if change has happened in data
            if (delta_rad > syncError || delta_exposure > syncError || player.ticksExisted % 20 == 0)
            {
                PacketSystem.INSTANCE.sendToPlayer(new PacketPlayerRadiation(rad, exposure), player);

                //Update previous, always do in sync to prevent slow creep of precision errors
                data.setFloat(ASIndirectEffects.NBT_RADS_PREV, rad);
                data.setFloat(ASIndirectEffects.NBT_RADS_ENVIROMENT_PREV, exposure);
            }
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event)
    {
        if (!event.entity.worldObj.isRemote)
        {
            //Clear data client side for respawn
            if (event.entity instanceof EntityPlayerMP)
            {
                PacketSystem.INSTANCE.sendToPlayer(new PacketPlayerRadiation(0, 0), (EntityPlayerMP) event.entity);
            }
        }
    }
}
