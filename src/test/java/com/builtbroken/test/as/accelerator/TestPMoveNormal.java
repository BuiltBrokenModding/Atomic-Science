package com.builtbroken.test.as.accelerator;

import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorParticle;
import com.builtbroken.atomic.content.machines.accelerator.tube.AcceleratorConnectionType;
import com.builtbroken.test.as.TestHelpers;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests particle movement in a normal tube
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-09.
 */
public class TestPMoveNormal
{

    @Test
    public void moveNorth()
    {
        //Create
        AcceleratorParticle particle = new AcceleratorParticle(new BlockPos(0, 0, 0), EnumFacing.NORTH, 1);
        particle.setSpeed(0.1f);

        //Create tube
        particle.setCurrentNode(new AcceleratorNode(new BlockPos(0, 0, 0), EnumFacing.NORTH, AcceleratorConnectionType.NORMAL));

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.4f, particle.zf(), "Should have only moved -.1f and now be 0.4f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.3f, particle.zf(), "Should have only moved -.1f and now be 0.3f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.2f, particle.zf(), "Should have only moved -.1f and now be 0.2f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.1f, particle.zf(), "Should have only moved -.1f and now be 0.1f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.0f, particle.zf(), "Should have only moved -.1f and now be 0.0f");

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void moveEast()
    {
        //Create
        AcceleratorParticle particle = new AcceleratorParticle(new BlockPos(0, 0, 0), EnumFacing.EAST, 1);
        particle.setSpeed(0.1f);

        //Create tube
        particle.setCurrentNode(new AcceleratorNode(new BlockPos(0, 0, 0), EnumFacing.EAST, AcceleratorConnectionType.NORMAL));

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.zf(), "Should have not moved in the z");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.6f, particle.xf(), "Should have only moved .1f and now be 0.6f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.7f, particle.xf(), "Should have only moved .1f and now be 0.7f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.8f, particle.xf(), "Should have only moved .1f and now be 0.8");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.9f, particle.xf(), "Should have only moved .1f and now be 0.9f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(1f, particle.xf(), "Should have only moved .1f and now be 1f");

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void moveSouth()
    {
        //Create
        AcceleratorParticle particle = new AcceleratorParticle(new BlockPos(0, 0, 0), EnumFacing.SOUTH, 1);
        particle.setSpeed(0.1f);

        //Create tube
        particle.setCurrentNode(new AcceleratorNode(new BlockPos(0, 0, 0), EnumFacing.SOUTH, AcceleratorConnectionType.NORMAL));

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.6f, particle.zf(), "Should have only moved .1f and now be 0.6f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.7f, particle.zf(), "Should have only moved .1f and now be 0.7f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.8f, particle.zf(), "Should have only moved .1f and now be 0.8f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.9f, particle.zf(), "Should have only moved .1f and now be 0.9f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(1f, particle.zf(), "Should have only moved .1f and now be 1f");

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void moveWest()
    {
        //Create
        AcceleratorParticle particle = new AcceleratorParticle(new BlockPos(0, 0, 0), EnumFacing.WEST, 1);
        particle.setSpeed(0.1f);

        //Create tube
        particle.setCurrentNode(new AcceleratorNode(new BlockPos(0, 0, 0), EnumFacing.WEST, AcceleratorConnectionType.NORMAL));

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.zf(), "Should have not moved in the z");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.4f, particle.xf(), "Should have only moved -.1f and now be 0.4f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.3f, particle.xf(), "Should have only moved -.1f and now be 0.3f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.2f, particle.xf(), "Should have only moved -.1f and now be 0.2f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.1f, particle.xf(), "Should have only moved -.1f and now be 0.1f");

        //Move again
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.0f, particle.xf(), "Should have only moved -.1f and now be 0.0f");

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }
}
