package com.builtbroken.test.as.accelerator.layout;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-17.
 */
public class TestSplitTRightConnections extends ConnectionCommon
{
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

        //BACK, NORTH -> south side, north facing
        addTube(tube, direction.getOpposite(), direction);

        //RIGHT, North -> east side, facing east
        addTube(tube, direction.rotateY(), direction.rotateY());

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.T_SPLIT_RIGHT, connectionType);
    }

    @Test
    public void inlineANorth()
    {
        checkInLineA(EnumFacing.NORTH);
    }

    @Test
    public void inlineAEast()
    {
        checkInLineA(EnumFacing.EAST);
    }

    @Test
    public void inlineASouth()
    {
        checkInLineA(EnumFacing.SOUTH);
    }

    @Test
    public void inlineAWest()
    {
        checkInLineA(EnumFacing.WEST);
    }

    private void checkInLineA(EnumFacing direction)
    {
        //Setup conditions
        final ATestTube tube = newTube(direction, BLOCK_POS_ZERO);

        //FRONT, North -> north side, north facing
        addTube(tube, direction, direction);

        //BACK, NORTH -> south side, north facing
        addTube(tube, direction.getOpposite(), direction);

        //LEFT, North -> west side, facing north
        addTube(tube, direction.rotateY().getOpposite(), direction);

        //RIGHT, North -> east side, facing east
        addTube(tube, direction.rotateY(), direction.rotateY());

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.T_SPLIT_RIGHT, connectionType);
    }

    @Test
    public void inlineBNorth()
    {
        checkInLineB(EnumFacing.NORTH);
    }

    @Test
    public void inlineBEast()
    {
        checkInLineB(EnumFacing.EAST);
    }

    @Test
    public void inlineBSouth()
    {
        checkInLineA(EnumFacing.SOUTH);
    }

    @Test
    public void inlineBWest()
    {
        checkInLineB(EnumFacing.WEST);
    }

    private void checkInLineB(EnumFacing direction)
    {
        //Setup conditions
        final ATestTube tube = newTube(direction, BLOCK_POS_ZERO);

        addTube(tube, direction, direction);

        //BACK, NORTH -> south side, north facing
        addTube(tube, direction.getOpposite(), direction);

        //LEFT, North -> west side, facing north
        addTube(tube, direction.rotateY().getOpposite(), direction.getOpposite());

        //RIGHT, North -> east side, facing east
        addTube(tube, direction.rotateY(), direction.rotateY());

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.T_SPLIT_RIGHT, connectionType);
    }
}
