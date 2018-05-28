package com.builtbroken.atomic.client.fx;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.world.World;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class FxSmoke extends EntitySmokeFX
{
    public FxSmoke(World world, double x, double y, double z, double vx, double vy, double vz)
    {
        super(world, x, y, z, vx, vy, vz);
    }

    public FxSmoke(World world, double x, double y, double z, double vx, double vy, double vz, float scale)
    {
        super(world, x, y, z, vx, vy, vz, scale);
    }

    public FxSmoke setColor(Color color)
    {
        this.particleRed = color.getRed() / 255f;
        this.particleGreen = color.getGreen() / 255f;
        this.particleBlue = color.getBlue() / 255f;
        return this;
    }
}