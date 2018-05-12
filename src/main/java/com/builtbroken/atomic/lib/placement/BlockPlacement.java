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

    public BlockPlacement(World world, int x, int y, int z, Block block, int meta)
    {
        this.dim = world.provider.dimensionId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
        this.meta = meta;
    }

    public void doPlacement()
    {
        try
        {
            if (canDoAction())
            {
                final World world = world();

                if (world != null)
                {
                    if (world.setBlock(x, y, z, block, meta, 3))
                    {
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
