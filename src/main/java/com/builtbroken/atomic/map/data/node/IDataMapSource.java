package com.builtbroken.atomic.map.data.node;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public interface IDataMapSource
{
    /**
     * Type of the source
     * <p>
     * Used for referencing when pulling from global data map
     *
     * @return source type
     */
    DataMapType getType();
}
