package com.builtbroken.atomic.content.effects;

import com.builtbroken.atomic.config.ConfigRadiation;
import com.builtbroken.atomic.content.ASIndirectEffects;
import com.builtbroken.atomic.content.effects.source.SourceWrapperPosition;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.network.netty.PacketSystem;
import com.builtbroken.atomic.network.packet.sync.PacketPlayerRadiation;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
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
    public static DamageSource radiationDeathDamage = new DamageSource("radiation").setDamageBypassesArmor().setDamageIsAbsolute();
    //TODO use Java 8 functions or interface object to trigger effects (makes the code easier to work with)
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

            if (entity.isEntityAlive())
            {
                //Increase radiation from environment
                if (!entity.isInvisible() && (!(entity instanceof EntityPlayerMP) || !((EntityPlayerMP) entity).capabilities.isCreativeMode))
                {
                    applyExposure(entity);

                    float rad = ASIndirectEffects.getRadiation(entity);
                    //Kill entity
                    if (rad > ConfigRadiation.RADIATION_DEATH_POINT) ///TODO get per entity
                    {
                        entity.attackEntityFrom(radiationDeathDamage, 5);
                    }

                    //Apply potion effects
                    if (entity.ticksExisted % 5 == 0)
                    {
                        //TODO move to separate functions to allow injecting new effects easily and turning off effects
                        //Apply first check
                        if (rad > ConfigRadiation.RADIATION_SICKNESS_POINT) ///TODO get per entity
                        {
                            float chance = (rad - ConfigRadiation.RADIATION_SICKNESS_POINT) / (ConfigRadiation.RADIATION_DEATH_POINT - ConfigRadiation.RADIATION_SICKNESS_POINT);
                            if (chance > entity.worldObj.rand.nextFloat())
                            {
                                if (entity.getActivePotionEffect(Potion.hunger) == null || entity.getActivePotionEffect(Potion.hunger).getDuration() < 10)
                                {
                                    entity.addPotionEffect(new PotionEffect(Potion.hunger.id, 100));
                                }
                            }

                            //Apply second check
                            if (rad > ConfigRadiation.RADIATION_WEAKNESS_POINT) ///TODO get per entity
                            {
                                chance = (rad - ConfigRadiation.RADIATION_WEAKNESS_POINT) / (ConfigRadiation.RADIATION_DEATH_POINT - ConfigRadiation.RADIATION_WEAKNESS_POINT);
                                if (chance > entity.worldObj.rand.nextFloat())
                                {
                                    if (entity.getActivePotionEffect(Potion.weakness) == null || entity.getActivePotionEffect(Potion.weakness).getDuration() < 10)
                                    {
                                        entity.addPotionEffect(new PotionEffect(Potion.weakness.id, 100));
                                    }
                                }

                                if (chance > entity.worldObj.rand.nextFloat())
                                {
                                    if (entity.getActivePotionEffect(Potion.digSlowdown) == null || entity.getActivePotionEffect(Potion.digSlowdown).getDuration() < 10)
                                    {
                                        entity.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 100));
                                    }
                                }

                                if (chance > entity.worldObj.rand.nextFloat())
                                {
                                    if (entity.getActivePotionEffect(Potion.moveSlowdown) == null || entity.getActivePotionEffect(Potion.moveSlowdown).getDuration() < 10)
                                    {
                                        entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100));
                                    }
                                }

                                //Apply third check
                                if (rad > ConfigRadiation.RADIATION_CONFUSION_POINT) ///TODO get per entity
                                {
                                    chance = (rad - ConfigRadiation.RADIATION_CONFUSION_POINT) / (ConfigRadiation.RADIATION_DEATH_POINT - ConfigRadiation.RADIATION_CONFUSION_POINT);
                                    if (chance > entity.worldObj.rand.nextFloat())
                                    {
                                        if (entity.getActivePotionEffect(Potion.confusion) == null || entity.getActivePotionEffect(Potion.confusion).getDuration() < 10)
                                        {
                                            entity.addPotionEffect(new PotionEffect(Potion.confusion.id, 100));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //Sync data to client if changes
                if (entity instanceof EntityPlayerMP)
                {
                    syncPlayerData((EntityPlayerMP) entity);
                }
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
        float remExposure = MapHandler.RADIATION_MAP.getRemExposure(entity);
        if (remExposure > 0)
        {
            SourceWrapperPosition sourceWrapperPosition = new SourceWrapperPosition(
                    entity.worldObj,
                    entity.posX,
                    entity.posY,
                    entity.posZ
            );
            ASIndirectEffects.applyIndirectEffect(entity, sourceWrapperPosition, remExposure);
        }
        else if (ASIndirectEffects.hasRadiationData(entity))
        {
            //Get data
            final NBTTagCompound data = ASIndirectEffects.getRadiationData(entity, false);

            //Only remove after add timer
            int removeTimer = data.getInteger(ASIndirectEffects.NBT_RADS_REMOVE_TIMER);
            if (removeTimer > ConfigRadiation.RAD_REMOVE_TIMER)
            {
                float rad = ASIndirectEffects.getRadiation(entity);
                if (rad <= ConfigRadiation.RAD_REMOVE_LOWER_LIMIT)
                {
                    ASIndirectEffects.setRadiation(entity, 0);
                }
                else
                {
                    //Remove a percentage of radiation
                    float amountToRemove = rad * ConfigRadiation.RAD_REMOVE_PERCENTAGE;
                    ASIndirectEffects.removeRadiation(entity, amountToRemove);
                }

                //Update remove timer
                data.setInteger(ASIndirectEffects.NBT_RADS_REMOVE_TIMER, 0);
            }
            else
            {
                //Update remove timer
                data.setInteger(ASIndirectEffects.NBT_RADS_REMOVE_TIMER, removeTimer + 1);
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
        boolean sendPacket = false;
        //Only sync if we have data to sync
        if (ASIndirectEffects.hasRadiationData(player))
        {
            //Get data
            NBTTagCompound data = ASIndirectEffects.getRadiationData(player, false);

            //Limit precision errors
            final float syncError = 0.001f;

            //Check exposure change
            float exposure = MapHandler.RADIATION_MAP.getRemExposure(player);
            float prev_exposure = data.getFloat(ASIndirectEffects.NBT_RADS_ENVIROMENT_PREV);
            float delta_exposure = Math.abs(prev_exposure - exposure);

            //Check rad change
            float rad = data.getFloat(ASIndirectEffects.NBT_RADS);
            float prev_rad = data.getFloat(ASIndirectEffects.NBT_RADS_PREV);
            float delta_rad = Math.abs(prev_rad - rad);

            //Only sync if change has happened in data
            if (delta_rad > syncError || delta_exposure > syncError || player.ticksExisted % 20 == 0)
            {
                sendPacket = true;
                //Update previous, always do in sync to prevent slow creep of precision errors
                data.setFloat(ASIndirectEffects.NBT_RADS_PREV, rad);
                data.setFloat(ASIndirectEffects.NBT_RADS_ENVIROMENT_PREV, exposure);
            }
        }

        //Update client
        if (sendPacket || player.ticksExisted % 5 == 0)
        {
            sendPacket(player);
        }
    }

    public static void sendPacket(EntityPlayerMP player)
    {
        NBTTagCompound data = ASIndirectEffects.getRadiationData(player, false);
        PacketSystem.INSTANCE.sendToPlayer(new PacketPlayerRadiation(
                data.getFloat(ASIndirectEffects.NBT_RADS),
                MapHandler.RADIATION_MAP.getRemExposure(player),
                data.getInteger(ASIndirectEffects.NBT_RADS_REMOVE_TIMER)), player);
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event)
    {
        if (!event.entity.worldObj.isRemote)
        {
            //Clear data client side for respawn
            if (event.entity instanceof EntityPlayerMP)
            {
                float remExposure = MapHandler.RADIATION_MAP.getRemExposure(event.entity);
                System.out.println(remExposure);
                PacketSystem.INSTANCE.sendToPlayer(new PacketPlayerRadiation(0, 0, 0), (EntityPlayerMP) event.entity);
            }
        }
    }
}
