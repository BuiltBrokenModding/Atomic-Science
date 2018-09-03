package com.builtbroken.atomic.map.data.grid;

import com.builtbroken.atomic.lib.thermal.HeatSpreadDirection;
import net.minecraft.block.Block;

import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/2/2018.
 */
public class MapNode
{
    public Block block;
    public int heat;

    Map<HeatSpreadDirection, Integer> heatSpreadMap;
    Map<HeatSpreadDirection, MapNode> nodes;
}
