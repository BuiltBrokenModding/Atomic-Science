package com.builtbroken.atomic.client;

/**
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2019.
 */
@FunctionalInterface
public interface ParticleSpawnFunction
{
    void spawn(double x, double y, double z, double vx, double vy, double vz);
}
