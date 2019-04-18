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
        if (this == FRONT)
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
