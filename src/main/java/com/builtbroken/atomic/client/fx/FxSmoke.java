package com.builtbroken.atomic.client.fx;

import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class FxSmoke extends ParticleSmokeNormal
{
    double accelerationY = 0.004D;

    public FxSmoke(World world, double x, double y, double z, double vx, double vy, double vz)
    {
        this(world, x, y, z, vx, vy, vz, 1);
    }

    public FxSmoke(World world, double x, double y, double z, double vx, double vy, double vz, float scale)
    {
        super(world, x, y, z, vx, vy, vz, scale);
    }

    public FxSmoke setYAcceleration(double d)
    {
        this.accelerationY = d;
        return this;
    }

    public FxSmoke setColor(Color color)
    {
        this.particleRed = color.getRed() / 255f;
        this.particleGreen = color.getGreen() / 255f;
        this.particleBlue = color.getBlue() / 255f;
        return this;
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);

        if (accelerationY != 0)
        {
            this.motionY += accelerationY;
        }

        this.move(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }
}