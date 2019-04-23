package com.builtbroken.atomic.content.machines.accelerator.magnet;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.prefab.BlockPrefab;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/** Simple support block
 *
 * Created by Dark(DarkGuardsman, Robert) on 11/10/2018.
 */
public class BlockMagnet extends BlockPrefab
{
    public BlockMagnet()
    {
        super(Material.IRON);
        setRegistryName(AtomicScience.PREFIX + "magnet");
        setTranslationKey(AtomicScience.PREFIX + "magnet");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityMagnet();
    }
}
