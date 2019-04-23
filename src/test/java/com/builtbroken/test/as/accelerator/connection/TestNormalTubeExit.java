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

    //Note: No matter what the tube will detect exit on a side even if it can't connect on that side.
    //      As the code is setup to detect the connecting tube's state not our own tube's state.

    @ParameterizedTest
    @ArgumentsSource(ExitIntoArgumentsProvider.class)
    public void testExitFront(EnumFacing facing, TubeSide side, TubeConnectionType type)
    {
        //Setup normal tube as center
        final ATestTube tube = ATubeTestCommon.newTube(facing, BLOCK_POS_ZERO, TubeConnectionType.NORMAL);

        //Add tube of facing side and type
        addTube(tube, TubeSide.FRONT.getFacing(facing), TubeSide.FRONT.getRotationRelative(facing, side), type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.EXIT, tube.getNode().getConnectedTubeState(null, TubeSide.FRONT));
        Assertions.assertTrue(tube.getNode().canConnectToTubeOnSide(TubeSide.FRONT));
    }

    @ParameterizedTest
    @ArgumentsSource(ExitIntoArgumentsProvider.class)
    public void testNoExitBack(EnumFacing facing, TubeSide side, TubeConnectionType type)
    {
        //Setup normal tube as center
        final ATestTube tube = newTube(facing, BLOCK_POS_ZERO, TubeConnectionType.NORMAL);

        //Add tube of facing side and type
        addTube(tube, TubeSide.BACK.getFacing(facing), TubeSide.BACK.getRotationRelative(facing, side), type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.EXIT, tube.getNode().getConnectedTubeState(null, TubeSide.BACK));
        Assertions.assertFalse(tube.getNode().canConnectToTubeOnSide(TubeSide.BACK));
    }

    @ParameterizedTest
    @ArgumentsSource(ExitIntoArgumentsProvider.class)
    public void testNoExitLeft(EnumFacing facing, TubeSide side, TubeConnectionType type)
    {
        //Setup normal tube as center
        final ATestTube tube = newTube(facing, BLOCK_POS_ZERO, TubeConnectionType.NORMAL);

        //Add tube of facing side and type
        addTube(tube, TubeSide.LEFT.getFacing(facing), TubeSide.LEFT.getRotationRelative(facing, side), type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.EXIT, tube.getNode().getConnectedTubeState(null, TubeSide.LEFT));
        Assertions.assertFalse(tube.getNode().canConnectToTubeOnSide(TubeSide.LEFT));
    }

    @ParameterizedTest
    @ArgumentsSource(ExitIntoArgumentsProvider.class)
    public void testNoExitRight(EnumFacing facing, TubeSide side, TubeConnectionType type)
    {
        //Setup normal tube as center
        final ATestTube tube = newTube(facing, BLOCK_POS_ZERO, TubeConnectionType.NORMAL);

        //Add tube of facing side and type
        addTube(tube, TubeSide.RIGHT.getFacing(facing), TubeSide.RIGHT.getRotationRelative(facing, side), type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.EXIT, tube.getNode().getConnectedTubeState(null, TubeSide.RIGHT));
        Assertions.assertFalse(tube.getNode().canConnectToTubeOnSide(TubeSide.RIGHT));
    }
}
