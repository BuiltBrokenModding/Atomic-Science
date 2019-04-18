package com.builtbroken.test.as.accelerator.connections;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-17.
 */
public class TestSplitConnections extends ConnectionCommon
{
    //Split only has 1 valid state, as it needs all 4 connections to work
    //Meaning invalid states are not possible since it wouldn't be a split

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

        //FRONT, North -> north side, north facing
        addTube(tube, direction, direction);

        //BACK, North -> south side, facing north
        addTube(tube, direction.getOpposite(), direction);

        //LEFT, North -> west side, facing west
        addTube(tube, direction.rotateY().getOpposite(), direction.rotateY().getOpposite());

        //RIGHT, North -> east side, facing east
        addTube(tube, direction.rotateY(), direction.rotateY());

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.SPLIT, connectionType);
    }
}
