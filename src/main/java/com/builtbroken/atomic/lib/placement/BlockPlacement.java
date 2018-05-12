package com.builtbroken.atomic.lib.placement;

import net.minecraft.block.Block;
import net.minecraft.world.World;

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

    @Override
    public String toString()
    {
        return "BlockPlacement[" + dim + " | " + x + ", " + y + ", " + z + " | " + block + " @ " + meta + "]";
    }
}
