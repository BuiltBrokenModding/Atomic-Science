package com.builtbroken.atomic.content.reactor;

import com.builtbroken.mc.prefab.tile.Tile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/** Electromagnet block */
public class TileElectromagnet extends Tile
{
    //TODO move to json since this has zero logic

    @SideOnly(Side.CLIENT)
    private static IIcon iconTop, iconGlass;

    public TileElectromagnet()
    {
        super("electromagnet", Material.iron);
        resistance = 20;
        isOpaque = false;
    }

    @Override
    public Tile newTile()
    {
        return null;
    }

    @Override
    public IIcon getIcon(int side, int metadata)
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
    public void registerIcons(IIconRegister iconRegister)
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
    public int getRenderBlockPass()
    {
        return 0;
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }
}
