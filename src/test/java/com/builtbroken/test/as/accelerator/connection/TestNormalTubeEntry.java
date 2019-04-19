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
 * valid entry from the back and invalid cases (front, left, right). Its not
 * meant to be a full coverage test as that is what the super permutation test
 * is designed to cover.
 * <p>
 * Instead this test is meant to give a way to ensure the basic entry permutations
 * are functioning as expected for a simple case.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-19.
 */
public class TestNormalTubeEntry extends ATubeTestCommon
{
    //https://www.baeldung.com/parameterized-tests-junit-5

    //Test that we can accept any exit side of a tube into the back as an entry
    @ParameterizedTest
    @ArgumentsSource(EntryFromArgumentsProvider.class)
    public void testEntry(EnumFacing facing, TubeSide side, TubeConnectionType type)
    {
        //Setup normal tube as center
        final ATestTube tube = ATubeTestCommon.newTube(facing, BLOCK_POS_ZERO, TubeConnectionType.NORMAL);

        //Add tube of facing side and type
        addTube(tube, facing.getOpposite(), side.getFacing(facing), type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.ENTER, tube.getConnectState(TubeSide.BACK));
    }

    //Test that we can't take exit points as entries into the front
    @ParameterizedTest
    @ArgumentsSource(EntryFromArgumentsProvider.class)
    public void testNoEntryFront(EnumFacing facing, TubeSide side, TubeConnectionType type)
    {
        //Setup normal tube as center
        final ATestTube tube = newTube(facing, BLOCK_POS_ZERO, TubeConnectionType.NORMAL);

        //Add tube of facing side and type
        addTube(tube, facing, side.getFacing(facing.getOpposite()), type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.NONE, tube.getConnectState(TubeSide.BACK));
    }

    //Test that we can't take exit points as entries into the front
    @ParameterizedTest
    @ArgumentsSource(EntryFromArgumentsProvider.class)
    public void testNoEntryLeft(EnumFacing facing, TubeSide side, TubeConnectionType type)
    {
        //Setup normal tube as center
        final ATestTube tube = newTube(facing, BLOCK_POS_ZERO, TubeConnectionType.NORMAL);

        //Add tube of facing side and type
        addTube(tube, facing.rotateY().getOpposite(), side.getFacing(facing.rotateY()), type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.NONE, tube.getConnectState(TubeSide.BACK));
    }

    //Test that we can't take exit points as entries into the front
    @ParameterizedTest
    @ArgumentsSource(EntryFromArgumentsProvider.class)
    public void testNoEntryRight(EnumFacing facing, TubeSide side, TubeConnectionType type)
    {
        //Setup normal tube as center
        final ATestTube tube = newTube(facing, BLOCK_POS_ZERO, TubeConnectionType.NORMAL);

        //Add tube of facing side and type
        addTube(tube, facing.rotateY(), side.getFacing(facing.rotateY().getOpposite()), type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.NONE, tube.getConnectState(TubeSide.BACK));
    }
}
