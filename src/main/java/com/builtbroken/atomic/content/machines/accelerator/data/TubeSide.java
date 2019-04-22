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

    /**
     * Finds the rotation of a second side based on the position of the first side facing.
     * <p>
     * This is used to allow relative rotation of objects based on a side. Say you have a east facing tube
     * and wanted to place a left facing tube based on your right side. This would mean the tube needs
     * to be on the south of your position. Then would need to face east in order to be
     * relative left of the attachment side.
     *
     * @param centerFace       - facing direction of the center tube
     * @param relativeRotation - relative side of the second tube
     * @return facing direction of second tube
     */
    public EnumFacing getRotationRelative(EnumFacing centerFace, TubeSide relativeRotation)
    {
        return relativeRotation.getFacing(this.getFacing(centerFace));
    }

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

    public EnumFacing getFacing(EnumFacing facing)
    {
        if(facing == null)
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
