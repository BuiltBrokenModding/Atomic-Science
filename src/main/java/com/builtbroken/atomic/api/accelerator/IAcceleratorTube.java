package com.builtbroken.atomic.api.accelerator;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSideType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/17/2019.
 */
public interface IAcceleratorTube
{

    /**
     * Wrappers to {@link TileEntity#getPos()}
     *
     * @return
     */
    @Nonnull
    BlockPos getPosition();

    /**
     * The node that represents this tube in the network
     * <p>
     * Keep in mind the node can exist when the world is unload
     *
     * @return
     */
    @Nonnull
    IAcceleratorNode getNode();

}
