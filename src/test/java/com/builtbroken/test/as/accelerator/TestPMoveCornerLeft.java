package com.builtbroken.test.as.accelerator;

import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorParticle;
import com.builtbroken.atomic.content.machines.accelerator.tube.AcceleratorConnectionType;
import com.builtbroken.test.as.TestHelpers;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-09.
 */
public class TestPMoveCornerLeft extends PMoveCommon
{

    @Test //Test move forward from center
    public void northFacingForward()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.NORTH, AcceleratorConnectionType.CORNER_LEFT);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.4f, particle.zf(), "Should have only moved -.1f and now be 0.4f");
    }

    @Test //Test move forward from left/incoming
    public void northFacingIncoming()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.NORTH, AcceleratorConnectionType.CORNER_LEFT);
        particle.setMoveDirection(EnumFacing.EAST);
        particle.setPos(0f, 0.5f, 0.5f);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(0.5f, particle.zf(), "Should have not moved in the x");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(0.1f, particle.xf(), "Should have only moved .1f and now be 0.1f");
    }

    @Test //Test move forward from center
    public void northFacingExit()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.NORTH, AcceleratorConnectionType.CORNER_LEFT);
        particle.setPos(0.5f, 0.5f, 0f);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(EnumFacing.NORTH, particle.getMoveDirection());
        Assertions.assertEquals(0.5f, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(0.5f, particle.yf(), "Should have not moved in the y");
        Assertions.assertEquals(0.5f, particle.zf(), "Should have not moved in the z");
        Assertions.assertNull(particle.getCurrentNode());

    }

    @Test
    public void northFacingFull()
    {
        //Init
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.NORTH, AcceleratorConnectionType.CORNER_LEFT);
        particle.setMoveDirection(EnumFacing.EAST);
        particle.setPos(0f, 0.5f, 0.5f);

        //Move 1 - 5
        checkMoveLine(particle, () -> particle.xf(), SPEED, 0, 0.5f);

        //Move 6, turn point
        particle.update(0);
        Assertions.assertEquals(EnumFacing.NORTH, particle.getMoveDirection());
        TestHelpers.compareFloats3Zeros(0.5f, particle.xf(), "Should have not moved in x");
        TestHelpers.compareFloats3Zeros(0.4f, particle.zf(), "Should have 0.1 in z");

        //Move 7 - 10
        checkMoveLine(particle, () -> particle.zf(), -SPEED, 0.4f, 0f);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }
}
