package com.builtbroken.atomic.map.data.node;

import com.builtbroken.atomic.lib.transform.IPosWorld;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public interface IDataMapSource extends IPosWorld
{
    /**
     * Type of the source
     * <p>
     * Used for referencing when pulling from global data map
     *
     * @return source type
     */
    DataMapType getType();

    /**
     * Called to clear any data attached to the source
     * that belongs to the map. Normally called on
     * world unload or source death.
     */
    void ClearMapData();

    default boolean isStillValid()
    {
        return true;
    }
}
