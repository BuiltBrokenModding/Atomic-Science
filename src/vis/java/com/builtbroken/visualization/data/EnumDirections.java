package com.builtbroken.visualization.data;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/26/2018.
 */
public enum EnumDirections
{
    NORTH(0, -1),
    SOUTH(0, 1),
    WEST(-1, 0),
    EAST(1, 0);

    public final int xDelta;
    public final int yDelta;

    EnumDirections(int xDelta, int yDelta)
    {
        this.xDelta = xDelta;
        this.yDelta = yDelta;
    }
}
