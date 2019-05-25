package com.builtbroken.atomic.content.machines.accelerator.data;

import net.minecraft.util.EnumFacing;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/13/2019.
 */
public enum TubeSide
{
    FRONT,
    LEFT,
    RIGHT,
    BACK,
    CENTER;

    public static final TubeSide[] SIDES = new TubeSide[]{FRONT, LEFT, RIGHT, BACK};

    public TubeSide getOpposite()
    {
        switch (this)
        {
            case FRONT:
                return BACK;
            case BACK:
                return FRONT;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return CENTER;
        }
    }

    public static TubeSide byIndex(int index)
    {
        if(index >= 0 && index < values().length)
        {
            return values()[index];
        }
        return CENTER;
    }

    /**
     * Finds the facing direction of a second side based on the position of the first side and it's facing direction.
     * <p>
     * This is used to allow relative rotation of objects based on a target side of another tube.
     * Say you want to connect a tube on left of an existing tube facing east. This would place the
     * tube on the north face of the existing tube. If the tube you were placing happened to be a right
     * corner and needed to connect with its right side to the existing tube left. This would then mean
     * that the tube needs to also face east for its placement to match.
     *
     * This method works by first getting the facing direction of exiting tube's side. Then using this
     * direction it gets the facing direction using the target side.
     *
     * Ex: North facing tube, left side, target right connection
     *          left of north is west
     *          right of west is north
     *
     * @param centerFace - facing direction of the center tube
     * @param targetSide - local side of the second tube
     * @return facing direction of second tube
     */
    public EnumFacing getRotationRelative(EnumFacing centerFace, TubeSide targetSide)
    {
        final EnumFacing sideFace = this.getFacing(centerFace);
        if(targetSide == FRONT || targetSide == BACK)
        {
            return targetSide.getFacing(sideFace.getOpposite());
        }
        return targetSide.getFacing(sideFace);
    }

    /**
     * Used to get the TubeSide localized based on the facing rotation and side
     *
     * @param facing - rotation to use for localizing to TubeSide
     * @param side   - direction/side to localize
     * @return localized TubeSide based on facing
     */
    public static TubeSide getSideFacingOut(EnumFacing facing, EnumFacing side)
    {
        if (side == null)
        {
            return TubeSide.CENTER;
        }
        else if (side == facing)
        {
            return TubeSide.FRONT;
        }
        else if (side == facing.getOpposite())
        {
            return TubeSide.BACK;
        }
        else if (facing.rotateY().getOpposite() == side)
        {
            return TubeSide.LEFT;
        }
        else if (facing.rotateY() == side)
        {
            return TubeSide.RIGHT;
        }
        return TubeSide.CENTER;
    }

    /**
     * Gets the facing side that would represent
     * this localized side based on the rotation
     *
     * @param facing - rotation used for localized side
     * @return unlocalized side
     */
    public EnumFacing getFacing(EnumFacing facing)
    {
        if (facing == null)
        {
            return null;
        }
        else if (this == FRONT)
        {
            return facing;
        }
        else if (this == LEFT)
        {
            return facing.rotateY().getOpposite();
        }
        else if (this == RIGHT)
        {
            return facing.rotateY();
        }
        else if (this == BACK)
        {
            return facing.getOpposite();
        }
        return null;
    }
}
