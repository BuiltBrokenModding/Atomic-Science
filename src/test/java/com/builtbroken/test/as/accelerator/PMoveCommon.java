package com.builtbroken.test.as.accelerator;

import com.builtbroken.atomic.content.effects.effects.FloatSupplier;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorParticle;
import com.builtbroken.atomic.content.machines.accelerator.tube.AcceleratorConnectionType;
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
    public static final float CENTER = 0.5f;
    public static final float EDGE_NORTH = 0;
    public static final float EDGE_SOUTH = 1;
    public static final float EDGE_EAST = 1;
    public static final float EDGE_WEST = 0;

    public static AcceleratorParticle newParticleInTube(EnumFacing facing, AcceleratorConnectionType connectionType)
    {
        //Create
        AcceleratorParticle particle = new AcceleratorParticle(new BlockPos(0, 0, 0), facing, 1);
        particle.setSpeed(SPEED);

        //Create tube
        particle.setCurrentNode(new AcceleratorNode(new BlockPos(0, 0, 0), facing, connectionType));

        //Test init so we can fail early if something goes wrong
        Assertions.assertNotNull(particle.getCurrentNode());
        Assertions.assertEquals(facing, particle.getMoveDirection());
        Assertions.assertEquals(CENTER, particle.xf(), "Should have not moved in the x after init");
        Assertions.assertEquals(CENTER, particle.yf(), "Should have not moved in the y after init");
        Assertions.assertEquals(CENTER, particle.zf(), "Should have not moved in the z after init");

        return particle;
    }

    public static void checkMoveLine(AcceleratorParticle particle, FloatSupplier data, float speed, float start, float end)
    {
        final float distance = Math.round(Math.abs(start - end) * 100) / 100f;
        float speedAbs = Math.abs(speed);
        final int steps = (int) Math.floor(distance / speedAbs);

        //Check start
        TestHelpers.compareFloats3Zeros(start, data.getAsFloat(),
                String.format("[" + 0 + "]Should have started at %.2f", start));

        //Move forward
        for (int i = 0; i < steps; i++)
        {
            final float expected = start + ((i + 1) * speed);

            //Move
            particle.update(i);

            //Assert
            TestHelpers.compareFloats3Zeros(expected, data.getAsFloat(),
                    String.format("[" + i + "]Should have only moved %.2f and now be %.2f", speed, expected));
        }

        //Check end
        TestHelpers.compareFloats3Zeros(end, data.getAsFloat(),
                String.format("[" + steps + "]Should have ended at %.2f", end));
    }
}
