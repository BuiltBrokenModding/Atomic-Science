package com.builtbroken.atomic.map.events;

import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.map.IDataMapNode;
import com.builtbroken.atomic.map.data.storage.DataChunk;
import com.builtbroken.atomic.map.data.storage.DataMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Event set fired for any change or action take on the Radiation map
 *
 *
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
    public static class OnValueChanged extends MapSystemEvent
    {
        /** Type of data */
        public final DataMapType type;

        //Location data
        public final int x;
        public final int y;
        public final int z;

        /** Previous value of all data of same type added together */
        public final int prev_value;


        private BlockPos pos;

        public OnValueChanged(DataMap map, DataMapType type, int x, int y, int z, int prev_value)
        {
            super(map);
            this.type = type;
            this.x = x;
            this.y = y;
            this.z = z;
            this.prev_value = prev_value;
        }

        public int getNewValue()
        {
            return prev_value;
        }

        public BlockPos getPos()
        {
            if (pos == null)
            {
                pos = new BlockPos(x, y, z);
            }
            return pos;
        }
    }

    /**
     * Called when a node is added to the map changing the value
     * <p>
     * Is Cancelable to prevent the value from changing
     * <p>
     * Can change value in event to effect results
     * <p>
     * Is fired on {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}
     */
    @Cancelable
    public static class OnNodeAdded extends OnValueChanged
    {
        /** Node being added, add value to prev_value to get new value */
        public IDataMapNode node;

        public OnNodeAdded(DataMap map, DataMapType type, int x, int y, int z, int prev_value, IDataMapNode node)
        {
            super(map, type, x, y, z, prev_value);
            this.node = node;
        }

        public int getNewValue()
        {
            if (node == null)
            {
                return prev_value;
            }
            return prev_value + type.getValue(node);
        }
    }

    /**
     * Called when a node(s) are removed from the map changing the value
     * <p>
     * Is Cancelable to prevent the value from changing
     * <p>
     * Can change value in event to effect results
     * <p>
     * Is fired on {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}
     */
    public static class OnNodeRemoved extends OnValueChanged
    {
        public final int new_value;

        public OnNodeRemoved(DataMap map, DataMapType type, int x, int y, int z, int prev_value, int new_value)
        {
            super(map, type, x, y, z, prev_value);
            this.new_value = new_value;
        }

        @Override
        public int getNewValue()
        {
            return new_value;
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
