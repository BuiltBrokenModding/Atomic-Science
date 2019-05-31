package com.builtbroken.atomic.content.machines.accelerator.graph;

import com.builtbroken.atomic.api.accelerator.AcceleratorHelpers;
import com.builtbroken.atomic.api.accelerator.IAcceleratorNode;
import com.builtbroken.atomic.api.accelerator.IAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSideType;
import com.builtbroken.atomic.content.machines.accelerator.particle.AcceleratorParticle;
import com.builtbroken.atomic.lib.CallTrigger;
import com.builtbroken.atomic.lib.math.BlockPosHelpers;
import com.builtbroken.atomic.lib.math.MathConstF;
import com.builtbroken.atomic.lib.math.SideMathHelper;
import com.builtbroken.atomic.network.netty.PacketSystem;
import com.builtbroken.atomic.network.packet.client.PacketAcceleratorParticleSync;
import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Created by Dark(DarkGuardsman, Robert) on 12/15/2018.
 */
public class AcceleratorNode extends AcceleratorComponent implements IAcceleratorNode
{
    public static final String NBT_TURN_INDEX = "turn_index";
    public static final String NBT_PARTICLES = "particles";

    //Connections
    private final IAcceleratorNode[] nodes = new IAcceleratorNode[6];

    //direction and connection data
    private TubeConnectionType connectionType;

    //Current network
    private AcceleratorNetwork network;

    //Used to track particles in case we break the node
    private List<AcceleratorParticle> currentParticles = new ArrayList(3);
    private final Queue<AcceleratorParticle> newParticles = new LinkedList();

    public int turnIndex = 0;

    public Consumer<AcceleratorParticle> onExitCallback;
    public Consumer<AcceleratorParticle> onLeaveCallback;
    public Consumer<AcceleratorParticle> onEnterCallback;
    public Consumer<AcceleratorParticle> onMoveCallback;
    public BiFunction<AcceleratorParticle, ImmutableList<TubeSide>, TubeSide> turnController;

    public IAcceleratorTube host;

    public AcceleratorNode(IAcceleratorTube tube) //TODO convert to host interface
    {
        super(() -> tube.dim());
        this.host = tube;
        onLeaveCallback = (particle) -> AcceleratorHandler.spawnParticleInWorld(particle);
    }

    public void setNetwork(AcceleratorNetwork network)
    {
        this.network = network;
    }

    public AcceleratorNetwork getNetwork()
    {
        return network;
    }

    @Override
    public List<AcceleratorParticle> getParticles()
    {
        return currentParticles;
    }

    public void add(AcceleratorParticle particle)
    {
        currentParticles.add(particle);
    }

    @Override
    public void update(World world, int tick)
    {
        if (newParticles.peek() != null)
        {
            host.markDirty();
            //Particles added, prevents concurrent errors
            do
            {
                AcceleratorParticle particle = newParticles.poll();
                particle.setCurrentNode(this);
                currentParticles.add(particle);
            }
            while (newParticles.peek() != null);
        }

        //Only loop if we have something
        if (currentParticles.size() > 0)
        {
            host.markDirty();

            //Update particles
            final Iterator<AcceleratorParticle> iterator = currentParticles.iterator();
            while (iterator.hasNext())
            {
                final AcceleratorParticle particle = iterator.next();
                if (particle.isDead() || particle.getCurrentNode() != this)
                {
                    iterator.remove();
                }
                else
                {
                    particle.update(tick);
                }
            }

            //Network handling
            currentParticles.forEach(acceleratorParticle -> {

                //System.out.println(acceleratorParticle);

                PacketAcceleratorParticleSync packet = new PacketAcceleratorParticleSync(acceleratorParticle); //TODO implement flywheel pattern

                PacketSystem.INSTANCE.sendToAllAround(packet,
                        new NetworkRegistry.TargetPoint(world.provider.getDimension(),
                                acceleratorParticle.x(), acceleratorParticle.y(), acceleratorParticle.z(),
                                30));
            });
        }
    }

    @Override
    public boolean updateConnections(IBlockAccess world)
    {
        boolean connectionStateChanged = false;
        //Find tubes
        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            final IAcceleratorNode node = getNode(world, facing);

            //Lost a connection
            if (node == null && getNode(facing) != null)
            {
                connectionStateChanged = true;
            }
            else if (node != null)
            {
                //Gained a connection
                if (getNode(facing) == null)
                {
                    connectionStateChanged = true;
                }
                nodes[facing.ordinal()] = node;
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

        return connectionStateChanged;
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
                    setNetwork(new AcceleratorNetwork(dim()));
                    getNetwork().connect(this);
                    getNetwork().connect(acceleratorNode);
                    getNetwork().registerNetwork();
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
    public TubeSideType getConnectedTubeState(IBlockAccess access, TubeSide localSide)
    {
        //Get tube on side
        final EnumFacing facingSide = localSide.getFacing(getDirection());
        final IAcceleratorNode tube = access == null ? getNode(facingSide) : getNode(access, facingSide);
        if (tube != null)
        {
            return tube.getConnectionState(facingSide.getOpposite()).getOpposite();
        }
        return TubeSideType.NONE;
    }

    public IAcceleratorNode getNode(IBlockAccess access, EnumFacing facing)
    {
        final TileEntity tileEntity = access.getTileEntity(getPos().offset(facing));
        final IAcceleratorTube tube = AcceleratorHelpers.getAcceleratorTube(tileEntity, facing.getOpposite());
        if (tube != null)
        {
            return tube.getNode();
        }
        return null;
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
        final TubeSideType state = getConnectedTubeState(null, localSide);
        return state != TubeSideType.NONE && getConnectionType().getTypeForSide(localSide) == state;
    }

    /**
     * Sets the data of the node
     */
    @Deprecated //TODO being moved to host
    public AcceleratorNode setData(BlockPos pos, EnumFacing facing, TubeConnectionType type)
    {
        setPos(pos);
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

        //final TubeSide movingTowardsSide = getSideFacingOut(moveDir);
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
        else if (getConnectionType().getTypeForSide(containingSide) == TubeSideType.EXIT)
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
        if (getPossibleExitCount() > 1)
        {
            //Advanced logic controller
            if (turnController != null)
            {
                final TubeSide side = turnController.apply(particle, getConnectionType().outputSides);
                if (side != null)
                {
                    return side.getFacing(host.getDirection());
                }
            }

            //Default index picker
            final TubeSide side = getConnectionType().outputSides.get(turnIndex);
            return side.getFacing(host.getDirection());
        }
        return host.getDirection();
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
        return TubeSide.getSideFacingOut(host.getDirection(), side);
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
        if (onMoveCallback != null)
        {
            onMoveCallback.accept(particle);
        }

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
            if (onLeaveCallback != null)
            {
                onLeaveCallback.accept(particle);
            }
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
        addParticle(particle);

        if (onEnterCallback != null)
        {
            onEnterCallback.accept(particle);
        }
    }

    public void addParticle(AcceleratorParticle particle)
    {
        if (particle != null)
        {
            newParticles.offer(particle);
        }
    }

    /**
     * Called when a particle exits the node
     *
     * @param particle
     */
    public void onParticleExit(AcceleratorParticle particle)
    {
        particle.setCurrentNode(null);
        if (onExitCallback != null)
        {
            onExitCallback.accept(particle);
        }
    }

    @Override
    public EnumFacing getDirection()
    {
        return host.getDirection();
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


    @Override
    public NBTTagCompound save(NBTTagCompound nbt)
    {
        nbt.setInteger(NBT_TURN_INDEX, turnIndex);
        if (!currentParticles.isEmpty())
        {
            final NBTTagList list = new NBTTagList();
            for (AcceleratorParticle particle : currentParticles)
            {
                if (!particle.isDead())
                {
                    final NBTTagCompound save = new NBTTagCompound();
                    particle.save(save);
                    list.appendTag(save);
                }
            }
            nbt.setTag(NBT_PARTICLES, list);
        }
        return nbt;
    }

    @Override
    public void load(NBTTagCompound nbt)
    {
        turnIndex = nbt.getInteger(NBT_TURN_INDEX);
        if (nbt.hasKey(NBT_PARTICLES))
        {
            currentParticles.clear();
            final NBTTagList list = nbt.getTagList(NBT_PARTICLES, 10);
            for (int i = 0; i < list.tagCount(); i++)
            {
                final NBTTagCompound save = list.getCompoundTagAt(i);
                addParticle(new AcceleratorParticle(save));
            }
        }
    }

    @Override
    public boolean isDead()
    {
        return host == null || host.isDead();
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
        return "AcceleratorNode[" + getPos() + ", " + host.getDirection() + ", " + connectionType + "]";
    }

}

