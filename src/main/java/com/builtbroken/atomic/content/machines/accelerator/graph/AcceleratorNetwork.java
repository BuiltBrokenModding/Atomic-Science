package com.builtbroken.atomic.content.machines.accelerator.graph;

import com.builtbroken.atomic.api.accelerator.AcceleratorHelpers;
import com.builtbroken.atomic.api.accelerator.IAcceleratorNode;
import com.builtbroken.atomic.api.accelerator.IAcceleratorTube;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.*;

/**
 * Network of tubes that creates a network
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2019.
 */
public class AcceleratorNetwork
{
    public final UUID uuid;
    public final int dim;

    private boolean destroyNetwork = false;
    private boolean init = false;

    /**
     * All nodes in the network
     */
    private final Set<IAcceleratorNode> nodes = new HashSet();

    public AcceleratorNetwork(int dim)
    {
        this(dim, UUID.randomUUID());
    }

    public AcceleratorNetwork(int dim, UUID uuid) //TODO use for load
    {
        this.dim = dim;
        this.uuid = uuid;
    }

    public AcceleratorNetwork registerNetwork()
    {
        AcceleratorHandler.getOrCreate(dim).add(this);
        return this;
    }

    public void init(IBlockAccess world, BlockPos start)
    {
        if (!init)
        {
            init = true;
            path(world, start);
        }
    }

    /**
     * Called each tick to update the network.
     *
     * @param world
     * @param tick
     */
    public void update(World world, int tick)
    {
        if (getNodes().removeIf(node -> node.isDead()))
        {
            destroy();
        }
        else
        {
            getNodes().forEach(node -> node.update(world, tick));
        }
    }

    /**
     * Connects a node to the network
     *
     * @param acceleratorNode
     */
    public void connect(IAcceleratorNode acceleratorNode)
    {
        nodes.add(acceleratorNode);
        acceleratorNode.setNetwork(this);
    }

    /**
     * Called to clear but not destroy the network
     */
    public void clear()
    {
        getNodes().clear();
    }

    /**
     * Called to destroy the network, reset connections, and clear cache
     */
    public void destroy()
    {
        if (init)
        {
            destroyNetwork = true;
        }
    }

    /**
     * Does the actual destroy process
     */
    public void onNetworkRemoved()
    {
        getNodes().forEach(node -> node.setNetwork(null));
        getNodes().forEach(node -> node.onNetworkRemoved());
        clear();
    }

    /**
     * Trigger to find all tubes in a network. Since tubes can't
     * trigger connections on there own something has to start
     * the network pathing.
     *
     * @param world
     * @param start
     */
    public void path(IBlockAccess world, BlockPos start)
    {
        //Positions already pathed
        final Set<BlockPos> pathedPositions = new HashSet();

        //Positions to path
        final Stack<BlockPos> positionsToPath = new Stack();
        positionsToPath.push(start);

        //Map of positions to nodes
        final HashMap<BlockPos, IAcceleratorNode> posToNode = new HashMap();

        //Loop until we run out of blocks to path
        while (!positionsToPath.isEmpty())
        {
            //Get next pos
            final BlockPos pos = positionsToPath.pop();

            //Add to pathed so we don't path again
            pathedPositions.add(pos);

            //Check for tube at position
            final TileEntity tileEntity = world.getTileEntity(pos);
            final IAcceleratorTube tube = AcceleratorHelpers.getAcceleratorTube(tileEntity, null);
            if (tube != null)
            {
                //Set network and update connections
                tube.getNode().setNetwork(this);
                tube.getNode().updateConnections(world);

                //Store tube
                posToNode.put(pos, tube.getNode());

                //Get all possible directions
                for (EnumFacing facing : EnumFacing.HORIZONTALS)
                {
                    final IAcceleratorNode connection = tube.getNode().getNodes()[facing.ordinal()];
                    if (connection != null)
                    {
                        final BlockPos nextPos = connection.getPos();
                        //If we have not pathed, add to path list
                        if (!pathedPositions.contains(nextPos))
                        {
                            positionsToPath.add(nextPos);
                        }
                    }
                    else
                    {
                        pathedPositions.add(pos.offset(facing));
                    }
                }
            }
        }

        //Add nodes to network
        this.getNodes().addAll(posToNode.values());
        destroyNetwork = false; //Temp fix for connection update calling destroy
    }

    public boolean isDead()
    {
        return nodes.isEmpty() || destroyNetwork;
    }

    public Set<IAcceleratorNode> getNodes()
    {
        return nodes;
    }
}
