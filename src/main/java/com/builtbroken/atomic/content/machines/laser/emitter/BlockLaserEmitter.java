package com.builtbroken.atomic.content.machines.laser.emitter;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.prefab.BlockMachine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2019.
 */
public class BlockLaserEmitter extends BlockMachine
{
    public BlockLaserEmitter()
    {
        super(Material.IRON);
        setRegistryName(AtomicScience.PREFIX + "laser_emitter");
        setTranslationKey(AtomicScience.PREFIX + "laser.emitter");
        setDefaultState(getDefaultState().withProperty(ROTATION_PROP, EnumFacing.NORTH));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockClickPos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing clickSide, float hitX, float hitY, float hitZ)
    {
        final ItemStack heldItem = playerIn.getHeldItem(hand);
        if (heldItem.getItem() == Items.GLOWSTONE_DUST)
        {
            if (!world.isRemote)
            {
                TileEntity tileEntity = world.getTileEntity(blockClickPos);
                if (tileEntity instanceof TileEntityLaserEmitter)
                {
                    playerIn.sendMessage(new TextComponentString("Boosters: " + ((TileEntityLaserEmitter) tileEntity).boosterCount));
                    playerIn.sendMessage(new TextComponentString("Power: " + ((TileEntityLaserEmitter) tileEntity).battery.getEnergyStored()));
                }
            }
            return true;
        }
        else if (heldItem.getItem() == Items.DYE && heldItem.getItemDamage() == EnumDyeColor.BLUE.getDyeDamage())
        {
            final TileEntity tileEntity = world.getTileEntity(blockClickPos);
            if (tileEntity instanceof TileEntityLaserEmitter && ((TileEntityLaserEmitter) tileEntity).getLaserMode() == LaserModes.NORMAL)
            {

                //Eat item
                if (!playerIn.isCreative())
                {
                    heldItem.shrink(1);
                }

                //Set mode
                ((TileEntityLaserEmitter) tileEntity).setLaserMode(LaserModes.FIELD);
                playerIn.sendStatusMessage(new TextComponentTranslation(getTranslationKey() + ".laser.mode.set.field"), true);
                //TODO play anvil audio
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityLaserEmitter();
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
