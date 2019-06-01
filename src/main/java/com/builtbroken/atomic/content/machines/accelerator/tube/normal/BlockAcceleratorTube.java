package com.builtbroken.atomic.content.machines.accelerator.tube.normal;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeConnectionType;
import com.builtbroken.atomic.content.machines.accelerator.data.TubeSide;
import com.builtbroken.atomic.content.prefab.BlockMachine;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Created by Dark(DarkGuardsman, Robert) on 11/10/2018.
 */
public class BlockAcceleratorTube extends BlockMachine
{
    public static final PropertyEnum<TubeConnectionType> CONNECTION_PROP = PropertyEnum.create("connection", TubeConnectionType.class, Arrays.asList(TubeConnectionType.values()));

    public BlockAcceleratorTube(Material material)
    {
        super(material);
    }

    public BlockAcceleratorTube()
    {
        this(Material.IRON);
        setRegistryName(AtomicScience.PREFIX + "accelerator_tube");
        setTranslationKey(AtomicScience.PREFIX + "accelerator.tube");
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        EnumFacing placement = getPlacement(facing, hitX, hitY, hitZ);
        if(placer.isSneaking())
        {
            placement = placement.getOpposite();
        }
        if(placement.getAxis() == EnumFacing.Axis.Y)
        {
            placement = placer.getHorizontalFacing();
        }
        return getDefaultState().withProperty(ROTATION_PROP, placement);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityAcceleratorTube)
        {
            ItemStack heldItem = playerIn.getHeldItem(hand);
            if (heldItem.getItem() == Items.STICK)
            {
                if (!world.isRemote)
                {
                    playerIn.sendMessage(new TextComponentString("Block Debug:"));
                    playerIn.sendMessage(new TextComponentString("---Pos: " + pos));
                    playerIn.sendMessage(new TextComponentString("---Dir: " + ((TileEntityAcceleratorTube) tile).getDirection() + "==" + state.getValue(ROTATION_PROP)));
                    playerIn.sendMessage(new TextComponentString("---Connection: " + state.getValue(CONNECTION_PROP)));
                    playerIn.sendMessage(new TextComponentString("---Network: " + ((TileEntityAcceleratorTube) tile).getNode().getNetwork()));
                    playerIn.sendMessage(new TextComponentString("---Particles: " + ((TileEntityAcceleratorTube) tile).getNode().getParticles().size()));
                }
                return true;
            }
            else if (heldItem.getItem() == Items.BLAZE_ROD)
            {
                if (!world.isRemote)
                {
                    playerIn.sendMessage(new TextComponentString("Connection State:"));
                    for (TubeSide side : TubeSide.SIDES)
                    {
                        playerIn.sendMessage(new TextComponentString("---" + side.name() + ": " + ((TileEntityAcceleratorTube) tile).getNode().getConnectedTubeState(null, side)));
                    }
                }
                return true;
            }
            else if (heldItem.getItem() == Items.REDSTONE)
            {
                if (((TileEntityAcceleratorTube) tile).getConnectionType() == TubeConnectionType.NORMAL)
                {
                    switchType(world, pos, state, tile); //TODO consume resources
                }
                else if (!world.isRemote)
                {
                    playerIn.sendStatusMessage(new TextComponentTranslation(getTranslationKey() + ".error.normal.conversion"), true);
                }
                return true;
            }
            else if (heldItem.getItem() == ASItems.itemWrench)
            {
                if (playerIn.isSneaking())
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

    protected void switchType(World world, BlockPos pos, IBlockState currentState, TileEntity tile)
    {
        //Save data
        NBTTagCompound save = new NBTTagCompound();
        tile.writeToNBT(save);
        save.removeTag("id");

        //Place new block
        world.setBlockState(pos, getSwitchState(currentState));
        currentState = world.getBlockState(pos);

        //Restore data
        tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityAcceleratorTube)
        {
            tile.readFromNBT(save);
            ((TileEntityAcceleratorTube) tile).updateConnections(world, false, true);
            ((TileEntityAcceleratorTube) tile).updateState(true, true);

            tile.markDirty();
            world.notifyBlockUpdate(pos, currentState, world.getBlockState(pos), 3);
        }
    }

    protected IBlockState getSwitchState(IBlockState currentState)
    {
        IBlockState newState = ASBlocks.blockAcceleratorTubePowered.getDefaultState();
        newState = newState.withProperty(CONNECTION_PROP, currentState.getValue(CONNECTION_PROP));
        newState = newState.withProperty(ROTATION_PROP, currentState.getValue(ROTATION_PROP));
        return newState;
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
    {
        //TODO if particle in tube cause explosion
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, ROTATION_PROP, CONNECTION_PROP);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityAcceleratorTube)
        {
            ((TileEntityAcceleratorTube) tile).updateConnections(world, true, true);
            ((TileEntityAcceleratorTube) tile).getNode().updateConnections(world);
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
        if (tile instanceof TileEntityAcceleratorTube && ((TileEntityAcceleratorTube) tile).getNode().getNetwork() != null)
        {
            ((TileEntityAcceleratorTube) tile).getNode().getNetwork().destroy();
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityAcceleratorTube)
        {
            return state.withProperty(CONNECTION_PROP, ((TileEntityAcceleratorTube) tile).getConnectionType());
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
            if(((TileEntityAcceleratorTube) tile).getNode().updateConnections(world))
            {
                if(((TileEntityAcceleratorTube) tile).getNode().getNetwork() != null)
                {
                    ((TileEntityAcceleratorTube) tile).getNode().getNetwork().destroy();
                }
            }
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
