package com.builtbroken.atomic.content.machines.accelerator.graph;

import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2019.
 */
public class AcceleratorWorld
{

    public final int dim;

    public final List<AcceleratorParticle> particles = new ArrayList();
    public final List<AcceleratorNetwork> networks = new ArrayList();

    private int _tick = 0;

    private final List<AcceleratorParticle> removeList = new ArrayList();

    public AcceleratorWorld(int dimension)
    {
        this.dim = dimension;
    }

    public void save()
    {
        //TODO save networks
        //TODO save particles, make sure to remove accelerator gun saving when implemented
    }

    public void load()
    {

    }

    /**
     * Called each tick of the world
     *
     * @param world
     */
    public void update(World world)
    {
        final Iterator<AcceleratorParticle> iterator = particles.iterator();
        while (iterator.hasNext())
        {
            final AcceleratorParticle particle = iterator.next();
            if (particle.isInvalid())
            {
                iterator.remove();
                System.out.println("Removed particle: " + particle);
            }
            else
            {
                particle.update(_tick);
            }
        }
    }

    /**
     * Called when the world unloads
     *
     * @param world
     */
    public void unload(World world)
    {
        particles.clear(); //These are saved by accelerator guns currently
        networks.clear();
    }
}
