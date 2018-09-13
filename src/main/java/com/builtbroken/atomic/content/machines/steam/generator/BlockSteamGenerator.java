package com.builtbroken.atomic.content.machines.steam.generator;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.block.BlockContainer;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Supplier;

/**
 * Simple block that converts steam flow into power
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class BlockSteamGenerator extends BlockContainer
{
    public static Supplier<TileEntitySteamGenerator> rfFactory;
    public static Supplier<TileEntitySteamGenerator> euFactory;

    public BlockSteamGenerator()
    {
        super(Material.IRON);
        setHardness(1);
        setResistance(5);
        setCreativeTab(AtomicScience.creativeTab);
        setTranslationKey(AtomicScience.PREFIX + "steam.generator");
        setRegistryName(AtomicScience.PREFIX + "steam_turbine");
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (player.getHeldItem(hand).getItem() == Items.STICK)
        {
            if (!world.isRemote)
            {
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof TileEntitySteamGenerator)
                {
                    player.sendMessage(new TextComponentString("Steam: "
                            + ((TileEntitySteamGenerator) tile).getSteamGeneration()
                            + "mb Power: "
                            + ((TileEntitySteamGenerator) tile).getPowerToOutput() + "watts"));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        if (meta == 1)
        {
            return rfFactory != null ? rfFactory.get() : new TileEntitySteamGenerator();
        }
        else if (meta == 2)
        {
            return euFactory != null ? euFactory.get() : new TileEntitySteamGenerator();
        }
        return new TileEntitySteamGenerator();
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
