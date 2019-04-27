package com.builtbroken.atomic.content.machines.accelerator.tube.powered;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.machines.accelerator.tube.normal.BlockAcceleratorTube;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Dark(DarkGuardsman, Robert) on 11/10/2018.
 */
public class BlockAcceleratorTubePowered extends BlockAcceleratorTube
{
    public BlockAcceleratorTubePowered()
    {
        super(Material.IRON);
        setRegistryName(AtomicScience.PREFIX + "accelerator_tube_powered");
        setTranslationKey(AtomicScience.PREFIX + "accelerator.tube.power");
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityAcceleratorTubePowered)
        {
            ItemStack heldItem = playerIn.getHeldItem(hand);
            if (heldItem.getItem() == Items.GLOWSTONE_DUST)
            {
                if (!world.isRemote)
                {
                    playerIn.sendMessage(new TextComponentString("Power: " + ((TileEntityAcceleratorTubePowered) tile).getMagnetPower()));
                }
                return true;
            }
        }
        return super.onBlockActivated(world, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ASItems.blockAcceleratorTube;
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(ASItems.blockAcceleratorTube, 1, 0);
    }

    @Override
    protected IBlockState getSwitchState(IBlockState currentState)
    {
        IBlockState newState = ASBlocks.blockAcceleratorTube.getDefaultState();
        newState = newState.withProperty(CONNECTION_PROP, currentState.getValue(CONNECTION_PROP));
        newState = newState.withProperty(ROTATION_PROP, currentState.getValue(ROTATION_PROP));
        return newState;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityAcceleratorTubePowered();
    }
}
