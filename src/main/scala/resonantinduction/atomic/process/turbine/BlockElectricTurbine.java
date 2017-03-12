package resonantinduction.atomic.process.turbine;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import resonant.lib.prefab.turbine.BlockTurbine;
import resonant.lib.render.block.BlockRenderingHandler;
import resonantinduction.atomic.Atomic;
import resonantinduction.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockElectricTurbine extends BlockTurbine
{
    public BlockElectricTurbine(int id)
    {
        super(id, Material.iron);
        setTextureName(Reference.PREFIX + "machine");
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
        return new TileElectricTurbine();
    }
}
