package com.builtbroken.atomic.map.data;

/**
 *
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
