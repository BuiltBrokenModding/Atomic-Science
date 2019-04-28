package com.builtbroken.atomic.client.particles;

import net.minecraft.item.ItemStack;

import java.util.UUID;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/14/2019.
 */
public class AcceleratorParticleRenderData
{
    public UUID ID;
    public int dim;
    public float cx, cy, cz;
    public float lx, ly, lz;
    public float tx, ty, tz;

    public ItemStack renderItem = ItemStack.EMPTY;

    public float energy;
    public float speed;

    public float yawRotation = 0;
    public float pitchRotation = 0;

    public int keepAlive = 0;
}
