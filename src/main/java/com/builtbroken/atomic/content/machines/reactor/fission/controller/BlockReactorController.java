package com.builtbroken.atomic.content.machines.reactor.fission.controller;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.ASBlocks;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/18/2018.
 */
public class BlockReactorController extends BlockContainer
{
    public static final PropertyEnum<ControllerState> STATE_PROPERTY = PropertyEnum.create("state", ControllerState.class, Lists.newArrayList(ControllerState.values()));

    public BlockReactorController()
    {
        super(Material.IRON);
        setHardness(1);
        setResistance(5);
        setCreativeTab(AtomicScience.creativeTab);
        setTranslationKey(AtomicScience.PREFIX + "reactor.controller");
        setRegistryName(AtomicScience.PREFIX + "reactor_controller");
        setDefaultState(getDefaultState().withProperty(STATE_PROPERTY, ControllerState.OFF));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityReactorController();
    }

    //-----------------------------------------------
    //--------- Triggers ---------------------------
    //----------------------------------------------

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack heldItem = player.getHeldItem(hand);
        if (heldItem != null
                && heldItem.getItem() instanceof ItemBlock
                && ((ItemBlock) heldItem.getItem()).getBlock() == ASBlocks.blockReactorCell)
        {
            return false;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityReactorController)
        {
            TileEntityReactorController controller = ((TileEntityReactorController) tileEntity);
            if (!world.isRemote)
            {
                if (controller.isInErrorState())
                {
                    player.sendMessage(new TextComponentTranslation(getTranslationKey() + ".error.state"));
                }
                else if (heldItem != null && heldItem.getItem() == Items.STICK)
                {
                    controller.setReactorsEnabled(!controller.areReactorsEnabled());
                    player.sendMessage(new TextComponentString(controller.areReactorsEnabled() ? "Reactors are set into enabled state" : "Reactors are set into disabled state"));//TODO translate
                    return true;
                }
                else
                {
                    player.sendMessage(new TextComponentTranslation(getTranslationKey() + ".cell.count", "" + controller.getCellCount()));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityReactorController)
        {
            ((TileEntityReactorController) tileEntity).markForRefresh();
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side)
    {
        return true;
    }

    @Override
    public IBlockState getActualState(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityReactorController)
        {
            final ControllerState controllerState = ((TileEntityReactorController) tile).controllerState;
            return blockState.withProperty(STATE_PROPERTY, controllerState);
        }
        return blockState;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, STATE_PROPERTY);
    }
}
