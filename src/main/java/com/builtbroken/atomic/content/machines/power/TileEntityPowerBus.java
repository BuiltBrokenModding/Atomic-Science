package com.builtbroken.atomic.content.machines.power;

import com.builtbroken.atomic.content.machines.TileEntityMachine;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public class TileEntityPowerBus extends TileEntityMachine
{
    PowerBusNetwork powerNetwork;

    public void setNetwork(PowerBusNetwork network)
    {
        this.powerNetwork = network;
    }
}
