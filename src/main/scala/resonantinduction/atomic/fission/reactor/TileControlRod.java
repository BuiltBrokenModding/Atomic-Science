package resonantinduction.atomic.fission.reactor;

import net.minecraft.block.material.Material;
import resonant.lib.content.module.TileBlock;
import resonant.lib.prefab.vector.Cuboid;

/** Control rod block */
public class TileControlRod extends TileBlock
{
    public TileControlRod()
    {
        super(Material.iron);
        bounds = new Cuboid(0.3f, 0f, 0.3f, 0.7f, 1f, 0.7f);
        isOpaqueCube = false;
    }
}
