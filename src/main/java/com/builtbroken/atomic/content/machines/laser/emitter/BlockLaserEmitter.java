package com.builtbroken.atomic.content.machines.laser.emitter;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.prefab.BlockMachine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
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
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        final float spacing = 0.3f;

        if (!world.isRemote && placer instanceof EntityPlayer)
        {
            placer.sendMessage(new TextComponentString(String.format("Click: %.2fx %.2fy %.2fz", hitX, hitY, hitZ)));
        }


        EnumFacing direction;

        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN)
        {
            //WEST
            boolean left = hitX <= spacing;
            //EAST
            boolean right = hitX >= (1 - spacing);
            //NORTH
            boolean up = hitZ <= spacing;
            //SOUTH
            boolean down = hitZ >= (1 - spacing);

            if (!up && !down && (left || right))
            {
                direction = left ? EnumFacing.WEST : EnumFacing.EAST;
            }
            else if (!left && !right && (up || down))
            {
                direction = up ? EnumFacing.NORTH : EnumFacing.SOUTH;
            }
            else if (!left && !right && !up && !down)
            {
                direction = facing;
            }
            else
            {
                direction = facing.getOpposite();
            }
        }
        else
        {
            boolean z = facing.getAxis() == EnumFacing.Axis.Z;
            boolean left = (z ? hitX : hitZ) <= spacing;
            boolean right = (z ? hitX : hitZ) >= (1 - spacing);

            boolean down = hitY <= spacing;
            boolean up = hitY >= (1 - spacing);

            if (!up && !down && (left || right))
            {
                if (z)
                {
                    direction = left ? EnumFacing.WEST : EnumFacing.EAST;
                }
                else
                {
                    direction = left ? EnumFacing.NORTH : EnumFacing.SOUTH;
                }
            }
            else if (!left && !right && (up || down))
            {
                direction = up ? EnumFacing.UP : EnumFacing.DOWN;
            }
            else if (!left && !right && !up && !down)
            {
                direction = facing;
            }
            else
            {
                direction = facing.getOpposite();
            }
        }

        if (!world.isRemote && placer instanceof EntityPlayer)
        {
            placer.sendMessage(new TextComponentString("Facing: " + direction));
        }
        return getDefaultState().withProperty(ROTATION_PROP, direction);
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
