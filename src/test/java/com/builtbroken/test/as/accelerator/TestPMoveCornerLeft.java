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
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-09.
 */
public class TestPMoveCornerLeft
{
    private static final float SPEED = 0.1f;

    @Test
    public void northFacing()
    {
        //Create
        AcceleratorParticle particle = new AcceleratorParticle(new BlockPos(0, 0, 0), EnumFacing.NORTH, 1);
        particle.setSpeed(SPEED);

        //Create tube
        particle.setCurrentNode(new AcceleratorNode(new BlockPos(0, 0, 0), EnumFacing.NORTH, AcceleratorConnectionType.CORNER_LEFT));

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.4f, particle.zf(), "Should have only moved -.1f and now be 0.4f");

        //Set position to edge
        particle.setMoveDirection(EnumFacing.EAST);
        particle.setPos(0f, 0.5f, 0.5f);

        //Move 1
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.1f, particle.xf(), "Should have only moved .1f and now be 0.1f");

        //Move 2
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.2f, particle.xf(), "Should have only moved .1f and now be 0.2f");

        //Move 3
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.3f, particle.xf(), "Should have only moved .1f and now be 0.3f");

        //Move 4
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.4f, particle.xf(), "Should have only moved .1f and now be 0.4f");

        //Move 5
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.5f, particle.xf(), "Should have only moved .1f and now be 0.5f");

        //Move 6, turn point
        particle.update(0);
        Assertions.assertEquals(EnumFacing.NORTH, particle.getMoveDirection());
        TestHelpers.compareFloats3Zeros(0.5f, particle.xf(), "Should have not moved in x");
        TestHelpers.compareFloats3Zeros(0.4f, particle.zf(), "Should have 0.1 in z");

        //Move 7
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.3f, particle.zf(), "Should have only moved -.1f and now be 0.3f");

        //Move 8
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.2f, particle.zf(), "Should have only moved -.1f and now be 0.2f");

        //Move 9
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0.1f, particle.zf(), "Should have only moved -.1f and now be 0.1f");

        //Move 10
        particle.update(0);
        TestHelpers.compareFloats3Zeros(0f, particle.zf(), "Should have only moved -.1f and now be 0f");

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }
}
