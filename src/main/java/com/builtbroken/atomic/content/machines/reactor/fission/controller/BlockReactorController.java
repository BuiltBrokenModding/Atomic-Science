package com.builtbroken.atomic.content.machines.reactor.fission.controller;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.lib.LanguageUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/18/2018.
 */
public class BlockReactorController extends BlockContainer
{
    @SideOnly(Side.CLIENT)
    IIcon topIcon;
    @SideOnly(Side.CLIENT)
    IIcon bottomIcon;

    public BlockReactorController()
    {
        super(Material.iron);
        setHardness(1);
        setResistance(5);
        setCreativeTab(AtomicScience.creativeTab);
        setBlockTextureName(AtomicScience.PREFIX + "reactor/controller");
        setBlockName(AtomicScience.PREFIX + "reactor.controller");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityReactorController();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.getTextureName() + ".sides");
        this.topIcon = reg.registerIcon(this.getTextureName() + ".top");
        this.bottomIcon = reg.registerIcon(this.getTextureName() + ".bottom");

    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == 0)
        {
            return topIcon;
        }
        else if (side == 1)
        {
            return bottomIcon;
        }
        return this.blockIcon;
    }

    //-----------------------------------------------
    //--------- Triggers ---------------------------
    //----------------------------------------------

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit)
    {
        ItemStack heldItem = player.getHeldItem();
        if(heldItem != null
                && heldItem.getItem() instanceof ItemBlock
                && ((ItemBlock)heldItem.getItem()).field_150939_a == ASBlocks.blockReactorCell)
        {
            return false;
        }

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityReactorController)
        {
            TileEntityReactorController controller = ((TileEntityReactorController) tileEntity);
            if (!world.isRemote)
            {
                if (controller.isInErrorState())
                {
                    player.addChatComponentMessage(new ChatComponentText(LanguageUtility.getLocal(getUnlocalizedName() + ".error.state")));
                }
                else if(heldItem != null && heldItem.getItem() == Items.stick)
                {
                    controller.setReactorsEnabled(!controller.areReactorsEnabled());
                    player.addChatComponentMessage(new ChatComponentText(controller.areReactorsEnabled() ? "Reactors are set into enabled state" : "Reactors are set into disabled state"));//TODO translate
                    return true;
                }
                else
                {
                    player.addChatComponentMessage(new ChatComponentTranslation(getUnlocalizedName() + ".cell.count", "" + controller.getCellCount()));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityReactorController)
        {
            ((TileEntityReactorController) tileEntity).markForRefresh();
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityReactorController)
        {
            ((TileEntityReactorController) tileEntity).markForRefresh();
        }
    }
}
