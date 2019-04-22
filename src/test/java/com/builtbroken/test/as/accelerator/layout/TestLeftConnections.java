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
public class TestLeftConnections extends ATubeTestCommon
{
    @ParameterizedTest
    @ArgumentsSource(EnumFacingSideArgumentsProvider.class)
    public void checkSameFacing(EnumFacing direction)
    {
        //Setup conditions
        final ATestTube tube = newTube(direction, BLOCK_POS_ZERO);
        addTube(tube, direction, direction); //North -> north side, north facing
        addTube(tube, direction.rotateY().getOpposite(), direction.rotateY()); //North -> west side, east facing

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.CORNER_LEFT, connectionType);
    }

    @ParameterizedTest
    @ArgumentsSource(EnumFacingSideArgumentsProvider.class)
    public void checkInlineA(EnumFacing direction)
    {
        //Setup conditions
        final ATestTube tube = newTube(direction, BLOCK_POS_ZERO);

        //Tubes we expect to connect with
        addTube(tube, direction, direction); //North -> north side, north facing
        addTube(tube, direction.rotateY().getOpposite(), direction.rotateY()); //North -> west side, east facing

        //Tubes running in parallel that should be ignored
        addTube(tube, direction.rotateY(), direction.getOpposite());  //North -> east side, south facing
        addTube(tube, direction.getOpposite(), direction.rotateY()); //North -> south side, east facing

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.CORNER_LEFT, connectionType);
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
        addTube(tube, direction.rotateY().getOpposite(), direction.rotateY()); //North -> west side, east facing

        //Tubes running in parallel that should be ignored
        addTube(tube, direction.rotateY(), direction);  //North -> east side, north facing
        addTube(tube, direction.getOpposite(), direction.rotateY().getOpposite()); //North -> south side, west facing

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.CORNER_LEFT, connectionType);
    }
}
