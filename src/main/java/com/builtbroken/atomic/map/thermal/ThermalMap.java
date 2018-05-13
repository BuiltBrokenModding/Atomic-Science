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

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Handles heat in the map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/6/2018.
 */
public class ThermalMap extends MapSystem implements IThermalSystem
{
    /** Queue of data to set from the thread */
    public ConcurrentLinkedQueue<DataChange> setDataQueue = new ConcurrentLinkedQueue();

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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHeatChanged(MapSystemEvent.UpdateValue event)
    {
        World world = event.world();
        DataMap map = getMap(world, false);
        if (world != null && map != null)
        {
            if (map.blockExists(event.x, event.y, event.z) && ThermalHandler.canChangeStates(world, event.x, event.y, event.z))
            {
                long joules = event.new_value * 1000;
                if (joules > ThermalHandler.energyCostToChangeStates(world, event.x, event.y, event.z))
                {
                    ThermalHandler.changeStates(world, event.x, event.y, event.z);
                }
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            long time = System.currentTimeMillis();
            while (!setDataQueue.isEmpty() && System.currentTimeMillis() - time < 10)
            {
                DataChange dataChange = setDataQueue.poll();
                if (dataChange != null)
                {
                    setData(dataChange.dim, dataChange.xi(), dataChange.yi(), dataChange.zi(), dataChange.new_value);
                }
                dataChange.dispose();
            }
        }
    }
}
