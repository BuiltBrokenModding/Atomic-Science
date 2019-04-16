package com.builtbroken.test.as.accelerator;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-16.
 */
public class TestPMoveSplit extends PMoveCommon
{
    public static int FRONT = 0;
    public static int LEFT = 1;
    public static int RIGHT = 2;

    @Test
    public void northEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.NORTH, TubeConnectionType.SPLIT, EnumFacing.NORTH);
    }

    @Test
    public void eastEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.EAST, TubeConnectionType.SPLIT, EnumFacing.EAST);
    }

    @Test
    public void southEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.SOUTH, TubeConnectionType.SPLIT, EnumFacing.SOUTH);
    }

    @Test
    public void westEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.WEST, TubeConnectionType.SPLIT, EnumFacing.WEST);
    }

    @Test
    public void northExitLeft()
    {
        checkExit(EnumFacing.NORTH, TubeConnectionType.SPLIT, EnumFacing.WEST);
    }

    @Test
    public void northExitRight()
    {
        checkExit(EnumFacing.NORTH, TubeConnectionType.SPLIT, EnumFacing.EAST);
    }

    @Test
    public void eastExitLeft()
    {
        checkExit(EnumFacing.EAST, TubeConnectionType.SPLIT, EnumFacing.NORTH);
    }

    @Test
    public void eastExitRight()
    {
        checkExit(EnumFacing.EAST, TubeConnectionType.SPLIT, EnumFacing.SOUTH);
    }

    @Test
    public void southExitLeft()
    {
        checkExit(EnumFacing.SOUTH, TubeConnectionType.SPLIT, EnumFacing.EAST);
    }

    @Test
    public void southExitRight()
    {
        checkExit(EnumFacing.SOUTH, TubeConnectionType.SPLIT, EnumFacing.WEST);
    }

    @Test
    public void westExitLeft()
    {
        checkExit(EnumFacing.WEST, TubeConnectionType.SPLIT, EnumFacing.SOUTH);
    }

    @Test
    public void westExitRight()
    {
        checkExit(EnumFacing.WEST, TubeConnectionType.SPLIT, EnumFacing.NORTH);
    }

    @Test
    public void northExitFront()
    {
        checkExit(EnumFacing.NORTH, TubeConnectionType.SPLIT, EnumFacing.NORTH);
    }

    @Test
    public void eastExitFront()
    {
        checkExit(EnumFacing.EAST, TubeConnectionType.SPLIT, EnumFacing.EAST);
    }

    @Test
    public void southExitFront()
    {
        checkExit(EnumFacing.SOUTH, TubeConnectionType.SPLIT, EnumFacing.SOUTH);
    }

    @Test
    public void westExitFront()
    {
        checkExit(EnumFacing.WEST, TubeConnectionType.SPLIT, EnumFacing.WEST);
    }

    @Test
    public void northFullSplitLeft()
    {
        checkTurn(EnumFacing.NORTH, TubeConnectionType.SPLIT, EnumFacing.SOUTH, LEFT, EnumFacing.WEST);
    }

    @Test
    public void northFullSplitRight()
    {
        checkTurn(EnumFacing.NORTH, TubeConnectionType.SPLIT, EnumFacing.SOUTH, RIGHT, EnumFacing.EAST);
    }

    @Test
    public void eastFullSplitLeft()
    {
        checkTurn(EnumFacing.EAST, TubeConnectionType.SPLIT, EnumFacing.WEST, LEFT, EnumFacing.NORTH);
    }

    @Test
    public void eastFullSplitRight()
    {
        checkTurn(EnumFacing.EAST, TubeConnectionType.SPLIT, EnumFacing.WEST, RIGHT, EnumFacing.SOUTH);
    }

    @Test
    public void southFullSplitLeft()
    {
        checkTurn(EnumFacing.SOUTH, TubeConnectionType.SPLIT, EnumFacing.NORTH, LEFT, EnumFacing.EAST);
    }

    @Test
    public void southFullSplitRight()
    {
        checkTurn(EnumFacing.SOUTH, TubeConnectionType.SPLIT, EnumFacing.NORTH, RIGHT, EnumFacing.WEST);
    }

    @Test
    public void westFullSplitLeft()
    {
        checkTurn(EnumFacing.WEST, TubeConnectionType.SPLIT, EnumFacing.EAST, LEFT, EnumFacing.SOUTH);
    }

    @Test
    public void westFullSplitRight()
    {
        checkTurn(EnumFacing.WEST, TubeConnectionType.SPLIT, EnumFacing.EAST, RIGHT, EnumFacing.NORTH);
    }

    @Test
    public void northFullSplitFront()
    {
        checkTurn(EnumFacing.NORTH, TubeConnectionType.SPLIT, EnumFacing.SOUTH, FRONT, EnumFacing.NORTH);
    }

    @Test
    public void eastFullSplitFront()
    {
        checkTurn(EnumFacing.EAST, TubeConnectionType.SPLIT, EnumFacing.WEST, FRONT, EnumFacing.EAST);
    }

    @Test
    public void southFullSplitFront()
    {
        checkTurn(EnumFacing.SOUTH, TubeConnectionType.SPLIT, EnumFacing.NORTH, FRONT, EnumFacing.SOUTH);
    }
    
    @Test
    public void westFullSplitFront()
    {
        checkTurn(EnumFacing.WEST, TubeConnectionType.SPLIT, EnumFacing.EAST, FRONT, EnumFacing.WEST);
    }
}

