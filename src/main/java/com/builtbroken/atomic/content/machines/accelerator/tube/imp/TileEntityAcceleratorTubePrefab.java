package com.builtbroken.atomic.content.machines.accelerator.tube.imp;

import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.api.accelerator.IAcceleratorNode;
import com.builtbroken.atomic.api.accelerator.IAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import com.builtbroken.atomic.content.prefab.TileEntityPrefab;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/20/2019.
 */
public class TileEntityAcceleratorTubePrefab extends TileEntityPrefab
{
    public static final String NBT_NODE = "accelerator_node";


    protected final AcceleratorTubeCap tubeCap = new AcceleratorTubeCap(this);


    public AcceleratorNode getNode()
    {
        return tubeCap.getNode();
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
        getNode().load(compound.getCompoundTag(NBT_NODE));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag(NBT_NODE, getNode().save(new NBTTagCompound()));
        return super.writeToNBT(compound);
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        if (getNode().getNetwork() != null)
        {
            getNode().getNetwork().destroy();
        }
    }

    @Override
    public void onChunkUnload()
    {
        //TODO mark node as unloaded, find way to restore node
        if (getNode().getNetwork() != null)
        {
            getNode().getNetwork().destroy();
        }
    }

    public void setDirection(EnumFacing direction)
    {
        getNode().setDirection(direction);
    }

    @Override
    public void setPos(BlockPos posIn)
    {
        super.setPos(posIn);
        getNode().setPos(pos);
    }
}
