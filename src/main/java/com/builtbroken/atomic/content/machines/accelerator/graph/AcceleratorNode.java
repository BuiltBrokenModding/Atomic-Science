package com.builtbroken.atomic.content.machines.accelerator.graph;

import com.builtbroken.atomic.api.accelerator.AcceleratorHelpers;
import com.builtbroken.atomic.api.accelerator.IAcceleratorNode;
import com.builtbroken.atomic.api.accelerator.IAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSideType;
import com.builtbroken.atomic.lib.math.BlockPosHelpers;
import com.builtbroken.atomic.lib.math.MathConstF;
import com.builtbroken.atomic.lib.math.SideMathHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 12/15/2018.
 */
public class AcceleratorNode implements IAcceleratorNode
{

    //Connections
    private final IAcceleratorNode[] nodes = new IAcceleratorNode[6];

    //direction and connection data
    private EnumFacing facing;
    private BlockPos pos = BlockPos.ORIGIN;
    private TubeConnectionType connectionType;

    //Current network
    private AcceleratorNetwork network;

    //Used to track particles in case we break the node
    private List<AcceleratorParticle> currentParticles = new ArrayList(3);

    public int turnIndex = 0;

    public Consumer<AcceleratorParticle> onExitCallback;
    public Consumer<AcceleratorParticle> onEnterCallback;

    public void setNetwork(AcceleratorNetwork network)
    {
        this.network = network;
    }

    public AcceleratorNetwork getNetwork()
    {
        return network;
    }

    @Override
    public void updateConnections(IBlockAccess world)
    {
        //Find tubes
        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            final BlockPos sidePos = pos.offset(facing);
            final TileEntity tileEntity = world.getTileEntity(sidePos);
            final IAcceleratorTube tube = AcceleratorHelpers.getAcceleratorTube(tileEntity, facing.getOpposite());
            if (tube == null && getNode(facing) != null)
            {
                getNetwork().destroy();
                return;
            }
            else if (tube != null)
            {
                nodes[facing.ordinal()] = tube.getNode();
            }
        }

        //Map connections
        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            IAcceleratorNode node = getNode(facing);
            if (node != null)
            {
                if (canConnectToTubeOnSide(TubeSide.getSideFacingOut(getDirection(), facing)))
                {
                    connect(node, facing);
                }
                else
                {
                    nodes[facing.ordinal()] = null;
                }
            }
        }
    }

    /**
     * Connect a node to this node
     *
     * @param acceleratorNode
     */
    public void connect(IAcceleratorNode acceleratorNode, EnumFacing facing)
    {
        if (acceleratorNode != null && facing != null)
        {
            final int index = facing.ordinal();
            final int indexInv = facing.getOpposite().ordinal();

            //Check our connection
            if (getNodes()[index] != acceleratorNode)
            {
                getNodes()[index] = acceleratorNode; //TODO add set side
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
     * Gets the expect connection state based on our relation
     * to the tube connected to our side.
     * <p>
     * If we have a tube entering from our left. It will
     * tell us that its side is an exit. To our tube this
     * means we expect to see particles enter from that
     * left.
     *
     * @param localSide - side of our tube
     * @return expected state for our side
     */
    public TubeSideType getConnectedTubeState(TubeSide localSide)
    {
        //Get tube on side
        final EnumFacing facingSide = localSide.getFacing(getDirection());
        final IAcceleratorNode tube = getNode(facingSide);
        if (tube != null)
        {
            return tube.getConnectionState(facingSide.getOpposite()).getOpposite();
        }
        return TubeSideType.NONE;
    }

    protected IAcceleratorNode getNode(EnumFacing side)
    {
        return nodes[side.ordinal()];
    }

    /**
     * Checks if our current {@link #getConnectionType()} can support a
     * connection on the given side
     *
     * @param localSide - localized side based on facing of the tube
     * @return true if can connect, false if can't
     */
    public boolean canConnectToTubeOnSide(TubeSide localSide)
    {
        final TubeSideType state = getConnectedTubeState(localSide);
        return state != TubeSideType.NONE && getConnectionType().getTypeForSide(localSide) == state;
    }

    /**
     * Sets the data of the node
     */
    public AcceleratorNode setData(BlockPos pos, EnumFacing facing, TubeConnectionType type)
    {
        setPos(pos);
        setDirection(facing);
        setConnectionType(type);
        return this;
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
            doTurn(particle);

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

    /**
     * Called to do the turn for the particle.
     *
     * @param particle
     */
    public void doTurn(AcceleratorParticle particle)
    {
        //Set turn
        particle.setMoveDirection(getExpectedTurnResult(particle));

        //Increment index for turn
        incrementTurnIndex();
    }

    /**
     * Gets the current expect turn. Will not change unless conditions
     * for the turn change. This includes the tube alterating turns
     * and a particle takes a turn. As well properties on the particle
     * changing such as energy and speed.
     *
     * @param particle
     * @return expected turn
     */
    public EnumFacing getExpectedTurnResult(AcceleratorParticle particle)
    {
        //TODO add advanced logic callback for tube
        if (getPossibleExitCount() > 1)
        {
            final TubeSide side = getConnectionType().outputSides.get(turnIndex);
            return side.getFacing(facing);
        }
        return facing;
    }

    private int incrementTurnIndex()
    {
        //Increase
        turnIndex += 1;

        //Loop around
        if (turnIndex >= getPossibleExitCount() || turnIndex < 0)
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
        return side == null || side == TubeSide.CENTER || getConnectionType().getTypeForSide(side) != TubeSideType.NONE;
    }

    private TubeSide getSideFacingOut(EnumFacing side)
    {
        return TubeSide.getSideFacingOut(facing, side);
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
    protected void moveToNextNode(AcceleratorParticle particle, IAcceleratorNode node)
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
    public IAcceleratorNode[] getNodes()
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

        if (onEnterCallback != null)
        {
            onEnterCallback.accept(particle);
        }
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

        if (onExitCallback != null)
        {
            onExitCallback.accept(particle);
        }
    }

    @Override
    public EnumFacing getDirection()
    {
        return facing;
    }

    public void setDirection(EnumFacing facing)
    {
        this.facing = facing;
    }

    /**
     * Position of the node in world
     *
     * @return
     */
    @Override
    public BlockPos getPos()
    {
        return pos;
    }

    public void setPos(BlockPos pos)
    {
        this.pos = pos;
    }

    /**
     * Type of node, combined with  {@link #getDirection()} to
     * figure out the path of the particles in the node.
     *
     * @return
     */
    @Override
    public TubeConnectionType getConnectionType()
    {
        return connectionType;
    }

    public void setConnectionType(TubeConnectionType type)
    {
        this.connectionType = type;
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
        else if (object instanceof IAcceleratorNode)
        {
            return getPos().equals(((IAcceleratorNode) object).getPos());
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "AcceleratorNode[" + pos + ", " + facing + ", " + connectionType + "]";
    }
}

