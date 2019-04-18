package com.builtbroken.test.as.accelerator.connections;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-17.
 */
public class TestNormalConnections extends ConnectionCommon
{
    @Test
    public void normalFacing()
    {
        //Setup conditions
        final ATestTube tube = newTube(EnumFacing.NORTH, BLOCK_POS_ZERO);
        tube.setTiles(EnumFacing.NORTH, newTube(EnumFacing.NORTH, BLOCK_POS_ZERO.offset(EnumFacing.NORTH)));
        tube.setTiles(EnumFacing.SOUTH, newTube(EnumFacing.NORTH, BLOCK_POS_ZERO.offset(EnumFacing.SOUTH)));

        //Run method
        TubeConnectionType connectionType =  tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.NORMAL, connectionType);
    }

    @Test
    public void normalFacingLines()
    {
        //Setup conditions
        final ATestTube tube = newTube(EnumFacing.NORTH, BLOCK_POS_ZERO);

        //Normal tubes we expect to connect with
        tube.setTiles(EnumFacing.NORTH, newTube(EnumFacing.NORTH, BLOCK_POS_ZERO.offset(EnumFacing.NORTH)));
        tube.setTiles(EnumFacing.SOUTH, newTube(EnumFacing.NORTH, BLOCK_POS_ZERO.offset(EnumFacing.SOUTH)));

        //Tubes running in parallel that should be ignored
        tube.setTiles(EnumFacing.EAST, newTube(EnumFacing.NORTH, BLOCK_POS_ZERO.offset(EnumFacing.EAST)));
        tube.setTiles(EnumFacing.WEST, newTube(EnumFacing.NORTH, BLOCK_POS_ZERO.offset(EnumFacing.WEST)));

        //Run method
        TubeConnectionType connectionType =  tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.NORMAL, connectionType);
    }
}
