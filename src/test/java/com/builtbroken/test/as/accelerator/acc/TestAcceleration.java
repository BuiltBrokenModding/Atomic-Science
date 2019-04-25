package com.builtbroken.test.as.accelerator.acc;

import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorParticle;
import com.builtbroken.atomic.content.machines.accelerator.tube.powered.TileEntityAcceleratorTubePowered;
import com.builtbroken.test.as.TestHelpers;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-25.
 */
public class TestAcceleration
{
    @Test
    public void accelerate()
    {
        TileEntityAcceleratorTubePowered tube = new TileEntityAcceleratorTubePowered()
        {
            @Override
            public float getMagnetPower()
            {
                return 0.5f;
            }
        };

        tube.setPos(BlockPos.ORIGIN);
        tube.setDirection(EnumFacing.NORTH);

        AcceleratorParticle particle = new AcceleratorParticle(0, BlockPos.ORIGIN, EnumFacing.NORTH, 1);
        particle.setVelocity(0.5f);

        TestHelpers.compareFloats3Zeros(0.5f / 10f, tube.getAcceleration());

    }
}
