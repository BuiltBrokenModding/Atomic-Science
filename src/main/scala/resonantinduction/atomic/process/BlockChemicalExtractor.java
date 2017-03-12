package resonantinduction.atomic.process;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import resonant.lib.prefab.block.BlockRotatable;
import resonant.lib.render.block.BlockRenderingHandler;
import resonantinduction.atomic.Atomic;
import universalelectricity.api.UniversalElectricity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Chemical Extractor
 * 
 * @author Calclavia */
public class BlockChemicalExtractor extends BlockRotatable
{
    public BlockChemicalExtractor(int ID)
    {
        super(ID, UniversalElectricity.machine);
    }

    /** Called when the block is right clicked by the player */
    @Override
    public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        int metadata = par1World.getBlockMetadata(x, y, z);

        if (!par1World.isRemote)
        {
            par5EntityPlayer.openGui(Atomic.INSTANCE, 0, par1World, x, y, z);
            return true;
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World var1)
    {
        return new TileChemicalExtractor();
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
