package com.builtbroken.atomic.map.thermal;

import com.builtbroken.atomic.api.thermal.IHeatSource;
import com.builtbroken.atomic.api.thermal.IThermalSystem;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.MapSystem;
import com.builtbroken.atomic.map.data.DataChange;
import com.builtbroken.atomic.map.data.DataMap;
import com.builtbroken.atomic.map.events.MapSystemEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.World;

/**
 * Handles heat in the map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/6/2018.
 */
public class ThermalMap extends MapSystem implements IThermalSystem
{
    public ThermalMap()
    {
        super(MapHandler.THERMAL_MAP_ID, MapHandler.NBT_THERMAL_CHUNK);
    }

    /**
     * Called to output energy to the world
     * <p>
     * This works by setting the heat into the world. Which
     * will then trigger the thread to spread the heat.
     *
     * @param source    - source of the heat
     * @param heatToAdd - amount of heat energy
     */
    public void outputHeat(IHeatSource source, int heatToAdd)
    {
        //data
        World world = source.world();
        int x = source.xi();
        int y = source.yi();
        int z = source.zi();

        //Get and add heat
        int heat = getData(world, x, y, z);
        heat += heatToAdd;

        //set, which should trigger thread
        setData(world, x, y, z, heat);
    }

    /**
     * Checks how much heat should spread from one block to the next.
     * <p>
     * In theory each block should have a different spread value. As
     * heat does not transfer evenly between sources.
     * <p>
     * As well heat travels differently between different types of blocks.
     * Air blocks will use convection while solid blocks direct heat transfer.
     *
     * @param x    - block 1
     * @param y    - block 1
     * @param z    - block 1
     * @param i    - block 2
     * @param j    - block 2
     * @param k    - block 2
     * @param heat - heat to transfer (some % of total heat)
     * @return heat to actually transfer
     */
    public int getHeatSpread(int x, int y, int z, int i, int j, int k, int heat)
    {
        //TODO implement
        return heat;
    }

    /**
     * Called from the thread to update data that depends on the heat in the map.
     * <p>
     * Example: How much heat to consume each tick to boil water to steam
     *
     * @param map  - map to change
     * @param x    - location
     * @param y    - location
     * @param z    - location
     * @param heat - current heat in the block
     * @return new heat value
     */
    public int doHeatAction(DataMap map, int x, int y, int z, int heat)
    {
        return heat;
    }


    @SubscribeEvent()
    public void onChunkAdded(MapSystemEvent.AddChunk event)
    {
        if (!event.world().isRemote && event.map.mapSystem == MapHandler.THERMAL_MAP)
        {
            MapHandler.THREAD_THERMAL_ACTION.queueChunkForAddition(event.chunk);
        }
    }

    @SubscribeEvent()
    public void onChunkRemove(MapSystemEvent.RemoveChunk event)
    {
        if (!event.world().isRemote && event.map.mapSystem == MapHandler.THERMAL_MAP)
        {
            MapHandler.THREAD_THERMAL_ACTION.queueChunkForRemoval(event.chunk);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRadiationChange(MapSystemEvent.UpdateValue event)
    {
        if (!event.world().isRemote && event.map.mapSystem == MapHandler.THERMAL_MAP && event.new_value > 0)
        {
            MapHandler.THREAD_THERMAL_ACTION.queuePosition(new DataChange(event.dim(), event.x, event.y, event.z, event.prev_value, event.new_value));
        }
    }
}
