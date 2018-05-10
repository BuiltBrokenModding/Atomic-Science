package com.builtbroken.atomic.content.tiles.reactor.fission;

import com.builtbroken.atomic.AtomicScience;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public class BlockReactorCell extends BlockContainer
{
    public BlockReactorCell()
    {
        super(Material.iron);
        setHardness(1);
        setResistance(5);
        setCreativeTab(AtomicScience.creativeTab);
        setBlockName(AtomicScience.PREFIX + "reactor.cell");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityReactorCell();
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
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityReactorCell)
        {
            TileEntityReactorCell reactorCell = ((TileEntityReactorCell) tileEntity);
            ItemStack heldItem = player.getHeldItem();
            if (heldItem != null)
            {
                if (reactorCell.isItemValidForSlot(0, heldItem))
                {
                    if (!world.isRemote && reactorCell.getStackInSlot(0) == null)
                    {
                        ItemStack copy = heldItem.splitStack(1);
                        reactorCell.setInventorySlotContents(0, copy);

                        if (heldItem.stackSize <= 0)
                        {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        }
                        else
                        {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, heldItem);
                        }
                        player.inventoryContainer.detectAndSendChanges();
                    }
                    return true;
                }
            }
            else
            {
                if (!world.isRemote && reactorCell.getStackInSlot(0) != null)
                {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, reactorCell.getStackInSlot(0));
                    reactorCell.setInventorySlotContents(0, null);
                    player.inventoryContainer.detectAndSendChanges();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityReactorCell)
        {
            ((TileEntityReactorCell) tileEntity).updateStructureType();
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
