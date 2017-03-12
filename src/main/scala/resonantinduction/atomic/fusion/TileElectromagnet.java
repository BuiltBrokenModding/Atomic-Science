package resonantinduction.atomic.fusion;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import resonant.api.IElectromagnet;
import resonant.lib.content.module.TileBase;
import resonant.lib.content.module.TileRender;
import resonant.lib.utility.ConnectedTextureRenderer;
import resonant.lib.prefab.item.ItemBlockMetadata;
import resonantinduction.core.Reference;
import universalelectricity.api.UniversalElectricity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Electromagnet block */
public class TileElectromagnet extends TileBase implements IElectromagnet
{
    private static Icon iconTop, iconGlass;

    public TileElectromagnet()
    {
        super(UniversalElectricity.machine);
        blockResistance = 20;
        isOpaqueCube = false;
        itemBlock = ItemBlockMetadata.class;
    }

    @Override
    public Icon getIcon(int side, int metadata)
    {
        if (metadata == 1)
        {
            return iconGlass;
        }

        if (side == 0 || side == 1)
        {
            return iconTop;
        }

        return super.getIcon(side, metadata);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
        iconTop = iconRegister.registerIcon(domain + textureName + "_top");
        iconGlass = iconRegister.registerIcon(domain + "electromagnetGlass");
    }

    @Override
    public int metadataDropped(int meta, int fortune)
    {
        return meta;
    }

    @Override
    public boolean canUpdate()
    {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
    {
        return true; // access.getBlockId(x, y, z) == blockID() && access.getBlockMetadata(x, y, z) == 1 ? false : super.shouldSideBeRendered(access, x, y, z, side);
    }

    @Override
    public int getRenderBlockPass()
    {
        return 0;
    }

    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @Override
    public boolean isRunning()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected TileRender newRenderer()
    {
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "atomic_edge");
    }
}
