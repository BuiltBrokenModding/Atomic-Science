package com.builtbroken.test.as.accelerator.layout;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSideType;
import com.builtbroken.test.as.accelerator.connection.EntryTubeArgumentsProvider;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-17.
 */
public class TestNormalConnections extends ConnectionCommon
{
    //https://www.baeldung.com/parameterized-tests-junit-5

    //Test that we can accept any exit side of a tube into the back as an entry
    @ParameterizedTest
    @ArgumentsSource(EntryTubeArgumentsProvider.class)
    public void testEntry(EnumFacing facing, TubeSide side, TubeConnectionType type)
    {
        //Setup normal tube as center
        final ATestTube tube = newTube(facing, BLOCK_POS_ZERO, TubeConnectionType.NORMAL);

        //Add tube of facing side and type
        addTube(tube, facing.getOpposite(), side.getFacing(facing), type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.ENTER, tube.getConnectState(TubeSide.BACK));
    }

    //Test that we can't take exit points as entries into the front
    @ParameterizedTest
    @ArgumentsSource(EntryTubeArgumentsProvider.class)
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
    @ArgumentsSource(EntryTubeArgumentsProvider.class)
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
    @ArgumentsSource(EntryTubeArgumentsProvider.class)
    public void testNoEntryRight(EnumFacing facing, TubeSide side, TubeConnectionType type)
    {
        //Setup normal tube as center
        final ATestTube tube = newTube(facing, BLOCK_POS_ZERO, TubeConnectionType.NORMAL);

        //Add tube of facing side and type
        addTube(tube, facing.rotateY(), side.getFacing(facing.rotateY().getOpposite()), type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.NONE, tube.getConnectState(TubeSide.BACK));
    }

    @Test
    public void normalNorth()
    {
        checkSameFacing(EnumFacing.NORTH);
    }

    @Test
    public void normalEast()
    {
        checkSameFacing(EnumFacing.EAST);
    }

    @Test
    public void normalSouth()
    {
        checkSameFacing(EnumFacing.SOUTH);
    }

    @Test
    public void normalWest()
    {
        checkSameFacing(EnumFacing.WEST);
    }

    private void checkSameFacing(EnumFacing direction)
    {
        //Setup conditions
        final ATestTube tube = newTube(direction, BLOCK_POS_ZERO);
        addTube(tube, direction, direction);
        addTube(tube, direction.getOpposite(), direction);

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.NORMAL, connectionType);
    }

    @Test
    public void northNormalInLines()
    {
        checkInline(EnumFacing.NORTH);
    }

    @Test
    public void eastNormalInLines()
    {
        checkInline(EnumFacing.EAST);
    }

    @Test
    public void southNormalInLines()
    {
        checkInline(EnumFacing.SOUTH);
    }

    @Test
    public void westNormalInLines()
    {
        checkInline(EnumFacing.WEST);
    }

    public void checkInline(EnumFacing direction)
    {
        //Setup conditions
        final ATestTube tube = newTube(direction, BLOCK_POS_ZERO);

        //Tubes we expect to connect with
        addTube(tube, direction, direction);
        addTube(tube, direction.getOpposite(), direction);

        //Tubes running in parallel that should be ignored
        addTube(tube, direction.rotateY(), direction);
        addTube(tube, direction.rotateY().getOpposite(), direction);

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.NORMAL, connectionType);
    }
}
