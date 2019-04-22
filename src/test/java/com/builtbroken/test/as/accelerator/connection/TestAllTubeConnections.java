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
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-22.
 */
public class TestAllTubeConnections extends ATubeTestCommon
{
    @ParameterizedTest
    @ArgumentsSource(SuperPermArgumentsProvider.class)
    public void testConnection(TubeConnectionType centerTube, EnumFacing centerRotation, TubeSide centerSide,
                          TubeConnectionType targetTube, EnumFacing targetRotation, TubeSideType expected, boolean match)
    {
        //Setup normal tube as center
        final ATestTube tube = ATubeTestCommon.newTube(centerRotation, BLOCK_POS_ZERO, centerTube);

        //Add tube of facing side and type
        addTube(tube, centerSide.getFacing(centerRotation), targetRotation, targetTube);

        //Get result of connection
        TubeSideType result = tube.getConnectState(centerSide);
        Assertions.assertEquals(expected, result);

        if(match)
        {
            Assertions.assertTrue(tube.canConnect(centerSide), "Should support connection on side. " + result);
        }
        else
        {
            Assertions.assertFalse(tube.canConnect(centerSide), "Should not support connection on side. " + result);
        }
    }
}
