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

    //Connections
    private final AcceleratorNode[] nodes = new AcceleratorNode[6];

    //direction and connection data
    private EnumFacing direction;
    private BlockPos pos;
    private AcceleratorConnectionType connectionType;

    //Current network
    private AcceleratorNetwork network;

    //Reference to host, used for realtime data
    private WeakReference<TileEntityAcceleratorTube> host;

    //Used to track particles in case we break the node
    private List<AcceleratorParticle> currentParticles = new ArrayList(3);

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
    public void connect(AcceleratorNode acceleratorNode, EnumFacing facing)
    {
        if (acceleratorNode != null && facing != null)
        {
            final int index = facing.ordinal();
            final int indexInv = facing.getOpposite().ordinal();

            //Check our connection
            if (getNodes()[index] != acceleratorNode)
            {
                getNodes()[index] = acceleratorNode;
            }

            //Check other connection
            if (acceleratorNode.getNodes()[indexInv] != this)
            {
                acceleratorNode.getNodes()[indexInv] = this;
            }

            //Check network
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

    /**
     * Called to update the internal cache of the node. This
     * is often called when the tube is updated in a way
     * that it's state changes.
     */
    public void updateCache()
    {
        final TileEntityAcceleratorTube host = getHost();
        if (host != null)
        {
            this.pos = host.getPos();
            this.direction = host.getDirection();
            this.connectionType = host.getConnectionType();
        }
    }

    /**
     * Called to move the particle in the accelerator
     *
     * @param particle
     */
    public float move(AcceleratorParticle particle, float distanceToMove)
    {
        //Figure out relative position from center of block
        final float deltaX = particle.xf() - (getPos().getX() + 0.5f);
        //final float deltaY = particle.yf() - (getPos().getY() + 0.5f);
        final float deltaZ = particle.zf() - (getPos().getZ() + 0.5f);

        float remaining;
        float moveAmount;

        if (getConnectionType() == AcceleratorConnectionType.NORMAL)
        {
            //Force direction
            particle.setMoveDirection(getDirection());

            //Update motion
            switch (getDirection())
            {
                //-z
                case NORTH:
                    remaining = Math.max(0, 0.5f + deltaZ);
                    moveAmount = Math.min(remaining, distanceToMove);
                    if (moveAmount == 0)
                    {
                        moveToNextNode(particle, getNodes()[getDirection().ordinal()]);
                    }
                    else
                    {
                        particle.move(0, 0, -moveAmount);
                    }
                    return moveAmount;
                //+X
                case EAST:
                    remaining = Math.max(0, 0.5f - deltaX);
                    moveAmount = Math.min(remaining, distanceToMove);
                    if (moveAmount == 0)
                    {
                        moveToNextNode(particle, getNodes()[getDirection().ordinal()]);
                    }
                    else
                    {
                        particle.move(moveAmount, 0, 0);
                    }
                    return moveAmount;
                //+Z
                case SOUTH:
                    remaining = Math.max(0, 0.5f - deltaZ);
                    moveAmount = Math.min(remaining, distanceToMove);
                    if (moveAmount == 0)
                    {
                        moveToNextNode(particle, getNodes()[getDirection().ordinal()]);
                    }
                    else
                    {
                        particle.move(0, 0, moveAmount);
                    }
                    return moveAmount;
                //-X
                case WEST:
                    remaining = Math.max(0, 0.5f + deltaX);
                    moveAmount = Math.min(remaining, distanceToMove);
                    if (moveAmount == 0)
                    {
                        moveToNextNode(particle, getNodes()[getDirection().ordinal()]);
                    }
                    else
                    {
                        particle.move(-moveAmount, 0, 0);
                    }
                    return moveAmount;
            }
        }
        return 0;
    }

    protected void moveToNextNode(AcceleratorParticle particle, AcceleratorNode node)
    {
        onParticleExit(particle);
        node.onParticleEnter(particle);
    }

    /**
     * All nodes this node is connected
     *
     * @return
     */
    public AcceleratorNode[] getNodes()
    {
        return nodes;
    }

    /**
     * Called when a particle enters the node
     *
     * @param particle
     */
    public void onParticleEnter(AcceleratorParticle particle)
    {
        this.currentParticles.add(particle);
        particle.setCurrentNode(this);
        System.out.println(this + "Particle Entered: " + particle);
    }

    /**
     * Called when a particle exits the node
     *
     * @param particle
     */
    public void onParticleExit(AcceleratorParticle particle)
    {
        this.currentParticles.remove(particle);
        particle.setCurrentNode(null);
        System.out.println(this + "Particle Exited: " + particle);
    }

    /**
     * Facing direction of the node
     *
     * @return
     */
    public EnumFacing getDirection()
    {
        TileEntityAcceleratorTube host = getHost();
        if (host != null)
        {
            return host.getDirection();
        }
        return direction;
    }

    /**
     * Position of the node in world
     *
     * @return
     */
    public BlockPos getPos()
    {
        TileEntityAcceleratorTube host = getHost();
        if (host != null)
        {
            return host.getPos();
        }
        return pos;
    }

    /**
     * Type of node, combined with  {@link #getDirection()} to
     * figure out the path of the particles in the node.
     *
     * @return
     */
    public AcceleratorConnectionType getConnectionType()
    {
        TileEntityAcceleratorTube host = getHost();
        if (host != null)
        {
            return host.getConnectionType();
        }
        return connectionType;
    }

    /**
     * Actual tube that this node represents
     *
     * @return tube or null if not in world
     */
    public TileEntityAcceleratorTube getHost()
    {
        return host != null ? host.get() : null;
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

