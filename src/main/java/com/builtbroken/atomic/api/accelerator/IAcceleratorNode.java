package com.builtbroken.atomic.api.accelerator;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSideType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNetwork;
import com.builtbroken.atomic.content.machines.accelerator.particle.AcceleratorParticle;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Node of the accelerator network graph
 * <p>
 * Most of the time a node will be a tube in the network.
 * However, it also includes exit and entry points. Which have
 * slightly different logic then a standard node.
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 4/20/2019.
 */
public interface IAcceleratorNode extends IAcceleratorComponent
{

    /**
     * Gets the connected nodes as an array of {@link net.minecraft.util.EnumFacing}
     *
     * @return array of connections to other nodes
     */
    IAcceleratorNode[] getNodes();

    /**
     * Called each update tick of the network.
     * <p>
     * In a normal node this will handle
     * movement of the particles {@link #getParticles()}
     * as well introducing new particles that were added
     * the tick before into the tube.
     *
     * @param world
     */
    void update(World world, int tick);

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
     * Called when the network is destroyed
     */
    default void onNetworkRemoved()
    {

    }

    /**
     * Network this node is contained as part
     *
     * @return
     */
    AcceleratorNetwork getNetwork(); //TODO interface for network

    /**
     * Particles current present in the tube.
     * <p>
     * This is not a complete list as newly
     * added particles will wait until next
     * tick before being added to avoid concurrent
     * modification errors.
     *
     * @return list of particles, do not modify
     */
    List<AcceleratorParticle> getParticles();

    /**
     * Called to update connections to nearby tubes
     *
     * @param world - world to access
     * @return true if connection state changed
     */
    boolean updateConnections(IBlockAccess world);

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
     * Direction the tube is facing
     *
     * @return
     */
    EnumFacing getDirection();

    /**
     * Gets the connection layout of the tube
     *
     * @return
     */
    TubeConnectionType getConnectionType();


    /**
     * Gets the connection state for the side
     *
     * @param facing - side of the block
     * @return state
     */
    default TubeSideType getConnectionState(EnumFacing facing)
    {
        return getConnectionState(TubeSide.getSideFacingOut(getDirection(), facing));
    }

    default TubeSideType getConnectionState(TubeSide side)
    {
        return getConnectionType().getTypeForSide(side);
    }

}
