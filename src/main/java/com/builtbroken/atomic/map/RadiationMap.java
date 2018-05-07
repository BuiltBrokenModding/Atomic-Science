package com.builtbroken.atomic.map;

import com.builtbroken.atomic.map.data.DataMap;
import com.builtbroken.atomic.map.events.RadiationMapEvent;
import com.builtbroken.atomic.map.thread.RadChange;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * Handles radiation exposure map. Is not saved and only cached to improve runtime.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2018.
 */
public class RadiationMap extends MapSystem
{
    public RadiationMap()
    {
        super(null); //Doesn't save
    }

    ///----------------------------------------------------------------
    ///-------- Level Data Accessors
    ///----------------------------------------------------------------

    /**
     * Gets the '(REM) roentgen equivalent man' at the given location
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return rem level in mili-rads (1/1000ths of a rem)
     */
    public int getRadLevel(World world, int x, int y, int z)
    {
        DataMap map = getMap(world, false);
        if (map != null)
        {
            return map.getData(x, y, z);
        }
        return 0;
    }

    /**
     * Gets the REM exposure for the entity
     *
     * @param entity - entity, will use the entity size to get an average value
     * @return REM value
     */
    public float getRemExposure(Entity entity)
    {
        float value = 0;

        //Top point
        value += getRadLevel(entity.worldObj, (int) Math.floor(entity.posX), (int) Math.floor(entity.posY + entity.height), (int) Math.floor(entity.posZ));

        //Mid point
        value += getRadLevel(entity.worldObj, (int) Math.floor(entity.posX), (int) Math.floor(entity.posY + (entity.height / 2)), (int) Math.floor(entity.posZ));

        //Bottom point
        value += getRadLevel(entity.worldObj, (int) Math.floor(entity.posX), (int) Math.floor(entity.posY), (int) Math.floor(entity.posZ));

        //Average TODO build alg to use body size (collision box)
        value /= 3;

        //Convert from mili rem to rem
        value /= 1000;

        return value;
    }

    ///----------------------------------------------------------------
    ///--------Edit events
    ///----------------------------------------------------------------

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRadiationChange(RadiationMapEvent.UpdateRadiationMaterial event)
    {
        if (event.prev_value != event.new_value)
        {
            MapHandler.THREAD_RAD_EXPOSURE.changeQueue.add(new RadChange(event.dim(), event.x, event.y, event.z, event.prev_value, event.new_value));
        }
    }
}
