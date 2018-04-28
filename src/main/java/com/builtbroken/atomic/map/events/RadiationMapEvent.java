package com.builtbroken.atomic.map.events;

import com.builtbroken.atomic.map.RadiationMap;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * Event set fired for any change or action take on the Radiation map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2018.
 */
public abstract class RadiationMapEvent extends Event
{
    public final RadiationMap map;

    public RadiationMapEvent(RadiationMap map)
    {
        this.map = map;
    }

    public int dim()
    {
        return map.dim;
    }

    public World world()
    {
        return DimensionManager.getWorld(map.dim);
    }

    /**
     * Called when a value is changed in the map
     * <p>
     * Is Cancelable to prevent the value from changing
     * <p>
     * Can change value in event to effect results
     * <p>
     * Is fired on {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}
     */
    @Cancelable
    public static class UpdateRadiationMaterial extends RadiationMapEvent
    {
        public final int x;
        public final int y;
        public final int z;
        public final int prev_value;
        public int new_value;

        public UpdateRadiationMaterial(RadiationMap map, int x, int y, int z, int prev_value, int new_value)
        {
            super(map);
            this.x = x;
            this.y = y;
            this.z = z;
            this.prev_value = prev_value;
            this.new_value = new_value;
        }
    }
}
