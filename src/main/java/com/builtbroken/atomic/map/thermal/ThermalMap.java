package com.builtbroken.atomic.map.thermal;

import com.builtbroken.atomic.api.thermal.IHeatSource;
import com.builtbroken.atomic.api.thermal.IThermalSystem;
import com.builtbroken.atomic.lib.MassHandler;
import com.builtbroken.atomic.lib.thermal.ThermalHandler;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.MapSystem;
import com.builtbroken.atomic.map.data.DataChange;
import com.builtbroken.atomic.map.data.DataMap;
import com.builtbroken.atomic.map.events.MapSystemEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Handles heat in the map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/6/2018.
 */
public class ThermalMap extends MapSystem implements IThermalSystem
{
    //Temp storage of data to dump into the thread at the end of the tick. Used to slow down updates to prevent heat moving too far and fast.
    private Queue<DataChange> tickQueueHopper = new LinkedList();

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
     * @param x    - block 1, source of heat
     * @param y    - block 1, source of heat
     * @param z    - block 1, source of heat
     * @param i    - block 2, receiver of heat
     * @param j    - block 2, receiver of heat
     * @param k    - block 2, receiver of heat
     * @param heat - heat to transfer (some % of total heat), in kilo-joules
     * @return heat to actually transfer, in kilo-joules
     */
    public int getHeatSpread(World world, int x, int y, int z, int i, int j, int k, int heat)
    {
        double deltaTemp = getTemperatureDelta(world, x, y, z, i, j, k);
        double specificHeat = ThermalHandler.getSpecificHeat(world, i, j, k);
        double mass = MassHandler.getMass(world, i, j, k);

        int maxHeat = (int) (deltaTemp * specificHeat * mass / 1000.0); //Map stores heat in KJ but equation is in joules

        //TODO implement
        return Math.min(maxHeat, heat);
    }

    /**
     * Energy in joules
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public double getJoules(World world, int x, int y, int z)
    {
        return getData(world, x, y, z) * 1000; //Map stores heat in kilo-joules
    }

    /**
     * Gets the temperature of the block at the location
     * <p>
     * Uses the properties of the block to calculate the value from
     * the heat + environmental values.
     *
     * @param world - map to pull data from
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return temperature in Kelvin
     */
    public double getTemperature(World world, int x, int y, int z)
    {
        return getJoules(world, x, y, z) / (MassHandler.getMass(world, x, y, z) * ThermalHandler.getSpecificHeat(world, x, y, z));
    }

    /**
     * Gets the different in temperature between two locations
     *
     * @param world - map to pull data from
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @param i     - location 2
     * @param j     - location 2
     * @param k     - location 2
     * @return temperature in Kelvin
     */
    public double getTemperatureDelta(World world, int x, int y, int z, int i, int j, int k)
    {
        return getTemperature(world, x, y, z) - getTemperature(world, i, j, k);
    }

    /**
     * Gets the amount of heat energy naturally present in the block due to environmental values
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return energy in joules
     */
    public double getEnvironmentalJoules(World world, int x, int y, int z)
    {
        return getEnvironmentalTemperature(world, x, y, z) * MassHandler.getMass(world, x, y, z) * ThermalHandler.getSpecificHeat(world, x, y, z);
    }

    /**
     * Gets the natural resting temperature of the environment
     *
     * @param world - location
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @return temperature in kelvin
     */
    public double getEnvironmentalTemperature(World world, int x, int y, int z)
    {
        return 290; //Slightly under room temp
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


    @SubscribeEvent
    public void onChunkAdded(MapSystemEvent.AddChunk event)
    {
        if (!event.world().isRemote && event.map.mapSystem == MapHandler.THERMAL_MAP)
        {
            MapHandler.THREAD_THERMAL_ACTION.queueChunkForAddition(event.chunk);
        }
    }

    @SubscribeEvent
    public void onChunkRemove(MapSystemEvent.RemoveChunk event)
    {
        if (!event.world().isRemote && event.map.mapSystem == MapHandler.THERMAL_MAP)
        {
            MapHandler.THREAD_THERMAL_ACTION.queueChunkForRemoval(event.chunk);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHeatChanged(MapSystemEvent.UpdateValue event)
    {
        if (!event.world().isRemote && event.map.mapSystem == MapHandler.THERMAL_MAP && event.new_value > 0)
        {
            tickQueueHopper.add(new DataChange(event.dim(), event.x, event.y, event.z, event.prev_value, event.new_value));
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            //Dump queue into thread
            while (!tickQueueHopper.isEmpty())
            {
                MapHandler.THREAD_THERMAL_ACTION.queuePosition(tickQueueHopper.poll());
            }
        }
    }
}
