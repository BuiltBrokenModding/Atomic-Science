package com.builtbroken.test.as.accelerator;

import com.builtbroken.atomic.content.effects.effects.FloatSupplier;
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

    private static final float SPEED = 0.1f;

    @Test
    public void moveNorth()
    {
        //Create
        AcceleratorParticle particle = new AcceleratorParticle(new BlockPos(0, 0, 0), EnumFacing.NORTH, 1);
        particle.setSpeed(SPEED);

        //Create tube
        particle.setCurrentNode(new AcceleratorNode(new BlockPos(0, 0, 0), EnumFacing.NORTH, AcceleratorConnectionType.NORMAL));

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.4f, particle.zf(), "Should have only moved -.1f and now be 0.4f");

        //Set position to edge
        particle.setPos(0.5f, 0.5f, 1f);

        //Move in line
        checkMoveLine(particle, () -> particle.zf(), -SPEED, 1);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void moveEast()
    {
        //Create
        AcceleratorParticle particle = new AcceleratorParticle(new BlockPos(0, 0, 0), EnumFacing.EAST, 1);
        particle.setSpeed(SPEED);

        //Create tube
        particle.setCurrentNode(new AcceleratorNode(new BlockPos(0, 0, 0), EnumFacing.EAST, AcceleratorConnectionType.NORMAL));

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.zf(), "Should have not moved in the z");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.6f, particle.xf(), "Should have only moved .1f and now be 0.6f");

        //Set position to edge
        particle.setPos(0, 0.5f, 0.5f);

        //Move in line
        checkMoveLine(particle, () -> particle.xf(), SPEED, 0);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void moveSouth()
    {
        //Create
        AcceleratorParticle particle = new AcceleratorParticle(new BlockPos(0, 0, 0), EnumFacing.SOUTH, 1);
        particle.setSpeed(SPEED);

        //Create tube
        particle.setCurrentNode(new AcceleratorNode(new BlockPos(0, 0, 0), EnumFacing.SOUTH, AcceleratorConnectionType.NORMAL));

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.6f, particle.zf(), "Should have only moved .1f and now be 0.6f");

        //Set position to edge
        particle.setPos(0.5f, 0.5f, 0f);

        //Move in line
        checkMoveLine(particle, () -> particle.zf(), SPEED, 0);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void moveWest()
    {
        //Create
        AcceleratorParticle particle = new AcceleratorParticle(new BlockPos(0, 0, 0), EnumFacing.WEST, 1);
        particle.setSpeed(SPEED);

        //Create tube
        particle.setCurrentNode(new AcceleratorNode(new BlockPos(0, 0, 0), EnumFacing.WEST, AcceleratorConnectionType.NORMAL));

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.zf(), "Should have not moved in the z");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.4f, particle.xf(), "Should have only moved -.1f and now be 0.4f");

        //Set position to edge
        particle.setPos(1, 0.5f, 0.5f);

        //Move in line
        checkMoveLine(particle, () -> particle.xf(), -SPEED, 1);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    public void checkMoveLine(AcceleratorParticle particle, FloatSupplier data, float speed, float start)
    {
        //Check start
        TestHelpers.compareFloats3Zeros(start, data.getAsFloat(),
                String.format("[" + 0 + "]Should have started at %.2f", start));

        //Move forward
        for (int i = 0; i < 10; i++)
        {
            final float expected = start + ((i + 1) * speed);

            //Move
            particle.update(i);

            //Assert
            TestHelpers.compareFloats3Zeros(expected, data.getAsFloat(),
                    String.format("[" + i + "]Should have only moved %.2f and now be %.2f", speed, expected));
        }
    }
}
