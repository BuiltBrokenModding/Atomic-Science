package com.builtbroken.atomic.content;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.content.effects.type.IETRadiation;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/20/2018.
 */
public class ASIndirectEffects
{
    /** NBT tag used to store rads on an entity */
    public static final String NBT_RADIATION_DATA = AtomicScience.PREFIX + "radiation_data";
    /** NBT tag used to store rads on an entity */
    public static final String NBT_RADS = "rads";
    /** NBT tag used to track prev value to see if a packet is needed */
    public static final String NBT_RADS_PREV = "prev_rads";
    /** NBT tag used to track prev environment value to see if a packet is needed */
    public static final String NBT_RADS_ENVIROMENT_PREV = "prev_env_rads";
    /** NBT tag used to store last rad add time on an entity */
    public static final String NBT_RADS_ADD = "add_time";
    /** NBT tag used to store last rad remove time on an entity */
    public static final String NBT_RADS_REMOVE = "remove_time";

    public static void register()
    {
        AtomicScienceAPI.RADIATION = new IETRadiation();
        MinecraftForge.EVENT_BUS.register(AtomicScienceAPI.RADIATION);
    }

    //TODO track entities and tiles (to produce radiation in environment)
    //TODO build a radiation environment map, save to chunk
    //TODO use tracking data to cause radiation to spawn from entities
    //TODO create function list to allow entities to control logic
    //TODO create ban list to disable running on some entities (mainly for entities that handle logic themselves)

    public static NBTTagCompound getRadiationData(Entity entity, boolean init)
    {
        if (!hasRadiationData(entity) && init)
        {
            entity.getEntityData().setTag(NBT_RADIATION_DATA, new NBTTagCompound());
        }
        return entity.getEntityData().getCompoundTag(NBT_RADIATION_DATA);
    }

    public static boolean hasRadiationData(Entity entity)
    {
        return entity.getEntityData().hasKey(NBT_RADIATION_DATA, 10);
    }
}
