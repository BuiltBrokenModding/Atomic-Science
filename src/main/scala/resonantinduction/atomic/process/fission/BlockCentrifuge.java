package resonantinduction.atomic.process.fission;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import resonant.lib.prefab.block.BlockRotatable;
import resonant.lib.render.block.BlockRenderingHandler;
import resonantinduction.atomic.Atomic;
import universalelectricity.api.UniversalElectricity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Centrifuge block */
public class BlockCentrifuge extends BlockRotatable
{
    public BlockCentrifuge(int ID)
    {
        super(ID, UniversalElectricity.machine);

    }

    /** Called when the block is right clicked by the player */
    @Override
    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        int metadata = world.getBlockMetadata(x, y, z);

        if (!world.isRemote)
        {
            par5EntityPlayer.openGui(Atomic.INSTANCE, 0, world, x, y, z);
            return true;
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World var1)
    {
        return new TileCentrifuge();
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderType()
    {
        return BlockRenderingHandler.ID;
    }
}
