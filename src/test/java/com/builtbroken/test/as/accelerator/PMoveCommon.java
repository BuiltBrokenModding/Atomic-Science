package com.builtbroken.test.as.accelerator;

import com.builtbroken.atomic.content.effects.effects.FloatSupplier;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorParticle;
import com.builtbroken.atomic.lib.math.BlockPosHelpers;
import com.builtbroken.atomic.lib.math.MathConstF;
import com.builtbroken.atomic.lib.math.SideMathHelper;
import com.builtbroken.test.as.TestHelpers;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-09.
 */
public class PMoveCommon
{

    public static final float SPEED = 0.1f;
    public static final BlockPos BLOCK_POS_ZERO = new BlockPos(0, 0, 0);

    /**
     * Creates a new tube with a particle centered facing the same direction as facing.
     * <p>
     * Setup will be validated after creation to ensure it always works
     *
     * @param facing         - direction to point the tube
     * @param connectionType - type of tube
     * @return particle created
     */
    public static AcceleratorParticle newParticleInTube(EnumFacing facing, TubeConnectionType connectionType)
    {
        //Create
        AcceleratorParticle particle = new AcceleratorParticle(0, BLOCK_POS_ZERO, facing, 1);
        particle.setSpeed(SPEED);

        //Create tube
        particle.setCurrentNode(new AcceleratorNode(BLOCK_POS_ZERO, facing, connectionType));

        //Test init so we can fail early if something goes wrong
        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(facing, particle.getMoveDirection());
        Assertions.assertEquals(MathConstF.CENTER, particle.xf(), "Should have not moved in the x after init");
        Assertions.assertEquals(MathConstF.CENTER, particle.yf(), "Should have not moved in the y after init");
        Assertions.assertEquals(MathConstF.CENTER, particle.zf(), "Should have not moved in the z after init");

        return particle;
    }

    /**
     * Creates a new tube with a particle centered on the start side aimed at the move direction.
     * <p>
     * Setup will be validated after creation to ensure it always works
     *
     * @param facing    - direction to point the tube
     * @param type      - type of tube
     * @param moveDir   - direction to aim the particle
     * @param startSide - side to start the particle on
     * @return particle created
     */
    public static AcceleratorParticle newParticleInTube(EnumFacing facing, TubeConnectionType type, EnumFacing moveDir, EnumFacing startSide)
    {
        //Create tube and particle
        AcceleratorParticle particle = new AcceleratorParticle(0, BLOCK_POS_ZERO, moveDir, 1);
        particle.setSpeed(SPEED);

        //Create tube
        particle.setCurrentNode(new AcceleratorNode(BLOCK_POS_ZERO, facing, type));

        //Set position
        BlockPosHelpers.centerOnEdge(BLOCK_POS_ZERO, startSide,
                (x, y, z) -> particle.setPos(x, y, z));

        //Validate setup
        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(moveDir, particle.getMoveDirection());
        testOnEdge(startSide, particle);

        return particle;
    }

    /**
     * Checks if the particle moves in a lien between start and end.
     * <p>
     * Will calculate the number of steps it takes to move from start to finish. Then loop
     * moving the particle each step checking if it moved the correct amount. This test
     * does not match exact due to rounding/precision errors in floats.
     * <p>
     * Test validates start and end position in addition to each steps. This is to ensure
     * that both cases are accounted for before considering step movement valid.
     *
     * @param particle - particle that will move
     * @param data     - function to access the data TODO replace with movement direction
     * @param speed    - speed to move at TODO replace with movement direction
     * @param start    - start value, Ex: 0.0
     * @param end      - end value, Ex: 0.5
     */
    public static void checkMoveLine(AcceleratorParticle particle, FloatSupplier data, float speed, float start, float end)
    {
        final float distance = Math.round(Math.abs(start - end) * 100) / 100f;
        float speedAbs = Math.abs(speed);
        final int steps = (int) Math.floor(distance / speedAbs);

        final EnumFacing moveDir = particle.getMoveDirection();

        //Check start
        TestHelpers.compareFloats3Zeros(start, data.getAsFloat(),
                String.format("[" + 0 + "]Should have started at %.2f", start));

        //Move forward
        for (int i = 0; i < steps; i++)
        {
            final float expected = start + ((i + 1) * speed);

            //Move
            particle.update(i);

            //Make sure we didn't change direction
            Assertions.assertEquals(moveDir, particle.getMoveDirection());

            //Make sure we only moved in a strait line
            testMoveOnlyAxis(moveDir, particle);

            //Make sure we moved by expected amount
            TestHelpers.compareFloats3Zeros(expected, data.getAsFloat(),
                    String.format("[" + i + "]Should have only moved %.2f and now be %.2f", speed, expected));
        }

        //Check end
        TestHelpers.compareFloats3Zeros(end, data.getAsFloat(),
                String.format("[" + steps + "]Should have ended at %.2f", end));
    }

    /**
     * Check that we exist on the given side
     *
     * @param facing
     * @param type
     * @param exit
     */
    public static void checkExit(EnumFacing facing, TubeConnectionType type, EnumFacing exit)
    {
        //Create
        AcceleratorParticle particle = newParticleInTube(facing, type, exit, exit);

        //Tick an update so we move
        particle.update(0);

        //We shouldn't have a tube as we left it
        Assertions.assertNull(particle.getCurrentNode());

        //We shouldn't have moved
        testOnEdge(exit, particle);
    }

    /**
     * Check that we can enter a side and step once forward
     *
     * @param facing  - facing direction of tube
     * @param type    - type of tube
     * @param moveDir - move direction of the particle, will spawn on opposite side of movement.
     *                if moving north the particle will start on the south
     */
    public static void checkEnterStep(EnumFacing facing, TubeConnectionType type, EnumFacing moveDir)
    {
        final EnumFacing startSide = moveDir.getOpposite();
        final float movement = moveDir.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? SPEED : -SPEED;

        //Create
        AcceleratorParticle particle = newParticleInTube(facing, type, moveDir, moveDir.getOpposite());

        //Tick an update so we move
        particle.update(0);

        //Check we are still in the tube
        Assertions.assertNotNull(particle.getCurrentNode());

        //Check that we are still centered
        testMoveOnlyAxis(moveDir, particle);

        //Get data for test

        float startEdge;
        float moveAxis;
        if (moveDir.getAxis() == EnumFacing.Axis.X)
        {
            startEdge = SideMathHelper.getEdgeOrCenterX(startSide);
            moveAxis = particle.xf();
        }
        else if (moveDir.getAxis() == EnumFacing.Axis.Y)
        {
            startEdge = SideMathHelper.getEdgeOrCenterY(startSide);
            moveAxis = particle.yf();
        }
        else
        {
            startEdge = SideMathHelper.getEdgeOrCenterZ(startSide);
            moveAxis = particle.zf();
        }

        //Check that we move as expected
        final float expectedMove = startEdge + movement;
        TestHelpers.compareFloats3Zeros(expectedMove, moveAxis,
                "Should have only moved " + movement + " and now be " + expectedMove);

    }

    /**
     * Check that we only moved in the axis
     *
     * @param side
     * @param particle
     */
    public static void testMoveOnlyAxis(EnumFacing side, AcceleratorParticle particle)
    {
        if (side.getAxis() != EnumFacing.Axis.X)
        {
            Assertions.assertEquals(MathConstF.CENTER, particle.xf(), "Should be centered on x axis");
        }
        if (side.getAxis() != EnumFacing.Axis.Y)
        {
            Assertions.assertEquals(MathConstF.CENTER, particle.yf(), "Should be centered on y axis");
        }
        if (side.getAxis() != EnumFacing.Axis.Z)
        {
            Assertions.assertEquals(MathConstF.CENTER, particle.zf(), "Should be centered on z axis");
        }
    }

    /**
     * Check that we are still on the edge of the block
     *
     * @param edge
     * @param particle
     */
    public static void testOnEdge(EnumFacing edge, AcceleratorParticle particle)
    {
        Assertions.assertEquals(SideMathHelper.getEdgeOrCenterX(edge), particle.xf(), "Should have not moved in the x");
        Assertions.assertEquals(SideMathHelper.getEdgeOrCenterY(edge), particle.yf(), "Should have not moved in the y");
        Assertions.assertEquals(SideMathHelper.getEdgeOrCenterZ(edge), particle.zf(), "Should have not moved in the z");
    }
}
