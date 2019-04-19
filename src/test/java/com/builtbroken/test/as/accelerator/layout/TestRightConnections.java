package com.builtbroken.test.as.accelerator.layout;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.test.as.accelerator.ATestTube;
import com.builtbroken.test.as.accelerator.ATubeTestCommon;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-17.
 */
public class TestRightConnections extends ATubeTestCommon
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
        addTube(tube, direction, direction); //North -> north side, north facing
        addTube(tube, direction.rotateY(), direction.rotateY().getOpposite()); //North -> east side, west facing

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.CORNER_RIGHT, connectionType);
    }

    @Test
    public void northNormalInLinesA()
    {
        checkInlineA(EnumFacing.NORTH);
    }

    @Test
    public void eastNormalInLinesA()
    {
        checkInlineA(EnumFacing.EAST);
    }

    @Test
    public void southNormalInLinesA()
    {
        checkInlineA(EnumFacing.SOUTH);
    }

    @Test
    public void westNormalInLinesA()
    {
        checkInlineA(EnumFacing.WEST);
    }

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

    @Test
    public void northNormalInLinesB()
    {
        checkInlineB(EnumFacing.NORTH);
    }

    @Test
    public void eastNormalInLinesB()
    {
        checkInlineB(EnumFacing.EAST);
    }

    @Test
    public void southNormalInLinesB()
    {
        checkInlineB(EnumFacing.SOUTH);
    }

    @Test
    public void westNormalInLinesB()
    {
        checkInlineB(EnumFacing.WEST);
    }

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
