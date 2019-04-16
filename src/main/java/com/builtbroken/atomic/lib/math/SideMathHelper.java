package com.builtbroken.atomic.lib.math;

import net.minecraft.util.EnumFacing;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/13/2019.
 */
public class SideMathHelper
{

    /**
     * Gets the edge as a float
     *
     * @param facing - side
     * @return value 0.0 or 1.0 for the side, returns 0.5 for null
     */
    public static float getEdge(EnumFacing facing)
    {
        switch (facing)
        {
            case UP:
                return MathConstF.EDGE_UP;
            case DOWN:
                return MathConstF.EDGE_DOWN;
            case NORTH:
                return MathConstF.EDGE_NORTH;
            case SOUTH:
                return MathConstF.EDGE_SOUTH;
            case EAST:
                return MathConstF.EDGE_EAST;
            case WEST:
                return MathConstF.EDGE_WEST;
            default:
                return MathConstF.CENTER;
        }
    }

    /**
     * Gets the value to use to center a position on the edge.
     * <p>
     * If the facing is the edge then it returns 0.0 or 1.0. Other
     * wise it will return 0.5
     *
     * @param facing - side
     * @return 0.0 or 1.0 if edge, 0.5 otherwise
     */
    public static float getEdgeOrCenterX(EnumFacing facing)
    {
        switch (facing)
        {
            case EAST:
                return MathConstF.EDGE_EAST;
            case WEST:
                return MathConstF.EDGE_WEST;
            default:
                return MathConstF.CENTER;
        }
    }

    /**
     * Gets the value to use to center a position on the edge.
     * <p>
     * If the facing is the edge then it returns 0.0 or 1.0. Other
     * wise it will return 0.5
     *
     * @param facing - side
     * @return 0.0 or 1.0 if edge, 0.5 otherwise
     */
    public static float getEdgeOrCenterZ(EnumFacing facing)
    {
        switch (facing)
        {
            case NORTH:
                return MathConstF.EDGE_NORTH;
            case SOUTH:
                return MathConstF.EDGE_SOUTH;
            default:
                return MathConstF.CENTER;
        }
    }

    /**
     * Gets the value to use to center a position on the edge.
     * <p>
     * If the facing is the edge then it returns 0.0 or 1.0. Other
     * wise it will return 0.5
     *
     * @param facing - side
     * @return 0.0 or 1.0 if edge, 0.5 otherwise
     */
    public static float getEdgeOrCenterY(EnumFacing facing)
    {
        switch (facing)
        {
            case UP:
                return MathConstF.EDGE_UP;
            case DOWN:
                return MathConstF.EDGE_DOWN;
            default:
                return MathConstF.CENTER;
        }
    }

    /**
     * Remaining distance to side of the block
     *
     * @param deltaX    - delta from center on X axis (-0.5 to 0.5)
     * @param deltaZ    - delta from center on Z axis (-0.5 to 0.5)
     * @param direction - side
     * @return distance left to the side between 0.0 and 1.0
     */
    public static float remainingDistanceToSide(float deltaX, float deltaZ, EnumFacing direction)
    {
        switch (direction)
        {
            //-z
            case NORTH:
                return Math.max(0, MathConstF.CENTER + deltaZ);
            //+X
            case EAST:
                return Math.max(0, MathConstF.CENTER - deltaX);
            //+Z
            case SOUTH:
                return Math.max(0, MathConstF.CENTER - deltaZ);
            //-X
            case WEST:
                return Math.max(0, MathConstF.CENTER + deltaX);
        }
        return 0;
    }

    /**
     * Gets the absolute distance left to the center of the block from the side
     *
     * @param deltaX    - delta from center on X axis (-0.5 to 0.5)
     * @param deltaZ    - delta from center on X axis (-0.5 to 0.5)
     * @param direction - side to check, switches between delta inputs
     * @return distance left to center
     */
    public static float remainingDistanceCenter(float deltaX, float deltaZ, EnumFacing direction)
    {
        float delta;
        switch (direction)
        {
            //z
            case NORTH:
            case SOUTH:
                delta = deltaZ;
                break;
            //x
            case EAST:
            case WEST:
                delta = deltaX;
                break;
            default:
                delta = 0;
        }
        return Math.abs(delta);
    }

    /**
     * Checks if an axis is close enough to zero to be considered zero
     *
     * @param delta -0.5 to 0.5
     * @return true if near zero
     */
    public static boolean isZero(float delta)
    {
        return delta <= MathConstF.ZERO_CUT && delta >= -MathConstF.ZERO_CUT;
    }

    /**
     * Checks if an axis is close enough to zero to be considered zero
     *
     * @param deltaX - delta from center on X axis (-0.5 to 0.5)
     * @param deltaY - delta from center on Y axis (-0.5 to 0.5)
     * @param deltaZ - delta from center on Z axis (-0.5 to 0.5)
     * @return true if near zero
     */
    public static boolean isZero(float deltaX, float deltaY, float deltaZ)
    {
        return isZero(deltaX) && isZero(deltaY) && isZero(deltaZ);
    }

    /**
     * Gets the containing side based on distance from center of a block.
     * <p>
     * Only works if only 1 side is not zero aligned. If 2 or more are not zero aligned
     * return null. As it can't tell which of the 6 sides contains it the most without
     * guessing.
     *
     * @param deltaX - delta from center on X axis (-0.5 to 0.5)
     * @param deltaY - delta from center on Y axis (-0.5 to 0.5)
     * @param deltaZ - delta from center on Z axis (-0.5 to 0.5)
     * @return side that contains the position
     */
    public static EnumFacing containingSide(float deltaX, float deltaY, float deltaZ)
    {
        //Check if we are near zero
        final boolean zeroX = isZero(deltaX);
        final boolean zeroY = isZero(deltaY);
        final boolean zeroZ = isZero(deltaZ);

        //If all not zero then we are invalid
        if (!zeroX && !zeroY && !zeroZ)
        {
            return null;
        }
        else if (!zeroZ)
        {
            return deltaZ > 0 ? EnumFacing.SOUTH : EnumFacing.NORTH;
        }
        else if (!zeroX)
        {
            return deltaX > 0 ? EnumFacing.EAST : EnumFacing.WEST;
        }
        else if (!zeroY)
        {
            return deltaY > 0 ? EnumFacing.UP : EnumFacing.DOWN;
        }
        return null;
    }
}
