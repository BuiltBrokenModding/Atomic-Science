package com.builtbroken.atomic.content.machines.accelerator.exit;

import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorParticle;
import com.builtbroken.atomic.content.machines.accelerator.tube.imp.TileEntityAcceleratorTubePrefab;
import com.builtbroken.atomic.content.machines.container.item.TileEntityItemContainer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

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

                        //Add antimatter
                        int amount = 3 + (int) (Math.random() * 10); //TODO add recipe and config
                        int taken = ((TileEntityItemContainer) tileEntity).addAntimatter(amount, false, true, true);
                        int remains = amount - taken;

                        //If any left over cause explosion
                        if(remains > 1)
                        {
                            world.newExplosion(null, x(), y(), zi(), remains / 5, true, false);
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
