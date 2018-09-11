package com.builtbroken.atomic.map.events;

import com.builtbroken.atomic.map.data.DataChunk;
import com.builtbroken.atomic.map.data.DataMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Event set fired for any change or action take on the Radiation map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2018.
 */
public abstract class MapSystemEvent extends Event
{
    public final DataMap map;

    public MapSystemEvent(DataMap map)
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
    public static class UpdateValue extends MapSystemEvent
    {
        public final BlockPos pos;
        public final int prev_value;
        public int new_value;

        public UpdateValue(DataMap map, BlockPos pos, int prev_value, int new_value)
        {
            super(map);
            this.pos = pos;
            this.prev_value = prev_value;
            this.new_value = new_value;
        }
    }

    /**
     * Called when a new chunk is added to the map.
     * <p>
     * Is fired on {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}
     */
    public static class AddChunk extends MapSystemEvent
    {
        public final DataChunk chunk;

        public AddChunk(DataMap map, DataChunk chunk)
        {
            super(map);
            this.chunk = chunk;
        }
    }

    /**
     * Called when a chunk is removed from the map.
     * <p>
     * Is fired on {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}
     */
    public static class RemoveChunk extends MapSystemEvent
    {
        public final DataChunk chunk;

        public RemoveChunk(DataMap map, DataChunk chunk)
        {
            super(map);
            this.chunk = chunk;
        }
    }
}
