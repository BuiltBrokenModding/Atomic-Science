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
public class TestNormalConnections extends ATubeTestCommon
{
    @ParameterizedTest
    @ArgumentsSource(EnumFacingSideArgumentsProvider.class)
    public void checkSameFacing(EnumFacing direction)
    {
        //Setup conditions
        final ATestTube tube = newTube(direction, BLOCK_POS_ZERO);
        addTube(tube, direction, direction);
        addTube(tube, direction.getOpposite(), direction);

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType(null);

        //Test
        Assertions.assertEquals(TubeConnectionType.NORMAL, connectionType);
    }

    @ParameterizedTest
    @ArgumentsSource(EnumFacingSideArgumentsProvider.class)
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
        TubeConnectionType connectionType = tube.calcConnectionType(null);

        //Test
        Assertions.assertEquals(TubeConnectionType.NORMAL, connectionType);
    }
}
