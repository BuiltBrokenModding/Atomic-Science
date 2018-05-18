package com.builtbroken.atomic.content.machines.steam.generator;

import com.builtbroken.atomic.AtomicScience;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

/**
 * Simple block that converts steam flow into power
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class BlockSteamGenerator extends BlockContainer
{
    public static Supplier<TileEntitySteamGenerator> rfFactory;
    public static Supplier<TileEntitySteamGenerator> euFactory;

    public BlockSteamGenerator()
    {
        super(Material.iron);
        setHardness(1);
        setResistance(5);
        setCreativeTab(AtomicScience.creativeTab);
        setBlockName(AtomicScience.PREFIX + "steam.generator");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == 0 || side == 1)
        {
            return Blocks.gold_block.getIcon(0, 0);
        }
        return Blocks.anvil.getIcon(0, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        //We pull icons from other blocks
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        if (tab == getCreativeTabToDisplayOn())
        {
            list.add(new ItemStack(item, 1, 0));
            if (rfFactory != null)
            {
                list.add(new ItemStack(item, 1, 1));
            }
            if (euFactory != null)
            {
                list.add(new ItemStack(item, 1, 2));
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        if (meta == 1)
        {
            return rfFactory != null ? rfFactory.get() : new TileEntitySteamGenerator();
        }
        else if (meta == 2)
        {
            return euFactory != null ? euFactory.get() : new TileEntitySteamGenerator();
        }
        return new TileEntitySteamGenerator();
    }
}
