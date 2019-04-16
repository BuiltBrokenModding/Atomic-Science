package com.builtbroken.test.as.accelerator;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-16.
 */
public class TestPMoveSplitTRight extends PMoveCommon
{
    public static int RIGHT = 1;
    public static int FRONT = 0;

    @Test
    public void northEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.NORTH, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.NORTH);
    }

    @Test
    public void eastEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.EAST, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.EAST);
    }

    @Test
    public void southEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.SOUTH);
    }

    @Test
    public void westEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.WEST, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.WEST);
    }

    @Test
    public void northExitRight()
    {
        checkExit(EnumFacing.NORTH, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.EAST);
    }

    @Test
    public void northExitFront()
    {
        checkExit(EnumFacing.NORTH, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.NORTH);
    }

    @Test
    public void eastExitRight()
    {
        checkExit(EnumFacing.EAST, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.SOUTH);
    }

    @Test
    public void eastExitFront()
    {
        checkExit(EnumFacing.EAST, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.EAST);
    }

    @Test
    public void southExitRight()
    {
        checkExit(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.WEST);
    }

    @Test
    public void southExitFront()
    {
        checkExit(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.SOUTH);
    }

    @Test
    public void westExitRight()
    {
        checkExit(EnumFacing.WEST, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.NORTH);
    }

    @Test
    public void westExitFront()
    {
        checkExit(EnumFacing.WEST, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.WEST);
    }

    @Test
    public void northFullSplitRight()
    {
        checkTurn(EnumFacing.NORTH, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.SOUTH, RIGHT, EnumFacing.EAST);
    }

    @Test
    public void northFullSplitFront()
    {
        checkTurn(EnumFacing.NORTH, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.SOUTH, FRONT, EnumFacing.NORTH);
    }

    @Test
    public void eastFullSplitRight()
    {
        checkTurn(EnumFacing.EAST, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.WEST, RIGHT, EnumFacing.SOUTH);
    }

    @Test
    public void eastFullSplitFront()
    {
        checkTurn(EnumFacing.EAST, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.WEST, FRONT, EnumFacing.EAST);
    }

    @Test
    public void southFullSplitRight()
    {
        checkTurn(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.NORTH, RIGHT, EnumFacing.WEST);
    }

    @Test
    public void southFullSplitFront()
    {
        checkTurn(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.NORTH, FRONT, EnumFacing.SOUTH);
    }

    @Test
    public void westFullSplitRight()
    {
        checkTurn(EnumFacing.WEST, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.EAST, RIGHT, EnumFacing.NORTH);
    }

    @Test
    public void westFullSplitFront()
    {
        checkTurn(EnumFacing.WEST, TubeConnectionType.T_SPLIT_RIGHT, EnumFacing.EAST, FRONT, EnumFacing.WEST);
    }
}

