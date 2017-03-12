package resonantinduction.atomic.particle.accelerator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import resonant.lib.prefab.block.BlockRotatable;
import resonantinduction.atomic.Atomic;
import universalelectricity.api.UniversalElectricity;

/** Accelerator block */
public class BlockAccelerator extends BlockRotatable
{
    public BlockAccelerator(int id)
    {
        super(id, UniversalElectricity.machine);

    }

    @Override
    public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        int metadata = par1World.getBlockMetadata(x, y, z);

        if (!par1World.isRemote)
        {
            par5EntityPlayer.openGui(Atomic.INSTANCE, 0, par1World, x, y, z);
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World var1)
    {
        return new TileAccelerator();
    }
}
