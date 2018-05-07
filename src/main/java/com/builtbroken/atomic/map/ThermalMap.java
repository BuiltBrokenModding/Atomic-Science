package com.builtbroken.atomic.map;

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
}
