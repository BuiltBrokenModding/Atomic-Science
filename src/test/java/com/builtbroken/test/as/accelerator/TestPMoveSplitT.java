package com.builtbroken.test.as.accelerator;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorParticle;
import com.builtbroken.atomic.lib.math.MathConstF;
import com.builtbroken.atomic.lib.math.SideMathHelper;
import com.builtbroken.test.as.TestHelpers;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-16.
 */
public class TestPMoveSplitT extends PMoveCommon
{

    @Test
    public void northEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.NORTH, TubeConnectionType.T_SPLIT, EnumFacing.NORTH);
    }

    @Test
    public void eastEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.EAST, TubeConnectionType.T_SPLIT, EnumFacing.EAST);
    }

    @Test
    public void southEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT, EnumFacing.SOUTH);
    }

    @Test
    public void westEnterBackStepOnce()
    {
        checkEnterStep(EnumFacing.WEST, TubeConnectionType.T_SPLIT, EnumFacing.WEST);
    }

    @Test
    public void northExitLeft()
    {
        checkExit(EnumFacing.NORTH, TubeConnectionType.T_SPLIT, EnumFacing.WEST);
    }

    @Test
    public void northExitRight()
    {
        checkExit(EnumFacing.NORTH, TubeConnectionType.T_SPLIT, EnumFacing.EAST);
    }

    @Test
    public void eastExitLeft()
    {
        checkExit(EnumFacing.EAST, TubeConnectionType.T_SPLIT, EnumFacing.NORTH);
    }

    @Test
    public void eastExitRight()
    {
        checkExit(EnumFacing.EAST, TubeConnectionType.T_SPLIT, EnumFacing.SOUTH);
    }

    @Test
    public void southExitLeft()
    {
        checkExit(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT, EnumFacing.EAST);
    }

    @Test
    public void southExitRight()
    {
        checkExit(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT, EnumFacing.WEST);
    }

    @Test
    public void westExitLeft()
    {
        checkExit(EnumFacing.WEST, TubeConnectionType.T_SPLIT, EnumFacing.SOUTH);
    }

    @Test
    public void westExitRight()
    {
        checkExit(EnumFacing.WEST, TubeConnectionType.T_SPLIT, EnumFacing.NORTH);
    }

    @Test
    public void northFullSplitLeft()
    {
        checkTurn(EnumFacing.NORTH, TubeConnectionType.T_SPLIT, EnumFacing.SOUTH, 0, EnumFacing.WEST);
    }

    @Test
    public void northFullSplitRight()
    {
        checkTurn(EnumFacing.NORTH, TubeConnectionType.T_SPLIT, EnumFacing.SOUTH, 1, EnumFacing.EAST);
    }

    @Test
    public void eastFullSplitLeft()
    {
        checkTurn(EnumFacing.EAST, TubeConnectionType.T_SPLIT, EnumFacing.WEST, 0, EnumFacing.NORTH);
    }

    @Test
    public void eastFullSplitRight()
    {
        checkTurn(EnumFacing.EAST, TubeConnectionType.T_SPLIT, EnumFacing.WEST, 1, EnumFacing.SOUTH);
    }

    @Test
    public void southFullSplitLeft()
    {
        checkTurn(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT, EnumFacing.NORTH, 0, EnumFacing.EAST);
    }

    @Test
    public void southFullSplitRight()
    {
        checkTurn(EnumFacing.SOUTH, TubeConnectionType.T_SPLIT, EnumFacing.NORTH, 1, EnumFacing.WEST);
    }

    @Test
    public void westFullSplitLeft()
    {
        checkTurn(EnumFacing.WEST, TubeConnectionType.T_SPLIT, EnumFacing.EAST, 0, EnumFacing.SOUTH);
    }

    @Test
    public void westFullSplitRight()
    {
        checkTurn(EnumFacing.WEST, TubeConnectionType.T_SPLIT, EnumFacing.EAST, 1, EnumFacing.NORTH);
    }
}

