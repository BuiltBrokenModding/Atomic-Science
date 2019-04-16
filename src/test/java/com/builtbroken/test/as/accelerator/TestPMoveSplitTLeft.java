package com.builtbroken.test.as.accelerator;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-16.
 */
public class TestPMoveSplitTLeft extends PMoveCommon
{
    public static int LEFT = 1;
    public static int FRONT = 0;

    @Test
    public void northEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.NORTH, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.NORTH);
    }

    @Test
    public void eastEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.EAST, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.EAST);
    }

    @Test
    public void southEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.SOUTH);
    }

    @Test
    public void westEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.WEST, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.WEST);
    }

    @Test
    public void northExitLeft()
    {
        checkExit(EnumFacing.NORTH, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.WEST);
    }

    @Test
    public void northExitFront()
    {
        checkExit(EnumFacing.NORTH, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.NORTH);
    }

    @Test
    public void eastExitLeft()
    {
        checkExit(EnumFacing.EAST, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.NORTH);
    }

    @Test
    public void eastExitFront()
    {
        checkExit(EnumFacing.EAST, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.EAST);
    }

    @Test
    public void southExitLeft()
    {
        checkExit(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.EAST);
    }

    @Test
    public void southExitFront()
    {
        checkExit(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.SOUTH);
    }

    @Test
    public void westExitLeft()
    {
        checkExit(EnumFacing.WEST, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.SOUTH);
    }

    @Test
    public void westExitFront()
    {
        checkExit(EnumFacing.WEST, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.WEST);
    }

    @Test
    public void northFullSplitLeft()
    {
        checkTurn(EnumFacing.NORTH, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.SOUTH, LEFT, EnumFacing.WEST);
    }

    @Test
    public void northFullSplitFront()
    {
        checkTurn(EnumFacing.NORTH, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.SOUTH, FRONT, EnumFacing.NORTH);
    }

    @Test
    public void eastFullSplitLeft()
    {
        checkTurn(EnumFacing.EAST, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.WEST, LEFT, EnumFacing.NORTH);
    }

    @Test
    public void eastFullSplitFront()
    {
        checkTurn(EnumFacing.EAST, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.WEST, FRONT, EnumFacing.EAST);
    }

    @Test
    public void southFullSplitLeft()
    {
        checkTurn(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.NORTH, LEFT, EnumFacing.EAST);
    }

    @Test
    public void southFullSplitFront()
    {
        checkTurn(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.NORTH, FRONT, EnumFacing.SOUTH);
    }

    @Test
    public void westFullSplitLeft()
    {
        checkTurn(EnumFacing.WEST, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.EAST, LEFT, EnumFacing.SOUTH);
    }

    @Test
    public void westFullSplitFront()
    {
        checkTurn(EnumFacing.WEST, TubeConnectionType.T_SPLIT_LEFT, EnumFacing.EAST, FRONT, EnumFacing.WEST);
    }
}

