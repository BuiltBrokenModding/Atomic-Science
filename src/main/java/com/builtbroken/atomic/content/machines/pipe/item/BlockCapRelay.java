package com.builtbroken.atomic.content.machines.pipe.item;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.pipe.imp.TileEntityDirectionalPipe;
import com.builtbroken.atomic.content.prefab.BlockMachine;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
public class BlockCapRelay extends BlockMachine implements ITileEntityProvider
{
    public BlockCapRelay()
    {
        super(Material.IRON);
        setRegistryName(AtomicScience.PREFIX + "cap_relay");
        setTranslationKey(AtomicScience.PREFIX + "cap.relay");
        setDefaultState(getDefaultState().withProperty(ROTATION_PROP, EnumFacing.NORTH));
        setCreativeTab(AtomicScience.creativeTab);
        setHardness(10);
        setResistance(10);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(playerIn.getHeldItem(hand).isEmpty())
        {
            if (!worldIn.isRemote)
            {
                TileEntity tileEntity = worldIn.getTileEntity(pos);
                if (tileEntity instanceof TileEntityCapRelay)
                {
                    playerIn.sendMessage(new TextComponentString( "Direction: " + ((TileEntityCapRelay) tileEntity).getDirection()));
                    playerIn.sendMessage(new TextComponentString(" I: " + tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing) ));
                    playerIn.sendMessage(new TextComponentString(" F: " + tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing) ));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        //TODO improve placement by detecting click of face to get direction
        //      Make sure to add config to disable
        EnumFacing placeDirection = facing;

        //Attempt to find connection block
        final BlockPos facePos = pos.offset(facing.getOpposite());
        if(world.isBlockLoaded(facePos))
        {
            final TileEntity tileEntity = world.getTileEntity(facePos);
            if(tileEntity instanceof TileEntityDirectionalPipe)
            {
                //return normal facing
                placeDirection = placeDirection.getOpposite();
            }
        }

        //invert if sneaking
        if(placer.isSneaking())
        {
            placeDirection = placeDirection.getOpposite();
        }

        //Return
        return getDefaultState().withProperty(ROTATION_PROP, placeDirection);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityCapRelay();
    }
}
