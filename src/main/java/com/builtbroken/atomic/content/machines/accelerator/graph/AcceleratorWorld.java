package com.builtbroken.atomic.content.machines.accelerator.graph;

import net.minecraft.world.World;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2019.
 */
public class AcceleratorWorld
{
    public final int dim;

    public final HashMap<UUID, AcceleratorNetwork> networks = new HashMap();

    private final Queue<AcceleratorNetwork> addList = new LinkedList();

    private int _tick = 0;

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
        while(addList.peek() != null)
        {
            AcceleratorNetwork network = addList.poll();
            if(!network.isDead() && !networks.containsKey(network.uuid))
            {
                networks.put(network.uuid, network); //TODO join if networks share same ID but are different
            }
        }
        networks.values().stream().filter(network -> network.isDead()).collect(Collectors.toList()).forEach(this::doRemove);
        networks.values().forEach(network -> network.update(world, _tick));
    }

    private void doRemove(AcceleratorNetwork network)
    {
        network.onNetworkRemoved();
        networks.remove(network.uuid);
    }

    /**
     * Called when the world unloads
     *
     * @param world
     */
    public void unload(World world)
    {
        networks.clear();
    }

    public void add(AcceleratorNetwork acceleratorNetwork)
    {
        addList.offer(acceleratorNetwork);
    }
}
