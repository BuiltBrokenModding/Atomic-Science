package com.builtbroken.atomic.content.machines.accelerator.tube;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.prefab.BlockPrefab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nullable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/10/2018.
 */
public class BlockAcceleratorTube extends BlockPrefab
{
    public static final PropertyEnum<AcceleratorTubeType> TYPE_PROP = PropertyEnum.create("type", AcceleratorTubeType.class, Arrays.asList(AcceleratorTubeType.values()));
    public static final PropertyEnum<AcceleratorConnectionType> CONNECTION_PROP = PropertyEnum.create("connection", AcceleratorConnectionType.class, Arrays.asList(AcceleratorConnectionType.values()));
    public static final PropertyDirection ROTATION_PROP = PropertyDirection.create("rotation");

    public BlockAcceleratorTube()
    {
        super(Material.IRON);
        setRegistryName(AtomicScience.PREFIX + "accelerator_tube");
        setTranslationKey(AtomicScience.PREFIX + "accelerator.tube");
        setDefaultState(getDefaultState().withProperty(TYPE_PROP, AcceleratorTubeType.NORMAL).withProperty(CONNECTION_PROP, AcceleratorConnectionType.NORMAL));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityAcceleratorTube)
        {
            if (playerIn.getHeldItem(hand).getItem() == Items.STICK)
            {
                if (!world.isRemote)
                {
                    playerIn.sendMessage(new TextComponentString("Block Debug:"));
                    playerIn.sendMessage(new TextComponentString("---Dir: " + ((TileEntityAcceleratorTube) tile).getDirection() + "==" + state.getValue(ROTATION_PROP)));
                    playerIn.sendMessage(new TextComponentString("---Type: " + state.getValue(TYPE_PROP)));
                    playerIn.sendMessage(new TextComponentString("---Connection: " + state.getValue(CONNECTION_PROP)));
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
    {
        //TODO if particle in tube cause explosion
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TYPE_PROP, ROTATION_PROP, CONNECTION_PROP);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(TYPE_PROP, AcceleratorTubeType.byIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(TYPE_PROP).ordinal();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return getStateFromMeta(meta).withProperty(ROTATION_PROP, placer.getHorizontalFacing());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityAcceleratorTube)
        {
            ((TileEntityAcceleratorTube) tile).direction = placer.getHorizontalFacing();
            ((TileEntityAcceleratorTube) tile).updateConnections(true);
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityAcceleratorTube)
        {
            return state
                    .withProperty(ROTATION_PROP, ((TileEntityAcceleratorTube) tile).getDirection())
                    .withProperty(CONNECTION_PROP, ((TileEntityAcceleratorTube) tile).getConnectionType());
        }
        return state;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityAcceleratorTube
                //Make sure we are on a server to prevent render notification updates
                && tile.getWorld() != null
                && !((TileEntityAcceleratorTube) tile).world().isRemote)
        {
            ((TileEntityAcceleratorTube) tile).updateConnections(true);
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityAcceleratorTube();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
}
