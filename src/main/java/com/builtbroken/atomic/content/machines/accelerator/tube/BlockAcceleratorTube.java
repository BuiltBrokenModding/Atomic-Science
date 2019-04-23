package com.builtbroken.atomic.content.machines.accelerator.tube;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
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
import net.minecraft.nbt.NBTTagCompound;
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

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 11/10/2018.
 */
public class BlockAcceleratorTube extends BlockPrefab
{
    public static final PropertyEnum<AcceleratorTubeType> TYPE_PROP = PropertyEnum.create("type", AcceleratorTubeType.class, Arrays.asList(AcceleratorTubeType.values()));
    public static final PropertyEnum<TubeConnectionType> CONNECTION_PROP = PropertyEnum.create("connection", TubeConnectionType.class, Arrays.asList(TubeConnectionType.values()));
    public static final PropertyDirection ROTATION_PROP = PropertyDirection.create("rotation");

    public BlockAcceleratorTube()
    {
        super(Material.IRON);
        setRegistryName(AtomicScience.PREFIX + "accelerator_tube");
        setTranslationKey(AtomicScience.PREFIX + "accelerator.tube");
        setDefaultState(getDefaultState().withProperty(TYPE_PROP, AcceleratorTubeType.NORMAL).withProperty(CONNECTION_PROP, TubeConnectionType.NORMAL));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityAcceleratorTube)
        {
            ItemStack heldItem = playerIn.getHeldItem(hand);
            if (heldItem.getItem() == Items.GLOWSTONE_DUST)
            {
                if (tile instanceof TileEntityAcceleratorTubePowered)
                {
                    if (!world.isRemote)
                    {
                        ((TileEntityAcceleratorTubePowered) tile).scanForMagnets();
                        playerIn.sendMessage(new TextComponentString("Power: " + ((TileEntityAcceleratorTubePowered) tile).calculateMagnetPower()));
                    }
                    return true;
                }
            }
            else if (heldItem.getItem() == Items.STICK)
            {
                if (!world.isRemote)
                {
                    playerIn.sendMessage(new TextComponentString("Block Debug:"));
                    playerIn.sendMessage(new TextComponentString("---Pos: " + pos));
                    playerIn.sendMessage(new TextComponentString("---Dir: " + ((TileEntityAcceleratorTube) tile).getDirection() + "==" + state.getValue(ROTATION_PROP)));
                    playerIn.sendMessage(new TextComponentString("---Type: " + state.getValue(TYPE_PROP)));
                    playerIn.sendMessage(new TextComponentString("---Connection: " + state.getValue(CONNECTION_PROP)));
                }
                return true;
            }
            else if (heldItem.getItem() == Items.BLAZE_ROD)
            {
                if (!world.isRemote)
                {
                    playerIn.sendMessage(new TextComponentString("Connection State:"));
                    for(TubeSide side : TubeSide.SIDES)
                    {
                        playerIn.sendMessage(new TextComponentString("---" + side.name() + ": " + ((TileEntityAcceleratorTube) tile).acceleratorNode.getConnectedTubeState(null, side)));
                    }
                }
                return true;
            }
            else if (heldItem.getItem() == Items.REDSTONE)
            {
                AcceleratorTubeType type = state.getValue(TYPE_PROP);
                AcceleratorTubeType typeToSet = type.next();

                NBTTagCompound save = new NBTTagCompound();
                tile.writeToNBT(save);
                save.removeTag("id");

                world.setBlockState(pos, state.withProperty(TYPE_PROP, typeToSet));

                tile = world.getTileEntity(pos);
                if (tile instanceof TileEntityAcceleratorTube)
                {
                    tile.readFromNBT(save);
                    ((TileEntityAcceleratorTube) tile).updateConnections(world,false, true);
                    ((TileEntityAcceleratorTube) tile).updateState(true, true);
                }

                if (!world.isRemote)
                {
                    playerIn.sendMessage(new TextComponentString("Type changed from '" + type + "' to '" + typeToSet + "'"));
                }
                return true;
            }
            else if (heldItem.getItem() == ASItems.itemWrench)
            {
                if(playerIn.isSneaking())
                {

                }
                else
                {

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
            ((TileEntityAcceleratorTube) tile).updateConnections(world,true, true);
            ((TileEntityAcceleratorTube) tile).acceleratorNode.updateConnections(world);
        }
    }

    @Override
    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onPlayerDestroy(worldIn, pos, state);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityAcceleratorTube && ((TileEntityAcceleratorTube) tile).acceleratorNode.getNetwork() != null)
        {
            ((TileEntityAcceleratorTube) tile).acceleratorNode.getNetwork().destroy();
        }
        super.breakBlock(world, pos, state);
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
            //((TileEntityAcceleratorTube) tile).updateConnections(true); - breaks connections
            ((TileEntityAcceleratorTube) tile).acceleratorNode.updateConnections(world);
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        if (getStateFromMeta(meta).getValue(TYPE_PROP) == AcceleratorTubeType.POWERED)
        {
            return new TileEntityAcceleratorTubePowered();
        }
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
