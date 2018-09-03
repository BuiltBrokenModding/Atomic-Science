package com.builtbroken.atomic.map.data.grid;

import com.builtbroken.atomic.api.thermal.IHeatSource;
import com.builtbroken.atomic.map.data.DataPos;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/2/2018.
 */
public class MapGrid
{
    public static final int MAX_PATH_RANGE = 50;

    public MapNode centerNode;
    public HashMap<DataPos, MapNode> positionToNode;

    public IHeatSource heatSource;

    public final int x;
    public final int y;
    public final int z;

    public MapGrid(IHeatSource source)
    {
        this.x = source.xi();
        this.y = source.yi();
        this.z = source.zi();
    }

    public void spreadHeat(final int startingHeat)
    {

    }

    public void clearNodes()
    {
        //TODO loop through nodes clearing data from map
        //For each node remove node from grid
    }
}
