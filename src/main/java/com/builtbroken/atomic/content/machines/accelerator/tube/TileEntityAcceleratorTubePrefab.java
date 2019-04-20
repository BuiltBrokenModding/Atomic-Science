package com.builtbroken.atomic.content.machines.accelerator.tube;

import com.builtbroken.atomic.api.AtomicScienceAPI;
import com.builtbroken.atomic.api.accelerator.IAcceleratorTube;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import com.builtbroken.atomic.content.prefab.TileEntityPrefab;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/20/2019.
 */
public class TileEntityAcceleratorTubePrefab extends TileEntityPrefab
{
    protected final AcceleratorNode acceleratorNode = new AcceleratorNode();
    protected final IAcceleratorTube tubeCap = new AcceleratorTubeCap(() -> getPos(), () -> acceleratorNode);

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
    public void invalidate()
    {
        super.invalidate();
        if (acceleratorNode.getNetwork() != null)
        {
            acceleratorNode.getNetwork().destroy();
        }
    }

    @Override
    public void onChunkUnload()
    {
        //TODO mark node as unloaded, find way to restore node
        if (acceleratorNode.getNetwork() != null)
        {
            acceleratorNode.getNetwork().destroy();
        }
    }

    public void setDirection(EnumFacing direction)
    {
        acceleratorNode.setDirection(direction);
    }

    @Override
    public void setPos(BlockPos posIn)
    {
        super.setPos(posIn);
        acceleratorNode.setPos(pos);
    }
}
