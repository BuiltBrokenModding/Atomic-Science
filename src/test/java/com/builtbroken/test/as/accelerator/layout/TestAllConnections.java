package com.builtbroken.test.as.accelerator.layout;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSideType;
import com.builtbroken.test.as.accelerator.ATestTube;
import com.builtbroken.test.as.accelerator.ATubeTestCommon;
import com.builtbroken.test.as.accelerator.connection.EntryFromArgumentsProvider;
import com.builtbroken.test.as.accelerator.connection.ExitIntoArgumentsProvider;
import com.builtbroken.test.as.world.FakeWorldAccess;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-23.
 */
public class TestAllConnections extends ATubeTestCommon
{

    @ParameterizedTest
    @MethodSource("provideData")
    public void superLayoutCheck(EnumFacing centerFace, TubeConnectionType expected, TestSide[] sides)
    {
        //Build center tube
        final ATestTube tube = newTube(centerFace, BLOCK_POS_ZERO);

        //Build fake world
        final FakeWorldAccess worldAccess = new FakeWorldAccess();
        for (TestSide side : sides)
        {
            if (side.connectionType != TubeSideType.NONE)
            {
                final EnumFacing placeSide = side.connectionSide.getFacing(centerFace);
                final BlockPos placePos = tube.getPos().offset(placeSide);

                ATestTube testTube = newTube(side.tubeFacing, placePos, side.tubeType);
                worldAccess.addTile(placePos, testTube);

                Assertions.assertEquals(testTube, worldAccess.getTileEntity(placePos));
            }
        }

        //Trigger connection building
        tube.updateConnections(worldAccess,false, false);

        //Test connections
        for (TestSide side : sides)
        {
            //Validate we have the right connection for the side
            TubeSideType connectionState = tube.getNode().getConnectionState(side.connectionSide);
            Assertions.assertEquals(side.connectionType, connectionState, "" + side);
        }

        //Test layout
        Assertions.assertEquals(expected, tube.getConnectionType());
        Assertions.assertEquals(expected, tube.getNode().getConnectionType());

        //Test connections
        for (TestSide side : sides)
        {
            //Validate that we can connect
            boolean canConnect = tube.getNode().canConnectToTubeOnSide(side.connectionSide);
            Assertions.assertEquals(side.connectionType != TubeSideType.NONE, canConnect, "Failed for side " + side.connectionSide);
        }
    }

    private static Stream<Arguments> provideData()
    {
        final List<Arguments> permutations = new ArrayList();

        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            for (TubeConnectionType type : TubeConnectionType.values())
            {
                if(type.canUserPlace)
                {
                    createPermutations(permutations, facing, type);
                }
            }
        }

        return permutations.stream();
    }

    private static void createPermutations(List<Arguments> permutations, EnumFacing centerFacing, TubeConnectionType centerType)
    {
        final TestSide[] testSides = new TestSide[4];

        //Init test data so we have a tube to start with on each valid side
        for (TubeSide side : TubeSide.SIDES)
        {
            testSides[side.ordinal()] = new TestSide();

            TestSide testSide = testSides[side.ordinal()];
            testSide.connectionType = centerType.getTypeForSide(side);
            testSide.connectionSide = side;

            testSide.tubeFacing = side.getFacing(centerFacing);

            if (testSide.connectionType != TubeSideType.NONE)
            {
                testSide.tubeType = TubeConnectionType.NORMAL;
            }

            if (testSide.connectionType == TubeSideType.ENTER)
            {
                testSide.tubeFacing = testSide.tubeFacing.getOpposite();
            }
        }

        //Generate test permutations 1 at a time
        for (TubeSide side : TubeSide.SIDES) //TODO generate permutations per side of side instead of only 1 side at a time
        {
            final TestSide testSide = testSides[side.ordinal()];
            if (testSide.connectionType == TubeSideType.ENTER)
            {
                for(Object[] data : EntryFromArgumentsProvider.getEntryData())
                {
                    final TubeSide targetSide = (TubeSide) data[0];
                    final TubeConnectionType targetType = (TubeConnectionType) data[1];

                    permutations.add(buildTest(centerFacing, centerType, side, testSides, targetSide, targetType));
                }
            }
            else if(testSide.connectionType == TubeSideType.EXIT)
            {
                for(Object[] data : ExitIntoArgumentsProvider.getExitData())
                {
                    final TubeSide targetSide = (TubeSide) data[0];
                    final TubeConnectionType targetType = (TubeConnectionType) data[1];

                    permutations.add(buildTest(centerFacing, centerType, side, testSides, targetSide, targetType));
                }
            }
        }
    }

    private static Arguments buildTest(EnumFacing centerFacing, TubeConnectionType centerType, TubeSide side,
                                TestSide[] testSides,
                                TubeSide targetSide, TubeConnectionType targetType)
    {
        //Calculated facing direction of tube
        final EnumFacing targetRotation = side.getRotationRelative(centerFacing, targetSide);

        //Clone test sides
        TestSide[] copySides = copy(testSides);

        //Set side with new tube data
        copySides[side.ordinal()].tubeType = targetType;
        copySides[side.ordinal()].tubeFacing = targetRotation;

        return Arguments.of(centerFacing, centerType, copySides);
    }

    private static TestSide[] copy(TestSide[] sides)
    {
        TestSide[] newSides = new TestSide[sides.length];
        for(int i = 0; i < sides.length; i++)
        {
            newSides[i] = sides[i].copy();
        }
        return newSides;
    }

    private static class TestSide
    {
        TubeSide connectionSide;
        TubeSideType connectionType;

        EnumFacing tubeFacing;
        TubeConnectionType tubeType;

        public TestSide copy()
        {
            TestSide side = new TestSide();
            side.connectionSide = connectionSide;
            side.connectionType = connectionType;
            side.tubeFacing = tubeFacing;
            side.tubeType = tubeType;

            return side;
        }

        @Override
        public String toString()
        {
            return "TestSide[" + connectionSide + ", " + connectionType + ", " + tubeFacing + ", " + tubeType + "]";
        }
    }
}
