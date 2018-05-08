package com.builtbroken.atomic.map;

import com.builtbroken.atomic.api.thermal.IHeatSource;
import net.minecraft.world.World;

/**
 * Handles heat in the map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/6/2018.
 */
public class ThermalMap extends MapSystem
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
     * @param source - source of the heat
     * @param heat   - amount of heat energy
     */
    public void outputHeat(IHeatSource source, int heat)
    {
        //data
        World world = source.world();
        int x = source.xi();
        int y = source.yi();
        int z = source.zi();

        //Get and add heat
        int prevHeat = getData(world, x, y, z);
        prevHeat += heat;

        //set, which should trigger thread
        setData(world, x, y, z, prevHeat);
    }
}
