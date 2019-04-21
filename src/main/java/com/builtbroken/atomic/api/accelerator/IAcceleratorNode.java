package com.builtbroken.atomic.api.accelerator;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNetwork;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorParticle;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/20/2019.
 */
public interface IAcceleratorNode
{
    /**
     * Gets the connected nodes as an array of {@link net.minecraft.util.EnumFacing}
     *
     * @return array of connections to other nodes
     */
    IAcceleratorNode[] getNodes();

    /**
     * Called to move the particle
     *
     * @param particle       - particle to move
     * @param distanceToMove - distance it can move
     * @return distance traveled
     */
    float move(AcceleratorParticle particle, float distanceToMove); //TODO interface for particle

    /**
     * Sets the network of the node
     *
     * @param acceleratorNetwork
     */
    void setNetwork(AcceleratorNetwork acceleratorNetwork); //TODO interface for network

    /**
     * Network this node is contained as part
     *
     * @return
     */
    AcceleratorNetwork getNetwork(); //TODO interface for network

    /**
     * Called to connect the node to the side
     *
     * @param otherNode
     * @param side
     */
    void connect(IAcceleratorNode otherNode, EnumFacing side);

    /**
     * Called when the particle enters
     *
     * @param particle
     */
    void onParticleEnter(AcceleratorParticle particle);
    //TODO interface for particle
    //TODO add side of enter

    /**
     * Called when the particle exists
     *
     * @param particle
     */
    void onParticleExit(AcceleratorParticle particle);
    //TODO interface for particle
    //TODO add side of exit

    /**
     * Location of the node in the world
     *
     * @return
     */
    BlockPos getPos();

    /**
     * Direction the tube is facing
     *
     * @return
     */
    EnumFacing getDirection();

    /**
     * Gets the connection layout of the tube
     * @return
     */
    TubeConnectionType getConnectionType();
}
