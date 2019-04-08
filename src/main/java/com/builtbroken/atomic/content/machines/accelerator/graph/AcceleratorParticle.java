package com.builtbroken.atomic.content.machines.accelerator.graph;

import com.builtbroken.jlib.data.vector.IPos3D;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/7/2019.
 */
public class AcceleratorParticle implements IPos3D
{

    private float speed;
    private float energy;

    private float x;
    private float y;
    private float z;

    private EnumFacing moveDirection;

    private AcceleratorNode node;

    private ItemStack itemStack = ItemStack.EMPTY;

    private boolean notInTube = false;

    public AcceleratorParticle(BlockPos start, EnumFacing moveDirection, float energy)
    {
        this.x = start.getX() + 0.5f;
        this.y = start.getY() + 0.5f;
        this.z = start.getZ() + 0.5f;
        this.moveDirection = moveDirection;
        this.energy = energy;
    }

    public void update(int worldTick)
    {
        move();
        consumeEnergy();
    }

    protected void move()
    {
        //TODO check if we need to turn
        //TODO update position
        //TODO center on tube

        //How much we can move in a single go
        float distanceToMove = speed;

        AcceleratorNode currentNode = getCurrentNode();
        if (currentNode != null)
        {
            //WE can move through several nodes, so loop until done TODO replace with path so we can % locate position and node
            while (distanceToMove > 0 && currentNode != null)
            {
                final EnumFacing prevDirection = moveDirection;

                //Move forward consuming distance
                float distanceMoved = currentNode.move(this, distanceToMove);
                distanceToMove -= distanceMoved;
                System.out.println(this + ": Has moved " + distanceMoved + " in " + currentNode);

                //Check if our direction changed
                if (prevDirection != moveDirection)
                {
                    System.out.println(this + ": Has turned from " + prevDirection + " to " + moveDirection);
                    //TODO track when we make a turn
                    //TODO send turn point to client for smooth animation
                    //      Client will need to lerp between points
                    //      A -> B -> C -> D
                    //      Each time it gets to a point it will clear the last
                    //      This mean it only has a current pos and target pos
                    //      If it has no target then its goal is the server's position
                }

                //Reset for next loop
                currentNode = getCurrentNode();
            }
        }
        else
        {
            //TODO destroy or fire off into world
            notInTube = true;
        }
    }

    public void move(float x, float y, float z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    protected void consumeEnergy()
    {
        //TODO slowly eat energy
    }

    public void setMoveDirection(EnumFacing direction)
    {
        this.moveDirection = direction;
    }

    @Override
    public double z()
    {
        return z;
    }

    @Override
    public double x()
    {
        return x;
    }

    @Override
    public double y()
    {
        return y;
    }

    @Override
    public float zf()
    {
        return z;
    }

    @Override
    public float xf()
    {
        return x;
    }

    @Override
    public float yf()
    {
        return y;
    }

    public AcceleratorNode getCurrentNode()
    {
        return node;
    }

    public AcceleratorParticle setCurrentNode(AcceleratorNode node)
    {
        this.node = node;
        return this;
    }

    public ItemStack getItem()
    {
        return itemStack;
    }

    public AcceleratorParticle setItem(ItemStack stack)
    {
        this.itemStack = stack;
        if (stack == null)
        {
            this.itemStack = ItemStack.EMPTY;
        }
        return this;
    }

    public boolean isInvalid()
    {
        return notInTube;
    }

    //TODO entity version

    @Override
    public String toString()
    {
        return "AcceleratorParticle[" + x + "," + y +","+ z + " | " + moveDirection + "]@" + hashCode();
    }
}
