package com.builtbroken.test.as.accelerator.acc;

import com.builtbroken.atomic.config.content.ConfigContent;
import com.builtbroken.atomic.content.machines.accelerator.particle.AcceleratorParticle;
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
            private EnumFacing direction;

            @Override
            public float getMagnetPower()
            {
                return BASE_MAGNET_POWER;
            }

            @Override
            public EnumFacing getDirection()
            {
                return direction;
            }

            @Override
            public void setDirection(EnumFacing facing)
            {
                direction = facing;
            }
        };

        tube.setPos(BlockPos.ORIGIN);
        tube.setDirection(EnumFacing.NORTH);

        AcceleratorParticle particle = new AcceleratorParticle(0, BlockPos.ORIGIN, EnumFacing.NORTH, 1);
        particle.setVelocity(0.5f);

        TestHelpers.compareFloats3Zeros(ConfigContent.ACCELERATOR.ACCELERATION_SCALE, tube.getAcceleration());

    }
}
