package com.builtbroken.test.as.accelerator;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorParticle;
import com.builtbroken.atomic.lib.math.MathConstF;
import com.builtbroken.test.as.TestHelpers;
import net.minecraft.util.EnumFacing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-09.
 */
public class TestPMoveJoinTRight extends PMoveCommon
{

    @Test //Test move forward from center
    public void northFacingForward()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.NORTH, TubeConnectionType.T_JOIN_RIGHT);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(MathConstF.CENTER, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(MathConstF.CENTER, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(MathConstF.CENTER - SPEED, particle.zf(), "Should have only moved -.1f and now be 0.4f");
    }

    @Test //Test move forward from center
    public void eastFacingForward()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.EAST, TubeConnectionType.T_JOIN_RIGHT);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(MathConstF.CENTER, particle.zf(), "Should have not moved in the z");
        Assertions.assertEquals(MathConstF.CENTER, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(MathConstF.CENTER + SPEED, particle.xf(), "Should have only moved .1f and now be 0.6f");
    }

    @Test //Test move forward from center
    public void southFacingForward()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.SOUTH, TubeConnectionType.T_JOIN_RIGHT);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(MathConstF.CENTER, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(MathConstF.CENTER, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(MathConstF.CENTER + SPEED, particle.zf(), "Should have only moved .1f and now be 0.6f");
    }


    @Test //Test move forward from center
    public void westFacingForward()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.WEST, TubeConnectionType.T_JOIN_RIGHT);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(MathConstF.CENTER, particle.zf(), "Should have not moved in the z");
        Assertions.assertEquals(MathConstF.CENTER, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(MathConstF.CENTER - SPEED, particle.xf(), "Should have only moved -.1f and now be 0.4f");
    }

    @Test //Test move forward from left/incoming
    public void northFacingIncoming()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.NORTH, TubeConnectionType.T_JOIN_RIGHT);
        particle.setMoveDirection(EnumFacing.WEST);
        particle.setPos(MathConstF.EDGE_EAST, MathConstF.CENTER, MathConstF.CENTER);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(MathConstF.CENTER, particle.zf(), "Should have not moved in the z");
        Assertions.assertEquals(MathConstF.CENTER, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(MathConstF.EDGE_EAST - SPEED, particle.xf(), "Should have only moved -.1f and now be 0.9f");
    }

    @Test //Test move forward from left/incoming
    public void eastFacingIncoming()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.EAST, TubeConnectionType.T_JOIN_RIGHT);
        particle.setMoveDirection(EnumFacing.NORTH);
        particle.setPos(MathConstF.CENTER, MathConstF.CENTER, MathConstF.EDGE_SOUTH);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(MathConstF.CENTER, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(MathConstF.CENTER, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(MathConstF.EDGE_SOUTH - SPEED, particle.zf(), "Should have only moved -.1f and now be 0.9f");
    }

    @Test //Test move forward from left/incoming
    public void southFacingIncoming()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.SOUTH, TubeConnectionType.T_JOIN_RIGHT);
        particle.setMoveDirection(EnumFacing.EAST);
        particle.setPos(MathConstF.EDGE_WEST, MathConstF.CENTER, MathConstF.CENTER);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(MathConstF.CENTER, particle.zf(), "Should have not moved in the z");
        Assertions.assertEquals(MathConstF.CENTER, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(SPEED, particle.xf(), "Should have only moved .1f and now be 0.1f");
    }

    @Test //Test move forward from left/incoming
    public void westFacingIncoming()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.WEST, TubeConnectionType.T_JOIN_RIGHT);
        particle.setMoveDirection(EnumFacing.SOUTH);
        particle.setPos(MathConstF.CENTER, MathConstF.CENTER, MathConstF.EDGE_NORTH);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(MathConstF.CENTER, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(MathConstF.CENTER, particle.yf(), "Should have not moved in the y");

        //Check move
        TestHelpers.compareFloats3Zeros(SPEED, particle.zf(), "Should have only moved .1f and now be 0.1f");
    }

    @Test //Test move forward from center
    public void northFacingExit()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.NORTH, TubeConnectionType.T_JOIN_RIGHT);
        particle.setPos(MathConstF.CENTER, MathConstF.CENTER, MathConstF.EDGE_NORTH);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertEquals(EnumFacing.NORTH, particle.getMoveDirection());
        Assertions.assertEquals(MathConstF.CENTER, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(MathConstF.CENTER, particle.yf(), "Should have not moved in the y");
        Assertions.assertEquals(MathConstF.EDGE_NORTH, particle.zf(), "Should have not moved in the z");
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test //Test move forward from center
    public void eastFacingExit()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.EAST, TubeConnectionType.T_JOIN_RIGHT);
        particle.setPos(MathConstF.EDGE_EAST, MathConstF.CENTER, MathConstF.CENTER);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertEquals(EnumFacing.EAST, particle.getMoveDirection());
        Assertions.assertEquals(MathConstF.EDGE_EAST, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(MathConstF.CENTER, particle.yf(), "Should have not moved in the y");
        Assertions.assertEquals(MathConstF.CENTER, particle.zf(), "Should have not moved in the z");
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test //Test move forward from center
    public void eastFacingWEST()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.WEST, TubeConnectionType.T_JOIN_RIGHT);
        particle.setPos(MathConstF.EDGE_WEST, MathConstF.CENTER, MathConstF.CENTER);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertEquals(EnumFacing.WEST, particle.getMoveDirection());
        Assertions.assertEquals(MathConstF.EDGE_WEST, particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(MathConstF.CENTER, particle.yf(), "Should have not moved in the y");
        Assertions.assertEquals(MathConstF.CENTER, particle.zf(), "Should have not moved in the z");
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test //Test move forward from center
    public void southFacingExit()
    {
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.SOUTH, TubeConnectionType.T_JOIN_RIGHT);
        particle.setPos(MathConstF.CENTER, MathConstF.CENTER, MathConstF.EDGE_SOUTH);

        //Tick an update so we move
        particle.update(0);

        Assertions.assertEquals(EnumFacing.SOUTH, particle.getMoveDirection());
        Assertions.assertEquals(MathConstF.EDGE_SOUTH, particle.zf(), "Should have not moved in the z");
        Assertions.assertEquals(MathConstF.CENTER, particle.yf(), "Should have not moved in the y");
        Assertions.assertEquals(MathConstF.CENTER, particle.xf(), "Should have not moved in the x");
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void northFacingFull()
    {
        //Init
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.NORTH, TubeConnectionType.T_JOIN_RIGHT);
        particle.setMoveDirection(EnumFacing.WEST);
        particle.setPos(MathConstF.EDGE_EAST, MathConstF.CENTER, MathConstF.CENTER);

        //Move 1 - 5
        checkMoveLine(particle, () -> particle.xf(), -SPEED, MathConstF.EDGE_EAST, MathConstF.CENTER);

        //Move 6, turn point
        particle.update(0);
        Assertions.assertEquals(EnumFacing.NORTH, particle.getMoveDirection());
        TestHelpers.compareFloats3Zeros(MathConstF.CENTER, particle.xf(), "Should have not moved in x");
        TestHelpers.compareFloats3Zeros(MathConstF.CENTER - SPEED, particle.zf(), "Should have 0.1 in z");

        //Move 7 - 10
        checkMoveLine(particle, () -> particle.zf(), -SPEED, MathConstF.CENTER - SPEED, MathConstF.EDGE_NORTH);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void eastFacingFull()
    {
        //Init
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.EAST, TubeConnectionType.T_JOIN_RIGHT);
        particle.setMoveDirection(EnumFacing.NORTH);
        particle.setPos(MathConstF.CENTER, MathConstF.CENTER, MathConstF.EDGE_SOUTH);

        //Move 1 - 5
        checkMoveLine(particle, () -> particle.zf(), -SPEED, MathConstF.EDGE_SOUTH, MathConstF.CENTER);

        //Move 6, turn point
        particle.update(0);
        Assertions.assertEquals(EnumFacing.EAST, particle.getMoveDirection());
        TestHelpers.compareFloats3Zeros(MathConstF.CENTER, particle.zf(), "Should have not moved in z");
        TestHelpers.compareFloats3Zeros(MathConstF.CENTER + SPEED, particle.xf(), "Should have 0.6 in x");

        //Move 7 - 10
        checkMoveLine(particle, () -> particle.xf(), SPEED, MathConstF.CENTER + SPEED, MathConstF.EDGE_EAST);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void southFacingFull()
    {
        //Init
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.SOUTH, TubeConnectionType.T_JOIN_RIGHT);
        particle.setMoveDirection(EnumFacing.EAST);
        particle.setPos(MathConstF.EDGE_WEST, MathConstF.CENTER, MathConstF.CENTER);

        //Move 1 - 5
        checkMoveLine(particle, () -> particle.xf(), SPEED, MathConstF.EDGE_WEST, MathConstF.CENTER);

        //Move 6, turn point
        particle.update(0);
        Assertions.assertEquals(EnumFacing.SOUTH, particle.getMoveDirection());
        TestHelpers.compareFloats3Zeros(MathConstF.CENTER, particle.xf(), "Should have not moved in x");
        TestHelpers.compareFloats3Zeros(MathConstF.CENTER + SPEED, particle.zf(), "Should have 0.1 in z");

        //Move 7 - 10
        checkMoveLine(particle, () -> particle.zf(), SPEED, MathConstF.CENTER + SPEED, MathConstF.EDGE_SOUTH);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void westFacingFull()
    {
        //Init
        final AcceleratorParticle particle = newParticleInTube(EnumFacing.WEST, TubeConnectionType.T_JOIN_RIGHT);
        particle.setMoveDirection(EnumFacing.SOUTH);
        particle.setPos(MathConstF.CENTER, MathConstF.CENTER, MathConstF.EDGE_NORTH);

        //Move 1 - 5
        checkMoveLine(particle, () -> particle.zf(), SPEED, MathConstF.EDGE_NORTH, MathConstF.CENTER);

        //Move 6, turn point
        particle.update(0);
        Assertions.assertEquals(EnumFacing.WEST, particle.getMoveDirection());
        TestHelpers.compareFloats3Zeros(MathConstF.CENTER, particle.zf(), "Should have not moved in z");
        TestHelpers.compareFloats3Zeros(MathConstF.CENTER - SPEED, particle.xf(), "Should have 0.4 in x");

        //Move 7 - 10
        checkMoveLine(particle, () -> particle.xf(), -SPEED, MathConstF.CENTER - SPEED, MathConstF.EDGE_WEST);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void northFacingFullForward()
    {
        //Create
        AcceleratorParticle particle = newParticleInTube(EnumFacing.NORTH, TubeConnectionType.T_JOIN_RIGHT);
        particle.setPos(0.5f, 0.5f, 1f);

        //Move in line
        checkMoveLine(particle, () -> particle.zf(), -SPEED, 1, 0);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void eastFacingFullForward()
    {
        //Create
        AcceleratorParticle particle = newParticleInTube(EnumFacing.EAST, TubeConnectionType.T_JOIN_RIGHT);
        particle.setPos(0, 0.5f, 0.5f);

        //Move in line
        checkMoveLine(particle, () -> particle.xf(), SPEED, 0, 1);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void southFacingFullForward()
    {
        //Create
        AcceleratorParticle particle = newParticleInTube(EnumFacing.SOUTH, TubeConnectionType.T_JOIN_RIGHT);
        particle.setPos(0.5f, 0.5f, 0f);

        //Move in line
        checkMoveLine(particle, () -> particle.zf(), SPEED, 0, 1);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }

    @Test
    public void westFacingFullForward()
    {
        //Create
        AcceleratorParticle particle = newParticleInTube(EnumFacing.WEST, TubeConnectionType.T_JOIN_RIGHT);
        particle.setPos(1, 0.5f, 0.5f);

        //Move in line
        checkMoveLine(particle, () -> particle.xf(), -SPEED, 1, 0);

        //Check that we exited the tube, previous step we would be at the edge of the tube
        particle.update(0);
        Assertions.assertNull(particle.getCurrentNode());
    }
}
