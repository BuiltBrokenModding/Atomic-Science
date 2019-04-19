package com.builtbroken.test.as.accelerator.connection;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSideType;
import com.builtbroken.test.as.accelerator.ATestTube;
import com.builtbroken.test.as.accelerator.ATubeTestCommon;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * Main purpose of this test is to act as a base line for the remaining tests.
 * <p>
 * For this we use a normal tube due to it only having 2 sides. We then test
 * valid exit from the front and invalid cases (back, left, right). Its not
 * meant to be a full coverage test as that is what the super permutation test
 * is designed to cover.
 * <p>
 * Instead this test is meant to give a way to ensure the basic exit permutations
 * are functioning as expected for a simple case.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-19.
 */
public class TestNormalTubeExit extends ATubeTestCommon
{
    @ParameterizedTest
    @ArgumentsSource(ExitIntoArgumentsProvider.class)
    public void testExitFront(EnumFacing facing, TubeSide side, TubeConnectionType type)
    {
        //Setup normal tube as center
        final ATestTube tube = ATubeTestCommon.newTube(facing, BLOCK_POS_ZERO, TubeConnectionType.NORMAL);

        //Add tube of facing side and type
        addTube(tube, facing, side.getFacing(facing), type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.EXIT, tube.getConnectState(TubeSide.FRONT));
    }

    @ParameterizedTest
    @ArgumentsSource(ExitIntoArgumentsProvider.class)
    public void testNoExitBack(EnumFacing facing, TubeSide side, TubeConnectionType type)
    {
        //Setup normal tube as center
        final ATestTube tube = newTube(facing, BLOCK_POS_ZERO, TubeConnectionType.NORMAL);

        //Add tube of facing side and type
        addTube(tube, facing.getOpposite(), side.getFacing(facing.getOpposite()), type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.NONE, tube.getConnectState(TubeSide.FRONT));
    }

    @ParameterizedTest
    @ArgumentsSource(ExitIntoArgumentsProvider.class)
    public void testNoExitLeft(EnumFacing facing, TubeSide side, TubeConnectionType type)
    {
        //Setup normal tube as center
        final ATestTube tube = newTube(facing, BLOCK_POS_ZERO, TubeConnectionType.NORMAL);

        //Add tube of facing side and type
        addTube(tube, facing.rotateY().getOpposite(), side.getFacing(facing.rotateY().getOpposite()), type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.NONE, tube.getConnectState(TubeSide.FRONT));
    }

    @ParameterizedTest
    @ArgumentsSource(ExitIntoArgumentsProvider.class)
    public void testNoExitRight(EnumFacing facing, TubeSide side, TubeConnectionType type)
    {
        //Setup normal tube as center
        final ATestTube tube = newTube(facing, BLOCK_POS_ZERO, TubeConnectionType.NORMAL);

        //Add tube of facing side and type
        addTube(tube, facing.rotateY(), side.getFacing(facing.rotateY()), type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.NONE, tube.getConnectState(TubeSide.FRONT));
    }
}
