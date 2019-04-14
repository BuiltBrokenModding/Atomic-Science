package com.builtbroken.atomic.content.machines.accelerator.graph;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSideType;
import com.builtbroken.atomic.content.machines.accelerator.tube.TileEntityAcceleratorTube;
import com.builtbroken.atomic.lib.math.BlockPosHelpers;
import com.builtbroken.atomic.lib.math.MathConstF;
import com.builtbroken.atomic.lib.math.SideMathHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

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
    private EnumFacing facing;
    private BlockPos pos;
    private TubeConnectionType connectionType;

    //Current network
    private AcceleratorNetwork network;

    //Reference to host, used for realtime data
    private WeakReference<TileEntityAcceleratorTube> host;

    //Used to track particles in case we break the node
    private List<AcceleratorParticle> currentParticles = new ArrayList(3);

    private int turnIndex = 0;

    public AcceleratorNode(TileEntityAcceleratorTube host)
    {
        this(host.getPos(), host.getDirection(), host.getConnectionType());
        this.host = new WeakReference(host);

    }

    public AcceleratorNode(BlockPos pos, EnumFacing facing, TubeConnectionType connectionType)
    {
        this.pos = pos;
        this.facing = facing;
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

    public void checkConnections(IBlockAccess world)
    {
        boolean destroyNetwork = false;
        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            final BlockPos sidePos = pos.offset(facing);
            final TileEntity tileEntity = world.getTileEntity(sidePos);
            if (tileEntity instanceof TileEntityAcceleratorTube)
            {
                connect(((TileEntityAcceleratorTube) tileEntity).acceleratorNode, facing);
            }
            else if (nodes[facing.ordinal()] != null)
            {
                //Network is likely invalid so rebuild
                destroyNetwork = true;

                //Clear connections, pathing will likely fix connections but still useful
                nodes[facing.ordinal()].nodes[facing.getOpposite().ordinal()] = null;
                nodes[facing.ordinal()] = null;
            }
        }

        //Clear network
        if (destroyNetwork && getNetwork() != null)
        {
            getNetwork().destroy();
        }
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
                //TODO validate network
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
            this.facing = host.getDirection();
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
        final float deltaX = BlockPosHelpers.getCenterDeltaX(getPos(), particle);
        final float deltaZ = BlockPosHelpers.getCenterDeltaZ(getPos(), particle);

        final EnumFacing moveDir = particle.getMoveDirection();
        final EnumFacing containingDir = SideMathHelper.containingSide(deltaX, 0, deltaZ);

        final TubeSide movingTowardsSide = getSideFacingOut(moveDir);
        final TubeSide containingSide = getSideFacingOut(containingDir);

        //Valid we can have a particle on the side
        if (!isValidSideForParticle(containingSide))
        {
            moveToNextNode(particle, null);
            return 0;
        }

        //At center
        if (containingSide == TubeSide.CENTER)
        {
            //Center particle to avoid it being slightly off center
            BlockPosHelpers.center(getPos(), (x, y, z) -> particle.setPos(x, y, z));

            //Update facing
            particle.setMoveDirection(getTurnDirection(particle));

            //Move forward
            return moveForward(particle, deltaX, deltaZ, distanceToMove);
        }
        //Exiting tube
        else if (getConnectionType().getTypeForSide(movingTowardsSide) == TubeSideType.EXIT)
        {
            return moveForward(particle, deltaX, deltaZ, distanceToMove);
        }
        //Entering tube
        else if (getConnectionType().getTypeForSide(containingSide) == TubeSideType.ENTER)
        {
            return moveToCenter(particle, deltaX, deltaZ, distanceToMove);
        }
        //Should never happen
        else
        {
            System.out.println(this + " - Invalid particle direction combo: " + particle);
            particle.setCurrentNode(null);
            //TODO destroy tube (or cause damage) as this means a particle entered incorrectly hitting the tube wall
        }
        return 0;
    }

    private EnumFacing getTurnDirection(AcceleratorParticle particle)
    {
        //TODO add advanced logic callback for tube
        if (getPossibleExitCount() > 1)
        {
            TubeSide side = getConnectionType().outputSides.get(nextTurnIndex());
            return side.getFacing(facing);
        }
        return facing;
    }

    private int nextTurnIndex()
    {
        turnIndex += 1;
        if(turnIndex >= getPossibleExitCount() || turnIndex < 0)
        {
            turnIndex = 0;
        }
        return turnIndex;
    }

    private int getPossibleExitCount()
    {
        return getConnectionType().outputSides.size();
    }

    private boolean isValidSideForParticle(TubeSide side)
    {
        return side == null || getConnectionType().getTypeForSide(side) != TubeSideType.NONE;
    }

    private TubeSide getSideFacingOut(EnumFacing side)
    {
        if (side == null)
        {
            return TubeSide.CENTER;
        }
        else if (side == facing)
        {
            return TubeSide.FRONT;
        }
        else if (side == facing.getOpposite())
        {
            return TubeSide.BACK;
        }
        else if (facing.rotateY().getOpposite() == side)
        {
            return TubeSide.LEFT;
        }
        else if (facing.rotateY() == side)
        {
            return TubeSide.RIGHT;
        }
        return TubeSide.CENTER;
    }

    private float moveToCenter(AcceleratorParticle particle, float deltaX, float deltaZ, float distanceToMove)
    {
        //Get remaining distance til center
        final float remaining = SideMathHelper.remainingDistanceCenter(deltaX, deltaZ, particle.getMoveDirection());

        return move(particle, particle.getMoveDirection(), remaining, distanceToMove);
    }

    private float moveForward(AcceleratorParticle particle, float deltaX, float deltaZ, float distanceToMove)
    {
        //Get remaining distance til end
        final float remaining = SideMathHelper.remainingDistanceToSide(deltaX, deltaZ, particle.getMoveDirection());

        //Do move
        return move(particle, particle.getMoveDirection(), remaining, distanceToMove);
    }

    private float move(AcceleratorParticle particle, EnumFacing direction, float remaining, float distanceToMove)
    {
        //calculate actual move, we are limited by tube size for now
        float moveAmount = Math.min(remaining, distanceToMove);

        //If less then cut off, switch tubes
        if (moveAmount <= MathConstF.ZERO_CUT)
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
        return facing;
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
    public TubeConnectionType getConnectionType()
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
        return "AcceleratorNode[" + pos + ", " + facing + ", " + connectionType + "]";
    }
}

