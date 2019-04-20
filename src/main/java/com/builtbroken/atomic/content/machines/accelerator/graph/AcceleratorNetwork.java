package com.builtbroken.atomic.content.machines.accelerator.graph;

import com.builtbroken.atomic.api.accelerator.AcceleratorHelpers;
import com.builtbroken.atomic.api.accelerator.IAcceleratorNode;
import com.builtbroken.atomic.api.accelerator.IAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.gun.TileEntityAcceleratorGun;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
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

    /** All nodes in the network */
    public final Set<IAcceleratorNode> nodes = new HashSet();

    /** Any guns in the network */
    public final Set<TileEntityAcceleratorGun> guns = new HashSet();

    public AcceleratorNetwork()
    {
        uuid = UUID.randomUUID();
    }

    public AcceleratorNetwork(UUID uuid) //TODO use for load
    {
        this.uuid = uuid;
    }

    /**
     * Joins two networks together
     *
     * @param network
     */
    public void join(AcceleratorNetwork network)
    {
        //Add nodes
        nodes.addAll(network.nodes);
        network.nodes.forEach(node -> node.setNetwork(this));

        //Add guns
        guns.addAll(network.guns);
        network.guns.forEach(gun -> gun.setNetwork(this));

        //Clear old
        network.clear();

        //Validate
        validate();
    }

    public void validate()
    {
        guns.removeIf(gun -> gun == null || gun.isInvalid());
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
        nodes.clear();
        guns.clear();
    }

    /**
     * Called to destroy the network, reset connections, and clear cache
     */
    public void destroy()
    {
        nodes.forEach(node -> node.setNetwork(null));
        guns.forEach(gun -> gun.setNetwork(null));
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
    public void path(World world, BlockPos start)
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
                tube.getNode().setNetwork(this);
                posToNode.put(pos, tube.getNode());

                //Get all possible directions
                for (EnumFacing facing : EnumFacing.HORIZONTALS)
                {
                    final BlockPos nextPos = pos.offset(facing);

                    //Check if is inside the map
                    if (world.isBlockLoaded(nextPos))
                    {
                        //If we have not pathed, add to path list
                        if (!pathedPositions.contains(nextPos))
                        {
                            positionsToPath.add(nextPos);
                        }
                        //If we have pathed, check for connection
                        else if (posToNode.containsKey(nextPos))
                        {
                            tube.getNode().connect(posToNode.get(nextPos), facing);
                        }
                    }
                    //If not ignore, we will handle this later
                    else
                    {
                        pathedPositions.add(nextPos);
                    }
                }
            }
        }

        //Add nodes to network
        this.nodes.addAll(posToNode.values());
    }
}
