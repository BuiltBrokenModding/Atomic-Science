package com.builtbroken.atomic.content.machines.container.item;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.prefab.BlockMachine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/24/2019.
 */
public class BlockItemContainer extends BlockMachine
{
    public static final PropertyEnum<ModelType> MODEL_TYPE = PropertyEnum.create("model", ModelType.class);

    public BlockItemContainer()
    {
        super(Material.IRON);
        setRegistryName(AtomicScience.PREFIX + "item_container");
        setTranslationKey(AtomicScience.PREFIX + "item.container");
        setDefaultState(getDefaultState().withProperty(ROTATION_PROP, EnumFacing.NORTH).withProperty(MODEL_TYPE, ModelType.NORMAL));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, ROTATION_PROP, MODEL_TYPE);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(player.isSneaking())
        {
            return false;
        }
        if (!world.isRemote)
        {
            ItemStack heldItem = player.getHeldItem(hand);
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileEntityItemContainer)
            {
                if (heldItem.isEmpty())
                {
                    heldItem = ((TileEntityItemContainer) tileEntity).getInventory().extractItem(0, 64, false);
                }
                else
                {
                    heldItem = ((TileEntityItemContainer) tileEntity).getInventory().insertItem(0, heldItem, false);
                }

                player.sendStatusMessage(new TextComponentString(((TileEntityItemContainer) tileEntity).getInventory().getStackInSlot(0).getDisplayName()), true);

                player.setHeldItem(hand, heldItem);
                player.inventoryContainer.detectAndSendChanges();
            }
        }
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityItemContainer();
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
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
        return layer == BlockRenderLayer.TRANSLUCENT || layer == BlockRenderLayer.SOLID;
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

    public static enum ModelType implements IStringSerializable
    {
        NORMAL,
        GLASS;

        @Override
        public String getName()
        {
            return name().toLowerCase();
        }
    }
}
