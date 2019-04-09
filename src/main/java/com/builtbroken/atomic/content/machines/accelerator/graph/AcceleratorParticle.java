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

    //How far in meters/blocks can be move per tick of the game
    private float movementPerTick;

    //Energy stored
    private float energy;

    //Position data
    private float x;
    private float y;
    private float z;

    //Movement direction
    private EnumFacing moveDirection;

    //Current pathing node, can be null if we are not in a accelerator
    private AcceleratorNode node;

    //Stack that was used to make the particle, used for mass and recipes
    private ItemStack itemStack = ItemStack.EMPTY;

    //True if we are not in a tube
    private boolean notInTube = false;

    public AcceleratorParticle(BlockPos start, EnumFacing moveDirection, float energy)
    {
        this.x = start.getX() + 0.5f;
        this.y = start.getY() + 0.5f;
        this.z = start.getZ() + 0.5f;
        this.moveDirection = moveDirection;
        this.energy = energy;
        this.movementPerTick = .1f; //TODO calculate speed from energy and mass of the itemstack
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
        float distanceToMove = movementPerTick;

        //Get current node we are pathing
        AcceleratorNode currentNode = getCurrentNode();
        if (currentNode != null)
        {
            //WE can move through several nodes, so loop until done TODO replace with path so we can % locate position and node
            while (distanceToMove > AcceleratorNode.ZERO && currentNode != null)
            {
                final EnumFacing prevDirection = moveDirection;

                //Move forward consuming distance
                float distanceMoved = currentNode.move(this, distanceToMove);
                distanceToMove -= distanceMoved;

                //Exit condition if we didn't move, prevents infinite loops
                if (Math.abs(distanceMoved) <= 0.0001)
                {
                    break;
                }

                //Check if our direction changed
                if (prevDirection != moveDirection)
                {
                    System.out.println(this + ": Has turned from " + prevDirection + " to " + moveDirection);

                    //Consume extra energy for turn TODO figure out how much rather than just x2
                    consumeEnergy();

                    //TODO track when we make a turn
                    //TODO send turn point to client for smooth animation
                    //      Client will need to lerp between points
                    //      A -> B -> C -> D
                    //      Each time it gets to a point it will clear the last
                    //      This mean it only has a current pos and target pos
                    //      If it has no target then its goal is the server's position
                    //TODO long term send path to client so we don't need to send every turn change
                    //TODO render points ahead and behind of the particle for debug
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

    public void move(float moveAmount, EnumFacing direction)
    {
        move(moveAmount * direction.getXOffset(),
                moveAmount * direction.getYOffset(),
                moveAmount * direction.getZOffset());
    }

    protected void consumeEnergy()
    {
        //TODO slowly eat energy
    }

    public void setMoveDirection(EnumFacing direction)
    {
        this.moveDirection = direction;
    }

    public void setSpeed(float v)
    {
        this.movementPerTick = v;
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

    public void setPos(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public EnumFacing getMoveDirection()
    {
        return moveDirection;
    }

    public boolean isInvalid()
    {
        return notInTube;
    }

    //TODO entity version

    @Override
    public String toString()
    {
        return String.format("AcceleratorParticle[Pos: %.2f, %.2f, %.2f", x, y, z) + " Dir:" + moveDirection + "]@" + hashCode();
    }
}
