package com.builtbroken.atomic.content.machines.reactor.fission.core;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.prefab.BlockPrefab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public class BlockReactorCell extends BlockPrefab
{
    public static final PropertyReactorState REACTOR_STRUCTURE_TYPE = new PropertyReactorState();

    public BlockReactorCell()
    {
        super(Material.IRON);
        setHardness(1);
        setResistance(5);
        setDefaultState(getDefaultState().withProperty(REACTOR_STRUCTURE_TYPE, ReactorStructureType.NORMAL));
        setCreativeTab(AtomicScience.creativeTab);
        setTranslationKey(AtomicScience.PREFIX + "reactor.cell");
        setRegistryName(AtomicScience.PREFIX + "reactor_cell");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityReactorCell();
    }

    //-----------------------------------------------
    //--------- Triggers ---------------------------
    //----------------------------------------------

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityReactorCell)
        {
            TileEntityReactorCell reactorCell = ((TileEntityReactorCell) tileEntity);
            ItemStack heldItem = player.getHeldItem(hand);
            if (!heldItem.isEmpty())
            {
                if (heldItem.getItem() == Items.STICK)
                {
                    if (!world.isRemote)
                    {
                        player.sendMessage(new TextComponentString("Fuel: " + reactorCell.getFuelRuntime()));
                        if (reactorCell.getHeatSource().getCurrentNodes() != null)
                        {
                            int nodes = reactorCell.getHeatSource().getCurrentNodes().size();
                            int heat = reactorCell.getHeatSource().getHeatGenerated();
                            player.sendMessage(new TextComponentString(String.format("Thermal: %,dn %,dh", nodes, heat)));
                        }
                        else
                        {
                            player.sendMessage(new TextComponentString("No thermal nodes"));
                        }
                        if (reactorCell.getRadiationSource().getCurrentNodes() != null)
                        {
                            int nodes = reactorCell.getRadiationSource().getCurrentNodes().size();
                            int mat = reactorCell.getRadiationSource().getRadioactiveMaterial();
                            player.sendMessage(new TextComponentString(String.format("Radiation: %,dn %,dr", nodes, mat)));
                        }
                        else
                        {
                            player.sendMessage(new TextComponentString("No radiation nodes"));
                        }
                    }
                    return true;
                }
                else if (reactorCell.isItemValidForSlot(TileEntityReactorCell.SLOT_FUEL_ROD, heldItem))
                {
                    if (!world.isRemote && reactorCell.getInventory().getStackInSlot(TileEntityReactorCell.SLOT_FUEL_ROD).isEmpty())
                    {
                        ItemStack copy = heldItem.splitStack(1);
                        reactorCell.getInventory().setStackInSlot(TileEntityReactorCell.SLOT_FUEL_ROD, copy); //TODO rework to use insert

                        if (heldItem.getCount() <= 0)
                        {
                            player.setHeldItem(hand, ItemStack.EMPTY); //TODO rework
                        }
                        else
                        {
                            player.setHeldItem(hand, heldItem); //TODO rework
                        }
                        player.inventoryContainer.detectAndSendChanges();
                    }
                    return true;
                }
            }
            else if (player.isSneaking())
            {
                if (!world.isRemote && !reactorCell.getInventory().getStackInSlot(TileEntityReactorCell.SLOT_FUEL_ROD).isEmpty())
                {
                    player.setHeldItem(hand, reactorCell.getInventory().getStackInSlot(TileEntityReactorCell.SLOT_FUEL_ROD));
                    reactorCell.getInventory().setStackInSlot(TileEntityReactorCell.SLOT_FUEL_ROD, ItemStack.EMPTY);
                    player.inventoryContainer.detectAndSendChanges();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityReactorCell)
        {
            ((TileEntityReactorCell) tileEntity).updateStructureType();
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityReactorCell)
        {
            ((TileEntityReactorCell) tileEntity).updateStructureType();
        }
    }

    //-------------------------------------------------

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, REACTOR_STRUCTURE_TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(REACTOR_STRUCTURE_TYPE, ReactorStructureType.get(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(REACTOR_STRUCTURE_TYPE).ordinal();
    }

    //-----------------------------------------------
    //-------- Properties ---------------------------
    //----------------------------------------------

    @Override
    public boolean isBlockNormalCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state)
    {
        return false;
    }
}
