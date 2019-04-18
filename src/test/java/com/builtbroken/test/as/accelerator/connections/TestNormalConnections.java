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
        addTube(tube, direction, direction);
        addTube(tube, direction.getOpposite(), direction);

        //Run method
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.NORMAL, connectionType);
    }

    @Test
    public void northNormalInLines()
    {
        checkInline(EnumFacing.NORTH);
    }

    @Test
    public void eastNormalInLines()
    {
        checkInline(EnumFacing.EAST);
    }

    @Test
    public void southNormalInLines()
    {
        checkInline(EnumFacing.SOUTH);
    }

    @Test
    public void westNormalInLines()
    {
        checkInline(EnumFacing.WEST);
    }

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
        TubeConnectionType connectionType = tube.calcConnectionType();

        //Test
        Assertions.assertEquals(TubeConnectionType.NORMAL, connectionType);
    }
}
