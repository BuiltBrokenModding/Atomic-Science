package com.builtbroken.test.as.accelerator;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorParticle;
import com.builtbroken.test.as.TestHelpers;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests particle movement in a normal tube
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-09.
 */
public class TestPMoveNormal extends PMoveCommon
{

    @Test
    public void northForward()
    {
        //Create
        AcceleratorParticle particle = newParticleInTube(EnumFacing.NORTH, TubeConnectionType.NORMAL);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.4f, particle.zf(), "Should have only moved -.1f and now be 0.4f");
    }

    @Test
    public void northFull()
    {
        //Create
        AcceleratorParticle particle = newParticleInTube(EnumFacing.NORTH, TubeConnectionType.NORMAL);
        particle.setPos(0.5f, 0.5f, 1f);

        //Move in line
        checkMoveLine(particle, () -> particle.zf(), -SPEED, 1, 0);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void eastForward()
    {
        //Create
        AcceleratorParticle particle = newParticleInTube(EnumFacing.EAST, TubeConnectionType.NORMAL);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.zf(), "Should have not moved in the z");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.6f, particle.xf(), "Should have only moved .1f and now be 0.6f");
    }

    @Test
    public void eastFull()
    {
        //Create
        AcceleratorParticle particle = newParticleInTube(EnumFacing.EAST, TubeConnectionType.NORMAL);
        particle.setPos(0, 0.5f, 0.5f);

        //Move in line
        checkMoveLine(particle, () -> particle.xf(), SPEED, 0, 1);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void southForward()
    {
        //Create
        AcceleratorParticle particle = newParticleInTube(EnumFacing.SOUTH, TubeConnectionType.NORMAL);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.6f, particle.zf(), "Should have only moved .1f and now be 0.6f");
    }

    @Test
    public void southFull()
    {
        //Create
        AcceleratorParticle particle = newParticleInTube(EnumFacing.SOUTH, TubeConnectionType.NORMAL);
        particle.setPos(0.5f, 0.5f, 0f);

        //Move in line
        checkMoveLine(particle, () -> particle.zf(), SPEED, 0, 1);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void westForward()
    {
        //Create
        AcceleratorParticle particle = newParticleInTube(EnumFacing.WEST, TubeConnectionType.NORMAL);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.zf(), "Should have not moved in the z");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.4f, particle.xf(), "Should have only moved -.1f and now be 0.4f");
    }

    @Test
    public void westFull()
    {
        //Create
        AcceleratorParticle particle = newParticleInTube(EnumFacing.WEST, TubeConnectionType.NORMAL);
        particle.setPos(1, 0.5f, 0.5f);

        //Move in line
        checkMoveLine(particle, () -> particle.xf(), -SPEED, 1, 0);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }
}
