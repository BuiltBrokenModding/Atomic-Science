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

    public static final float ZERO = 0.001f; //WE consider anything under zero due to precision errors

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

        final EnumFacing direction = getDirection();

        if (getConnectionType() == AcceleratorConnectionType.NORMAL)
        {
            //Force direction
            particle.setMoveDirection(direction); //TODO consider allowing opposite directions

            //Get remaining distance til end
            final float remaining = remainingDistance(deltaX, deltaZ, 0.5f, direction);

            //do move
            return move(particle, direction, remaining, distanceToMove);
        }
        else if (getConnectionType() == AcceleratorConnectionType.CORNER_LEFT
                || getConnectionType() == AcceleratorConnectionType.CORNER_RIGHT)
        {
            //Direction of movement not face
            //      north facing turn would have incoming from east on its west face
            //      inverse for right corner
            final EnumFacing incomingDirection = getConnectionType() == AcceleratorConnectionType.CORNER_LEFT
                    ? direction.rotateY()
                    : direction.rotateY().getOpposite();

            //If incoming, move towards center
            if (particle.getMoveDirection() == incomingDirection)
            {
                //Get remaining distance til center
                final float remaining = remainingDistanceCenter(deltaX, deltaZ, incomingDirection);

                //Turn
                if (remaining <= ZERO)
                {
                    //Center particle to avoid it being slightly off center
                    particle.setPos(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);

                    //Update facing
                    particle.setMoveDirection(direction);

                    //Call move again with new facing
                    return move(particle, distanceToMove);
                }
                //Move towards center
                else
                {
                    return move(particle, incomingDirection, remaining, distanceToMove);
                }
            }
            //If same direction, move towards edge
            else if (particle.getMoveDirection() == direction)
            {
                //Get remaining distance til end
                final float remaining = remainingDistance(deltaX, deltaZ, 0.5f, direction);

                //do move
                return move(particle, direction, remaining, distanceToMove);
            }
            //Shouldn't happen
            else
            {
                System.out.println("Invalid particle in left turn: " + particle);
                particle.setCurrentNode(null); //TODO destroy as this means a particle entered incorrectly
            }
        }
        return 0;
    }

    private float move(AcceleratorParticle particle, EnumFacing direction, float remaining, float distanceToMove)
    {
        //calculate actual move, we are limited by tube size for now
        float moveAmount = Math.min(remaining, distanceToMove);

        //If less then cut off, switch tubes
        if (moveAmount <= ZERO)
        {
            moveToNextNode(particle, getNodes()[direction.ordinal()]);
        }
        //Else, move by direction
        else
        {
            particle.move(moveAmount, direction);
        }
        return moveAmount;
    }

    private float remainingDistance(float deltaX, float deltaZ, float goal, EnumFacing direction)
    {
        switch (direction)
        {
            //-z
            case NORTH:
                return Math.max(0, goal + deltaZ);
            //+X
            case EAST:
                return Math.max(0, goal - deltaX);
            //+Z
            case SOUTH:
                return Math.max(0, goal - deltaZ);
            //-X
            case WEST:
                return Math.max(0, goal + deltaX);
        }
        return 0;
    }

    private float remainingDistanceCenter(float deltaX, float deltaZ, EnumFacing direction)
    {
        float delta;
        switch (direction)
        {
            //z
            case NORTH:
            case SOUTH:
                delta = deltaZ;
                break;
            //x
            case EAST:
            case WEST:
                delta = deltaX;
                break;
            default:
                delta = 0;
        }
        return Math.abs(delta);
    }

    /**
     * Called to move to the next tube
     *
     * @param particle - particle to move
     * @param node     - next node, can be null if there is no next
     */
    protected void moveToNextNode(AcceleratorParticle particle, AcceleratorNode node)
    {
        onParticleExit(particle);
        if (node != null)
        {
            node.onParticleEnter(particle);
        }
        else
        {
            //TODO exit tube into the world or explode if its a wall
        }
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

    @Override
    public String toString()
    {
        return "AcceleratorNode[" + pos + ", " + direction + ", " + connectionType + "]";
    }
}

