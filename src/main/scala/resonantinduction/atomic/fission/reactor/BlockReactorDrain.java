package resonantinduction.atomic.fission.reactor;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import resonant.lib.prefab.block.BlockRotatable;
import universalelectricity.api.UniversalElectricity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Reactor tap block */
public class BlockReactorDrain extends BlockRotatable
{
    private Icon frontIcon;

    public BlockReactorDrain(int id)
    {
        super(id, UniversalElectricity.machine);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        if (MathHelper.abs((float) entityLiving.posX - x) < 2.0F && MathHelper.abs((float) entityLiving.posZ - z) < 2.0F)
        {
            double d0 = entityLiving.posY + 1.82D - entityLiving.yOffset;

            if (d0 - y > 2.0D)
            {
                world.setBlockMetadataWithNotify(x, y, z, 1, 3);
                return;
            }

            if (y - d0 > 0.0D)
            {
                world.setBlockMetadataWithNotify(x, y, z, 0, 3);
                return;
            }
        }

        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
    }

    @Override
    public Icon getIcon(int side, int metadata)
    {
        if (side == metadata)
        {
            return this.frontIcon;
        }
        return this.blockIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
        this.frontIcon = iconRegister.registerIcon(this.getUnlocalizedName().replace("tile.", "") + "_front");
    }

    @Override
    public TileEntity createNewTileEntity(World var1)
    {
        return new TileReactorDrain();
    }

}
