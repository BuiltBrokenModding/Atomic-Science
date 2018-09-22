package com.builtbroken.atomic.api.map;

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
    void clearMapData();

    /**
     * Called to remove stored map data from the
     * {@link com.builtbroken.atomic.map.MapHandler#GLOBAL_DATA_MAP} without
     * de-validating the data. As the data may be recycled to reduce
     * memory churn.
     */
    void disconnectMapData();

    /**
     * Called to connect all stored data into
     * {@link com.builtbroken.atomic.map.MapHandler#GLOBAL_DATA_MAP}
     */
    void connectMapData();

    /**
     * Called after new data has been added to the source
     * and old data has been cleared. Allows the source
     * to do some quick checks and trigger additional logic.
     */
    default void initMapData()
    {

    }

    /**
     * Checks if the source is still
     * valid. This is used during clean up
     * checks of the map to remove sources
     * that can not remove themselves.
     * <p>
     * Example: Map sources that are not
     * stored in the map data. Which are
     * used to act as fake source for map
     * radiation.
     *
     * @return true if still valid
     */
    default boolean isStillValid()
    {
        return true;
    }
}
