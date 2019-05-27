package com.builtbroken.atomic.content.machines.accelerator.gun;

import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.api.accelerator.AcceleratorHelpers;
import com.builtbroken.atomic.api.accelerator.IAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorHandler;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNetwork;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import com.builtbroken.atomic.content.machines.accelerator.tube.imp.AcceleratorTubeCap;
import com.builtbroken.atomic.content.machines.container.item.TileEntityItemContainer;
import com.builtbroken.atomic.content.machines.laser.emitter.TileEntityLaserEmitter;
import com.builtbroken.atomic.content.prefab.TileEntityMachine;
import com.builtbroken.atomic.lib.timer.TickTimerTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 12/15/2018.
 */
public class TileEntityAcceleratorGun extends TileEntityMachine
{
    public static final String NBT_NODE = "accelerator_node";

    public final AcceleratorNode acceleratorNode = new AcceleratorNode(() -> dim(), () -> !isInvalid(), () -> markDirty());
    private final IAcceleratorTube tubeCap = new AcceleratorTubeCap(() -> getPos(), () -> acceleratorNode);

    public TileEntityAcceleratorGun()
    {
        tickServer.add(TickTimerTileEntity.newConditional(20, (tick) -> validateNetwork(), () -> acceleratorNode.getNetwork() == null || acceleratorNode.getNetwork().isDead()));
    }

    @Override
    public void onLoad()
    {
        acceleratorNode.setData(getPos(), getDirection(), TubeConnectionType.START_CAP);
    }

    private void validateNetwork()
    {
        //If we have no network try to locate a tube with a network
        if (acceleratorNode.getNetwork() == null)
        {
            final EnumFacing facing = getDirection();

            final TileEntity tileEntity = world.getTileEntity(getPos().offset(facing));
            final IAcceleratorTube tube = AcceleratorHelpers.getAcceleratorTube(tileEntity, null);
            if (tube != null)
            {
                //Connection to node to create or join a network
                tube.getNode().connect(acceleratorNode, getDirection().getOpposite());
            }
        }

        //Find tubes
        if (acceleratorNode.getNetwork() != null)
        {
            acceleratorNode.getNetwork().init(getWorld(), getPos().offset(getDirection()));
        }
    }

    private void createNewNetwork()
    {
        acceleratorNode.setNetwork(new AcceleratorNetwork(dim()));
    }

    /**
     * Called when laser fires through a container into the gun
     *
     * @param container    - container holding an item
     * @param laserEmitter - laser that fired, used to get starting energy
     */
    public void onLaserFiredInto(TileEntityItemContainer container, TileEntityLaserEmitter laserEmitter)
    {
        if(laserEmitter.getDirection() == getDirection())
        {
            final ItemStack heldItem = container.getHeldItem();
            if (!heldItem.isEmpty())
            {
                createParticle(heldItem, laserEmitter.boosterCount / container.consumeItems()); //TODO figure out how we are going to do energy
            }
        }
        else
        {
            //Explode
        }
    }

    /**
     * Creates a new particle at the gun tip fired into the system
     *
     * @param item          - item to use
     * @param energyToStart - energy to start with
     */
    public void createParticle(ItemStack item, int energyToStart)
    {
        AcceleratorHandler.newParticle(world, acceleratorNode, item, energyToStart);
    }

    /**
     * Called from remote system to trigger laser
     */
    public boolean fireLaser()
    {
        TileEntityLaserEmitter laser = getLaser();
        if (laser != null)
        {
            return laser.triggerFire();
        }
        return false;
    }

    /**
     * Gets the laser behind the gun tip
     *
     * @return
     */
    public TileEntityLaserEmitter getLaser()
    {
        final EnumFacing facing = getDirection().getOpposite();

        final TileEntity tileEntity = world.getTileEntity(getPos().offset(facing, 2));
        if (tileEntity instanceof TileEntityLaserEmitter)
        {
            TileEntityLaserEmitter laser = (TileEntityLaserEmitter) tileEntity;
            if (laser.getDirection() == getDirection())
            {
                return laser;
            }
        }
        return null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == AtomicScienceAPI.ACCELERATOR_TUBE_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if(capability == AtomicScienceAPI.ACCELERATOR_TUBE_CAPABILITY)
        {
            return (T) tubeCap;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        acceleratorNode.load(compound.getCompoundTag(NBT_NODE));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag(NBT_NODE, acceleratorNode.save(new NBTTagCompound()));
        return super.writeToNBT(compound);
    }
}
