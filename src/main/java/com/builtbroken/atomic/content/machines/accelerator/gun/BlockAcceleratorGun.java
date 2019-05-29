package com.builtbroken.atomic.content.machines.accelerator.gun;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorDebug;
import com.builtbroken.atomic.content.prefab.BlockMachine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 12/15/2018.
 */
public class BlockAcceleratorGun extends BlockMachine
{
    public BlockAcceleratorGun()
    {
        super(Material.IRON);
        setRegistryName(AtomicScience.PREFIX + "accelerator_gun");
        setTranslationKey(AtomicScience.PREFIX + "accelerator.gun");
        setDefaultState(getDefaultState().withProperty(ROTATION_PROP, EnumFacing.NORTH));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockClickPos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing clickSide, float hitX, float hitY, float hitZ)
    {
        final TileEntity gunTile = world.getTileEntity(blockClickPos);
        if (gunTile instanceof TileEntityAcceleratorGun)
        {
            if (playerIn.getHeldItem(hand).getItem() == Items.STICK)
            {
                if (!world.isRemote)
                {
                    AcceleratorDebug.printNetwork(((TileEntityAcceleratorGun)gunTile).tubeCap.getNode().getNetwork());
                }
                return true;
            }

        }

        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityAcceleratorGun();
    }


    //-----------------------------------------------
    //-------- Properties ---------------------------
    //----------------------------------------------

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

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
