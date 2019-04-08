package com.builtbroken.atomic.content.machines.accelerator.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2019.
 */
public class AcceleratorWorld
{
    public final List<AcceleratorParticle> particles = new ArrayList();
    public final List<AcceleratorNetwork> networks = new ArrayList();

    public void save() {
        //TODO save networks
        //TODO save particles
    }

    public void load() {

    }
}
