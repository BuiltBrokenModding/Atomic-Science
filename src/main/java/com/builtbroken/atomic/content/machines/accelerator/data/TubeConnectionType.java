package com.builtbroken.atomic.content.machines.accelerator.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.IStringSerializable;

import java.util.ArrayList;
import java.util.List;

import static com.builtbroken.atomic.content.machines.accelerator.data.TubeSideType.*;

/**
 * Handles connection between tubes.
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 11/10/2018.
 */
public enum TubeConnectionType implements IStringSerializable
{
    //Enter back, exit front
    NORMAL(EXIT, NONE, NONE, ENTER),

    //-------------------------------------
    //Enter right, exit front
    CORNER_RIGHT(EXIT, NONE, ENTER, NONE),
    //Enter left, exit front
    CORNER_LEFT(EXIT, ENTER, NONE, NONE),

    //---------------------------------------------
    //Join two inputs to a single output, with only 1 being a turn
    T_JOIN_RIGHT(EXIT, NONE, ENTER, ENTER),
    //Join two inputs to a single output, with only 1 being a turn
    T_JOIN_LEFT(EXIT, ENTER, NONE, ENTER),
    //Join two inputs to a single output, with two turns
    T_JOIN(EXIT, ENTER, ENTER, NONE),
    //Join 3 inputs to a single output
    JOIN(EXIT, ENTER, ENTER, ENTER),

    //-----------------------------------------------
    //Enters from back, splits left or right
    T_SPLIT(NONE, EXIT, EXIT, ENTER),
    //Enters from back, splits right or continues forward
    T_SPLIT_RIGHT(EXIT, NONE, EXIT, ENTER),
    //Enter from back, split left or continues forward
    T_SPLIT_LEFT(EXIT, EXIT, NONE, ENTER),
    //Enter from back, split to 3 directions
    SPLIT(EXIT, EXIT, EXIT, ENTER),

    //-----------------------------------------------
    START_CAP(false, EXIT, NONE, NONE, NONE), //Accelerator gun
    END_CAP(false, NONE, NONE, NONE, ENTER), //Accelerator exit
    INVALID(false, NONE, NONE, NONE, NONE); //Error state

    public final TubeSideType[] connections;

    public final ImmutableList<TubeSide> outputSides;
    public final ImmutableList<TubeSide> inputSides;

    public final boolean canUserPlace;

    public static TubeConnectionType[] VALID = new TubeConnectionType[]{
            NORMAL,
            CORNER_RIGHT,
            CORNER_LEFT,
            T_JOIN_RIGHT,
            T_JOIN_LEFT,
            T_JOIN,
            JOIN,
            T_SPLIT,
            T_SPLIT_RIGHT,
            T_SPLIT_LEFT,
            SPLIT,
    };

    TubeConnectionType(TubeSideType front, TubeSideType left, TubeSideType right, TubeSideType back)
    {
        this(true, front, left, right, back);
    }

    TubeConnectionType(boolean canPlace, TubeSideType front, TubeSideType left, TubeSideType right, TubeSideType back)
    {
        this.canUserPlace = canPlace;
        this.connections = new TubeSideType[]{front, left, right, back};

        //Collect sides
        final List<TubeSide> enterList = new ArrayList();
        final List<TubeSide> exitList = new ArrayList();

        for (TubeSide side : TubeSide.SIDES)
        {
            final TubeSideType type = connections[side.ordinal()];
            if (type == ENTER)
            {
                enterList.add(side);
            }
            else if (type == EXIT)
            {
                exitList.add(side);
            }
        }

        //Convert
        outputSides = ImmutableList.copyOf(exitList);
        inputSides = ImmutableList.copyOf(enterList);
    }

    public static TubeConnectionType byIndex(int i)
    {
        if (i >= 0 && i < values().length)
        {
            return values()[i];
        }
        return NORMAL;
    }

    /**
     * Gets the connection type of the side
     *
     * @param side - side of the tube
     * @return connection type, or ENTER if null/center
     */
    public TubeSideType getTypeForSide(TubeSide side)
    {
        if (side != null && side != TubeSide.CENTER)
        {
            return connections[side.ordinal()];
        }
        return NONE;
    }

    public TubeSideType front()
    {
        return connections[TubeSide.FRONT.ordinal()];
    }

    public TubeSideType left()
    {
        return connections[TubeSide.LEFT.ordinal()];
    }

    public TubeSideType right()
    {
        return connections[TubeSide.RIGHT.ordinal()];
    }

    public TubeSideType back()
    {
        return connections[TubeSide.BACK.ordinal()];
    }

    /**
     * Checks if the connection layout matches the type
     *
     * @param front - connection for side
     * @param left  - connection for side
     * @param right - connection for side
     * @param back  - connection for side
     * @return true if all sides match
     */
    public boolean connectionsMatch(TubeSideType front, TubeSideType left, TubeSideType right, TubeSideType back)
    {
        return front() == front && back() == back && left() == left && right() == right;
    }

    /**
     * Tries to match the connection layout to a tube type
     *
     * @param front - connection for side
     * @param left  - connection for side
     * @param right - connection for side
     * @param back  - connection for side
     * @return type or {@link #NORMAL} as a default
     */
    public static TubeConnectionType getTypeForLayout(TubeSideType front, TubeSideType left, TubeSideType right, TubeSideType back, boolean canPlaceOnly)
    {
        for (TubeConnectionType type : TubeConnectionType.values())
        {
            if (type.connectionsMatch(front, left, right, back) && (type.canUserPlace || !canPlaceOnly))
            {
                return type;
            }
        }
        return INVALID;
    }

    @Override
    public String getName()
    {
        return name().toLowerCase();
    }

}
