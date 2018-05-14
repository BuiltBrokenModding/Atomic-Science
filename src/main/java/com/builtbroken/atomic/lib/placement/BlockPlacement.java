package com.builtbroken.atomic.lib.placement;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/12/2018.
 */
public class BlockPlacement
{
    public int dim;
    public int x;
    public int y;
    public int z;
    public Block block;
    public int meta;

    public int placementDelay = 0;

    public BlockPlacement(World world, int x, int y, int z, Block block, int meta)
    {
        this.dim = world.provider.dimensionId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
        this.meta = meta;
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
                    if (world.setBlock(x, y, z, block, meta, 3))
                    {
                        world.markBlockForUpdate(x, y, z);
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
        return "BlockPlacement[" + dim + " | " + x + ", " + y + ", " + z + " | " + block + " @ " + meta + "]";
    }
}
