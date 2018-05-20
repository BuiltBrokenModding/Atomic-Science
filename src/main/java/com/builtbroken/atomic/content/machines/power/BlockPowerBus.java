package com.builtbroken.atomic.content.machines.power;

import com.builtbroken.atomic.AtomicScience;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

/**
 * Used to transfer power from outside systems to the lab machines
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public class BlockPowerBus extends BlockContainer
{
    public static Supplier<TileEntityPowerBus> rfFactory;
    public static Supplier<TileEntityPowerBus> euFactory;

    public BlockPowerBus()
    {
        super(Material.iron);
        setHardness(1);
        setResistance(5);
        setCreativeTab(AtomicScience.creativeTab);
        setBlockName(AtomicScience.PREFIX + "chem.extractor");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        this.blockIcon = Blocks.iron_block.getIcon(0, 0);
    }

    //-----------------------------------------------
    //--------- Triggers ---------------------------
    //----------------------------------------------

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit)
    {
        if (player.getHeldItem() != null && player.getHeldItem().getItem() == Items.stick)
        {
            if (!world.isRemote)
            {
                TileEntity tile = world.getTileEntity(x, y, z);
                if (tile instanceof TileEntityPowerBus)
                {
                    player.addChatComponentMessage(new ChatComponentText("Network: " + ((TileEntityPowerBus) tile).getPowerNetwork()));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityPowerBus)
        {
            ((TileEntityPowerBus) tile).getPowerNetwork().destroy();
        }
        super.breakBlock(world, x, y, z, block, meta);
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
            return rfFactory != null ? rfFactory.get() : new TileEntityPowerBus();
        }
        else if (meta == 2)
        {
            return euFactory != null ? euFactory.get() : new TileEntityPowerBus();
        }
        return new TileEntityPowerBus();
    }

    //-----------------------------------------------
    //-------- Properties ---------------------------
    //----------------------------------------------

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return 0; //TODO change when model is added
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isNormalCube()
    {
        return false;
    }
}
