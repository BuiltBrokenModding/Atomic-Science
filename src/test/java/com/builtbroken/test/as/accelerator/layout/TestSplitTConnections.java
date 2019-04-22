package com.builtbroken.test.as.accelerator.layout;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.test.as.accelerator.ATestTube;
import com.builtbroken.test.as.accelerator.ATubeTestCommon;
import com.builtbroken.test.as.providers.EnumFacingSideArgumentsProvider;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-17.
 */
public class TestSplitTConnections extends ATubeTestCommon
{
    @ParameterizedTest
    @ArgumentsSource(EnumFacingSideArgumentsProvider.class)
    public void checkSameFacing(EnumFacing direction)
    {
        //Setup conditions
        final ATestTube tube = newTube(direction, BLOCK_POS_ZERO);

        //BACK, North -> south side, north facing
        addTube(tube, direction.getOpposite(), direction);

        //LEFT, North -> west side, facing west
        addTube(tube, direction.rotateY(), direction.rotateY());

        //RIGHT, North -> east side, facing east
        addTube(tube, direction.rotateY().getOpposite(), direction.rotateY().getOpposite());

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.T_SPLIT, connectionType);
    }

    @ParameterizedTest
    @ArgumentsSource(EnumFacingSideArgumentsProvider.class)
    public void checkInLineA(EnumFacing direction)
    {
        //Setup conditions
        final ATestTube tube = newTube(direction, BLOCK_POS_ZERO);

        //FRONT, shouldn't connect, North -> north side, east facing
        addTube(tube, direction, direction.rotateY());

        //BACK, NORTH -> south side, north facing
        addTube(tube, direction.getOpposite(), direction);

        //LEFT, North -> west side, facing west
        addTube(tube, direction.rotateY(), direction.rotateY());

        //RIGHT, North -> east side, facing east
        addTube(tube, direction.rotateY().getOpposite(), direction.rotateY().getOpposite());

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.T_SPLIT, connectionType);
    }

    @ParameterizedTest
    @ArgumentsSource(EnumFacingSideArgumentsProvider.class)
    public void checkInLineB(EnumFacing direction)
    {
        //Setup conditions
        final ATestTube tube = newTube(direction, BLOCK_POS_ZERO);

        //FRONT, shouldn't connect, North -> north side, west facing
        addTube(tube, direction, direction.rotateY().getOpposite());

        //BACK, NORTH -> south side, north facing
        addTube(tube, direction.getOpposite(), direction);

        //LEFT, North -> west side, facing west
        addTube(tube, direction.rotateY(), direction.rotateY());

        //RIGHT, North -> east side, facing east
        addTube(tube, direction.rotateY().getOpposite(), direction.rotateY().getOpposite());

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.T_SPLIT, connectionType);
    }
}
