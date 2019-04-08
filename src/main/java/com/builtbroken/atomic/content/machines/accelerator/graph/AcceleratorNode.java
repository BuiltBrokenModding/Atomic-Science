package com.builtbroken.atomic.content.machines.accelerator.graph;

import com.builtbroken.atomic.content.machines.accelerator.tube.AcceleratorConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.tube.TileEntityAcceleratorTube;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/15/2018.
 */
public class AcceleratorNode
{
    private final List<AcceleratorNode> nodes = new ArrayList(4);

    private EnumFacing direction;
    private BlockPos pos;
    private AcceleratorConnectionType connectionType;

    private AcceleratorNetwork network;

    private WeakReference<TileEntityAcceleratorTube> host;

    public AcceleratorNode(TileEntityAcceleratorTube host)
    {
        this(host.getPos(), host.getDirection(), host.getConnectionType());
        this.host = new WeakReference(host);

    }

    public AcceleratorNode(BlockPos pos, EnumFacing direction, AcceleratorConnectionType connectionType)
    {
        this.pos = pos;
        this.direction = direction;
        this.connectionType = connectionType;
    }

    public void setNetwork(AcceleratorNetwork network)
    {
        this.network = network;
    }

    public AcceleratorNetwork getNetwork()
    {
        return network;
    }

    /**
     * Connect a node to this node
     *
     * @param acceleratorNode
     */
    public void connect(AcceleratorNode acceleratorNode)
    {
        if (acceleratorNode != null)
        {
            if (!getNodes().contains(acceleratorNode))
            {
                getNodes().add(acceleratorNode);
            }
            if (!acceleratorNode.getNodes().contains(this))
            {
                acceleratorNode.getNodes().add(this);
            }

            if (acceleratorNode.getNetwork() == null)
            {
                if (getNetwork() == null)
                {
                    setNetwork(new AcceleratorNetwork());
                    getNetwork().connect(this);
                    getNetwork().connect(acceleratorNode);
                }
                else
                {
                    getNetwork().connect(acceleratorNode);
                }
            }
            else if (getNetwork() == null)
            {
                acceleratorNode.getNetwork().connect(this);
            }
        }
    }

    public void updateCache()
    {
        TileEntityAcceleratorTube host = getHost();
        if (host != null)
        {
            this.pos = host.getPos();
            this.direction = host.getDirection();
            this.connectionType = host.getConnectionType();
        }
    }

    public List<AcceleratorNode> getNodes()
    {
        return nodes;
    }

    public EnumFacing getDirection()
    {
        TileEntityAcceleratorTube host = getHost();
        if (host != null)
        {
            return host.getDirection();
        }
        return direction;
    }

    public BlockPos getPos()
    {
        TileEntityAcceleratorTube host = getHost();
        if (host != null)
        {
            return host.getPos();
        }
        return pos;
    }

    public AcceleratorConnectionType getConnectionType()
    {
        TileEntityAcceleratorTube host = getHost();
        if (host != null)
        {
            return host.getConnectionType();
        }
        return connectionType;
    }

    public TileEntityAcceleratorTube getHost()
    {
        return host.get();
    }

    @Override
    public int hashCode()
    {
        return getPos().hashCode();
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        else if (object instanceof AcceleratorNode)
        {
            return getPos().equals(((AcceleratorNode) object).getPos());
        }
        return false;
    }
}

