package com.builtbroken.atomic.content.machines.accelerator.tube.imp;

import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.api.accelerator.IAcceleratorNode;
import com.builtbroken.atomic.api.accelerator.IAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import com.builtbroken.atomic.content.prefab.TileEntityPrefab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/20/2019.
 */
public abstract class TileEntityAcceleratorTubePrefab extends TileEntityPrefab
{
    public static final String NBT_NODE = "accelerator_node";


    protected final AcceleratorTubeCap tubeCap = new AcceleratorTubeCap(this, () -> getDirection(), () -> getConnectionType());


    public AcceleratorNode getNode()
    {
        return tubeCap.getNode();
    }

    public abstract TubeConnectionType getConnectionType();

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
        if (!world.isRemote && getNode().getNetwork() != null)
        {
            getNode().getNetwork().destroy();
        }
    }

    @Override
    public void onChunkUnload()
    {
        //TODO mark node as unloaded, find way to restore node
        if (!world.isRemote && getNode().getNetwork() != null)
        {
            getNode().getNetwork().destroy();
        }
    }

    @Override
    public void markDirty()
    {
        if (this.world != null)
        {
            this.world.markChunkDirty(this.pos, this);
        }
    }
}
