package com.builtbroken.atomic.lib.placement;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/12/2018.
 */
public class BlockPlacement
{
    public int dim;
    public BlockPos pos;
    public IBlockState blockState;

    public int placementDelay = 0;

    public BlockPlacement(World world, BlockPos pos, IBlockState block)
    {
        this.dim = world.provider.getDimension();
        this.pos = pos;
        this.blockState = block;
    }

    public BlockPlacement delay(int ticks)
    {
        placementDelay = ticks;
        return this;
    }

    /**
     * Called to place the block
     * <p>
     * Placement is not required when called. Instead
     * it can be used as an update method. Allowing
     * for delaying placement for a few ticks.
     *
     * @return true to remove from queue
     */
    public boolean doPlacement()
    {
        if (placementDelay-- > 0)
        {
            return false;
        }
        try
        {
            if (canDoAction())
            {
                final World world = world();

                if (world != null)
                {
                    if (world.setBlockState(pos, blockState, 3))
                    {
                        //world.markBlockForUpdate(x, y, z); TODO
                        onPlacedBlock();
                    }
                    else
                    {
                        AtomicScience.logger.error("PlacementQueue: Failed to place block in world. " + this);
                    }
                }
                else
                {
                    AtomicScience.logger.error("PlacementQueue: Failed to get world for placement. " + this);
                }
            }
        }
        catch (Exception e)
        {
            AtomicScience.logger.error("PlacementQueue: Unexpected error placing block. " + this, e);
        }
        return true;
    }

    protected void onPlacedBlock()
    {

    }

    protected boolean canDoAction()
    {
        return true;
    }

    public World world()
    {
        return DimensionManager.getWorld(dim);
    }

    @Override
    public String toString()
    {
        return "BlockPlacement[" + dim + " | " + pos + " | " + blockState + "]";
    }
}
