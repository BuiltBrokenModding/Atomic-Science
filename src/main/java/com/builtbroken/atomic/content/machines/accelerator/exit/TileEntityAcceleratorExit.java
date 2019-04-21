package com.builtbroken.atomic.content.machines.accelerator.exit;

import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorParticle;
import com.builtbroken.atomic.content.machines.accelerator.tube.TileEntityAcceleratorTubePrefab;
import com.builtbroken.atomic.content.machines.container.TileEntityItemContainer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/20/2019.
 */
public class TileEntityAcceleratorExit extends TileEntityAcceleratorTubePrefab
{
    @Override
    public void onLoad()
    {
        acceleratorNode.setData(getPos(), getDirection(), TubeConnectionType.END_CAP);
        acceleratorNode.onExitCallback = (particle) -> onParticleExit(particle);
    }

    public void onParticleExit(AcceleratorParticle particle)
    {
        //Kill particle
        particle.setDead();


        //Find collision collector
        final EnumFacing facing = getDirection();
        for (int i = 0; i < 3; i++) //TODO spawn particle in world instead of finding collision chamber
        {
            BlockPos pos = getPos().offset(facing, i + 1);

            IBlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            if (!block.isAir(blockState, world, pos))
            {
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof TileEntityItemContainer)
                {
                    ItemStack itemStack = ((TileEntityItemContainer) tileEntity).getHeldItem();
                    if (!itemStack.isEmpty())
                    {
                        //Consume item
                        ((TileEntityItemContainer) tileEntity).setHeldItem(ItemStack.EMPTY);

                        int amount = (int) (Math.random() * 100);

                        TileEntity tileUnder = world.getTileEntity(pos.offset(EnumFacing.DOWN));
                        if (tileUnder.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP))
                        {
                            IFluidHandler handler = tileUnder.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
                            if (handler != null)
                            {
                                int filled = handler.fill(new FluidStack(ASFluids.ANTIMATTER.fluid, amount), true);
                                int remains = amount - filled;
                                if (remains > 1)
                                {
                                    System.out.println(this + " Failed to store all antimater into " + tileUnder);
                                    //TODO let particle bounce around with remaining antimatter damaging walls
                                }
                            }
                        }
                    }
                    else
                    {
                        ((TileEntityItemContainer) tileEntity).setHeldItem(particle.getItem());
                    }
                }
                break;
            }
        }
    }
}
