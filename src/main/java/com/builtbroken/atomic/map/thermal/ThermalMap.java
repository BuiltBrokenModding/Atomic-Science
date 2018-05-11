package com.builtbroken.atomic.map.thermal;

import com.builtbroken.atomic.api.thermal.IHeatSource;
import com.builtbroken.atomic.api.thermal.IThermalSystem;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.MapSystem;
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
}
