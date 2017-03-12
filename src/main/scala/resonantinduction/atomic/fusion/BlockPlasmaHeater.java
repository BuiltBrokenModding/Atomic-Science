package resonantinduction.atomic.fusion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import resonant.lib.prefab.block.BlockTile;
import resonant.lib.render.block.BlockRenderingHandler;
import resonant.lib.utility.FluidUtility;
import universalelectricity.api.UniversalElectricity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Fusion reactor */
public class BlockPlasmaHeater extends BlockTile
{
    public BlockPlasmaHeater(int ID)
    {
        super(ID, UniversalElectricity.machine);

    }

    @Override
    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        return FluidUtility.playerActivatedFluidItem(world, x, y, z, player, side);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderType()
    {
        return BlockRenderingHandler.ID;
    }

    @Override
    public TileEntity createNewTileEntity(World var1)
    {
        return new TilePlasmaHeater();
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

}
