package com.builtbroken.test.as.accelerator;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import net.minecraft.util.EnumFacing;
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
    public void northFullLeft()
    {

    }
}
