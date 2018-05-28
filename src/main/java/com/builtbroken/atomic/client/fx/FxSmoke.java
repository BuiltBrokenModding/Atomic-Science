package com.builtbroken.atomic.client.fx;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.world.World;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class FxSmoke extends EntitySmokeFX
{
    double accelerationY = 0.004D;

    public FxSmoke(World world, double x, double y, double z, double vx, double vy, double vz)
    {
        super(world, x, y, z, vx, vy, vz);
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
            this.setDead();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);

        if (accelerationY != 0)
        {
            this.motionY += accelerationY;
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);

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