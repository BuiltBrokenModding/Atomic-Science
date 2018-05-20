package com.builtbroken.atomic.content.machines.power;

import com.builtbroken.atomic.content.machines.TileEntityMachine;
import com.builtbroken.atomic.content.machines.TileEntityPowerInvMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public class TileEntityPowerBus extends TileEntityMachine
{
    private PowerBusNetwork powerNetwork;
    private TileEntityPowerInvMachine[] connections = new TileEntityPowerInvMachine[6];

    public void setNetwork(PowerBusNetwork network)
    {
        this.powerNetwork = network;
    }

    @Override
    protected void firstTick()
    {
        super.firstTick();
        checkForWires();
        checkConnections(false);
    }

    @Override
    public void update(int ticks)
    {
        super.update(ticks);
        if (ticks % 20 == 0)
        {
            checkConnections(false);
        }
    }

    protected void checkConnections(boolean clear)
    {
        TileEntityPowerInvMachine[] newConnections = new TileEntityPowerInvMachine[6];
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            int x = xCoord + direction.offsetX;
            int y = yCoord + direction.offsetY;
            int z = zCoord + direction.offsetZ;

            TileEntity tile = world().getTileEntity(x, y, z);
            if (tile instanceof TileEntityPowerInvMachine)
            {
                newConnections[direction.ordinal()] = (TileEntityPowerInvMachine) tile;
            }
        }

        for (int i = 0; i < 6; i++)
        {
            TileEntityPowerInvMachine newConnection = newConnections[i];
            if (!clear)
            {
                TileEntityPowerInvMachine oldConnection = getConnections()[i];
                if (newConnection != oldConnection)
                {
                    if (oldConnection != null)
                    {
                        getPowerNetwork().removeConnection(oldConnection);
                    }

                    if (newConnection != null)
                    {
                        getPowerNetwork().addConnection(newConnection);
                    }
                }
            }
            else if (newConnection != null)
            {
                getPowerNetwork().addConnection(newConnection);
            }
            getConnections()[i] = newConnection;
        }
    }

    protected void checkForWires()
    {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            int x = xCoord + direction.offsetX;
            int y = yCoord + direction.offsetY;
            int z = zCoord + direction.offsetZ;

            TileEntity tile = world().getTileEntity(x, y, z);
            if (tile instanceof TileEntityPowerBus)
            {
                if (powerNetwork == null)
                {
                    ((TileEntityPowerBus) tile).getPowerNetwork().addWire(this);
                }
                else
                {
                    getPowerNetwork().merge(((TileEntityPowerBus) tile).getPowerNetwork());
                }
            }
        }
    }

    public PowerBusNetwork getPowerNetwork()
    {
        if (powerNetwork == null)
        {
            powerNetwork = new PowerBusNetwork();
            powerNetwork.addWire(this);
        }
        return powerNetwork;
    }

    public TileEntityPowerInvMachine[] getConnections()
    {
        return connections;
    }
}
