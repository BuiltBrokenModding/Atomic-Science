package com.builtbroken.atomic.map.data;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
@FunctionalInterface
public interface MapValueConsumer
{

    /**
     * Performs this operation on the given argument.
     *
     * @param value the input argument
     */
    void accept(int dim, int x, int y, int z, int value);
}
