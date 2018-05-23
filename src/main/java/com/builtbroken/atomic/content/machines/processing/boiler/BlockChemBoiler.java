package com.builtbroken.atomic.content.machines.processing.boiler;

import com.builtbroken.atomic.AtomicScience;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2018.
 */
public class BlockChemBoiler extends BlockContainer
{
    public BlockChemBoiler()
    {
        super(Material.iron);
        setHardness(1);
        setResistance(5);
        setCreativeTab(AtomicScience.creativeTab);
        setBlockName(AtomicScience.PREFIX + "chem.boiler");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityChemBoiler();
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
        if (!world.isRemote)
        {
            if (player.getHeldItem() != null && (player.getHeldItem().getItem() == Items.stick || player.getHeldItem().getItem() == Items.slime_ball)) //TODO replace with wrench
            {
                TileEntity tile = world.getTileEntity(x, y, z);
                if (tile instanceof TileEntityChemBoiler)
                {
                    if (player.getHeldItem().getItem() == Items.slime_ball)
                    {
                        ((TileEntityChemBoiler) tile).outputSideWasteTank[side] = !((TileEntityChemBoiler) tile).outputSideWasteTank[side];
                        player.addChatComponentMessage(new ChatComponentText("Side set to output waste water -> " + ((TileEntityChemBoiler) tile).outputSideWasteTank[side]));
                    }
                    else
                    {
                        ((TileEntityChemBoiler) tile).outputSideHexTank[side] = !((TileEntityChemBoiler) tile).outputSideHexTank[side];
                        player.addChatComponentMessage(new ChatComponentText("Side set to output hexafluoride gas -> " + ((TileEntityChemBoiler) tile).outputSideHexTank[side]));
                    }
                }
            }
            else
            {
                player.openGui(AtomicScience.INSTANCE, 0, world, x, y, z);
            }
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack stack)
    {
        int rotation = MathHelper.floor_double((double) (entityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        if (rotation == 0)
        {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }
        else if (rotation == 1)
        {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }
        else if (rotation == 2)
        {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }
        else if (rotation == 3)
        {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }
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
        return -1;
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
