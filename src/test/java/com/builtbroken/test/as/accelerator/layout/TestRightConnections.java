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
public class TestRightConnections extends ATubeTestCommon
{
    //Tests the guessing code
    @ParameterizedTest
    @ArgumentsSource(EnumFacingSideArgumentsProvider.class)
    public void checkSideOnly(EnumFacing direction)
    {
        //Setup conditions
        final ATestTube tube = newTube(direction, BLOCK_POS_ZERO);
        addTube(tube, direction.rotateY(), direction.rotateY().getOpposite()); //North -> east side, west facing

        //Run method A
        TubeConnectionType connectionType = tube.calcConnectionType();
        Assertions.assertEquals(TubeConnectionType.INVALID, connectionType);

        //Run method B
        connectionType = tube.guessConnectionType();
        Assertions.assertEquals(TubeConnectionType.CORNER_RIGHT, connectionType);
    }

    @ParameterizedTest
    @ArgumentsSource(EnumFacingSideArgumentsProvider.class)
    public void checkSameFacing(EnumFacing direction)
    {
        //Setup conditions
        final ATestTube tube = newTube(direction, BLOCK_POS_ZERO);
        addTube(tube, direction, direction); //North -> north side, north facing
        addTube(tube, direction.rotateY(), direction.rotateY().getOpposite()); //North -> east side, west facing

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.CORNER_RIGHT, connectionType);
    }

    @ParameterizedTest
    @ArgumentsSource(EnumFacingSideArgumentsProvider.class)
    public void checkInlineA(EnumFacing direction)
    {
        //Setup conditions
        final ATestTube tube = newTube(direction, BLOCK_POS_ZERO);

        //Tubes we expect to connect with
        addTube(tube, direction, direction); //North -> north side, north facing
        addTube(tube, direction.rotateY(), direction.rotateY().getOpposite()); //North -> east side, west facing

        //Tubes running in parallel that should be ignored
        addTube(tube, direction.rotateY().getOpposite(), direction.getOpposite());  //North -> west side, south facing
        addTube(tube, direction.getOpposite(), direction.rotateY()); //North -> south side, east facing

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.CORNER_RIGHT, connectionType);
    }

    @ParameterizedTest
    @ArgumentsSource(EnumFacingSideArgumentsProvider.class)
    public void checkInlineB(EnumFacing direction)
    {
        //Setup conditions
        //Setup conditions
        final ATestTube tube = newTube(direction, BLOCK_POS_ZERO);

        //Tubes we expect to connect with
        addTube(tube, direction, direction); //North -> north side, north facing
        addTube(tube, direction.rotateY(), direction.rotateY().getOpposite()); //North -> east side, west facing

        //Tubes running in parallel that should be ignored
        addTube(tube, direction.rotateY().getOpposite(), direction);  //North -> west side, north facing
        addTube(tube, direction.getOpposite(), direction.rotateY().getOpposite()); //North -> south side, west facing

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.CORNER_RIGHT, connectionType);
    }
}
