package com.builtbroken.test.as.accelerator.connection;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSideType;
import com.builtbroken.test.as.accelerator.ATestTube;
import com.builtbroken.test.as.accelerator.ATubeTestCommon;
import com.builtbroken.test.as.accelerator.TubeFaceSideArgumentsProvider;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * Test that with no tube on the side we should have no connection
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-19.
 */
public class TestNoConnections extends ATubeTestCommon
{

    @ParameterizedTest
    @ArgumentsSource(TubeFaceSideArgumentsProvider.class)
    public void testEntry(TubeConnectionType type, EnumFacing facing, TubeSide side)
    {
        //Setup normal tube as center
        final ATestTube tube = ATubeTestCommon.newTube(facing, BLOCK_POS_ZERO, type);

        //Test that we can connect
        Assertions.assertEquals(TubeSideType.NONE, tube.getConnectState(side));
    }
}
