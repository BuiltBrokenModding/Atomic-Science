package com.builtbroken.atomic.api.map;

import javax.annotation.Nullable;

/**
 * Single data point in the data map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public interface IDataMapNode
{
    /**
     * Type of node
     * <p>
     * Used for referencing when pulling from global data map
     *
     * @return node type
     */
    DataMapType getType();

    /**
     * Gets the source of the node.
     * <p>
     * Not all nodes have a source. Especially when
     * the node itself is the source. Such as the case
     * of radioactive material in the map.
     *
     * @return source
     */
    @Nullable
    IDataMapSource getSource();

    /**
     * Checks if the node is valid
     * @return
     */
    default boolean isNodeValid()
    {
        return getSource() != null && getSource().isStillValid();
    }
}
