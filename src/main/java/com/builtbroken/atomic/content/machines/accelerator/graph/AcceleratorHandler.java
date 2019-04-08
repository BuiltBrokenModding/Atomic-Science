package com.builtbroken.atomic.content.machines.accelerator.graph;

import net.minecraft.world.World;

import java.util.HashMap;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2019.
 */
public class AcceleratorHandler
{
    private static final HashMap<Integer, AcceleratorWorld> dimToAcceleratorWorld = new HashMap();

    public AcceleratorWorld get(World world)
    {
        return get(world.provider.getDimension());
    }

    public AcceleratorWorld get(int dim)
    {
        return dimToAcceleratorWorld.get(dim);
    }
}
