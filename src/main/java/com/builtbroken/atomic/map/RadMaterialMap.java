package com.builtbroken.atomic.map;

import com.builtbroken.atomic.api.radiation.IRadioactiveMaterialSystem;

/**
 * Handles storing information about amount of radiation active material per area
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/6/2018.
 */
public class RadMaterialMap extends MapSystem implements IRadioactiveMaterialSystem
{
    //TODO estimate rad per chunk
    //TODO render radiation in world (if configs enable)

    public RadMaterialMap()
    {
        super(MapHandler.RAD_MATERIAL_MAP_ID, MapHandler.NBT_RAD_CHUNK);
    }
}
