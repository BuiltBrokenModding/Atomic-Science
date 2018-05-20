package com.builtbroken.atomic.content.machines.power;

import com.builtbroken.atomic.content.machines.TileEntityPowerInvMachine;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public class PowerBusNetwork
{
    private List<TileEntityPowerInvMachine> outputs = new ArrayList();

    private List<TileEntityPowerBus> wires = new ArrayList();

    public int addEnergy(int energy, boolean doAction)
    {
        if (outputs.size() > 0)
        {
            int energyLeft = energy;
            int tiles = outputs.size();

            for (TileEntityPowerInvMachine machine : outputs)
            {
                if (!machine.isInvalid())
                {
                    //Evenly add energy to all tiles
                    int energyToGive = (energyLeft / tiles) + (energyLeft % tiles);
                    energyLeft -= machine.addEnergy(energyToGive, doAction);
                }
            }

            return energy - energyLeft;
        }
        return 0;
    }

    public void merge(PowerBusNetwork other)
    {
        if (other != this)
        {
            wires.addAll(other.wires);
            other.destroy();
            refresh();
        }
    }

    public void destroy()
    {
        wires.forEach(wire -> wire.setNetwork(null));
        outputs.clear();
        wires.clear();
    }

    protected void refresh()
    {
        //Make sure all wires are set the right network
        wires.forEach(wire -> wire.setNetwork(this));

        //Find all connections
        outputs.clear();
        for (TileEntityPowerBus bus : wires)
        {
            bus.checkConnections(true);
        }
    }

    protected void addConnection(TileEntityPowerInvMachine machine)
    {
        if (!outputs.contains(machine))
        {
            outputs.add(machine);
        }
    }

    public void removeConnection(TileEntityPowerInvMachine oldConnection)
    {
        outputs.remove(oldConnection);
    }

    public void addWire(TileEntityPowerBus wire)
    {
        wire.setNetwork(this);
    }
}
