package com.builtbroken.atomic.lib.thermal;

public enum HeatSpreadDirection
{
    /** -Y */
    DOWN(0, -1, 0, .16f),

    /** +Y */
    UP(0, 1, 0, .16f),

    /** -Z */
    NORTH(0, 0, -1, .16f),

    /** +Z */
    SOUTH(0, 0, 1, .16f),

    /** -X */
    WEST(-1, 0, 0, .16f),

    /** +X */
    EAST(1, 0, 0, .16f),

    NE(1, 0, -1, 0.02f),
    NW(-1, 0, -1, 0.02f),
    SE(1, 0, 1, 0.02f),
    SW(-1, 0, 1, 0.02f),

    UNE(1, 1, -1, 0.02f),
    UNW(-1, 1, -1, 0.02f),
    USE(1, 1, 1, 0.02f),
    USW(-1, 1, 1, 0.02f),

    DNE(1, -1, -1, 0.02f),
    DNW(-1, -1, -1, 0.02f),
    DSE(1, -1, 1, 0.02f),
    DSW(-1, -1, 1, 0.02f);

    public final int offsetX;
    public final int offsetY;
    public final int offsetZ;
    public final float percentage;

    HeatSpreadDirection(int x, int y, int z, float percentage)
    {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        this.percentage = percentage;
    }
}