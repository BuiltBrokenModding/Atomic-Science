package com.builtbroken.atomic.content.machines.steam.funnel;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * Simple block that collects steam
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/16/2018.
 */
public class BlockSteamFunnel extends BlockContainer
{
    public BlockSteamFunnel()
    {
        super(Material.IRON);
        setHardness(1);
        setResistance(5);
        setCreativeTab(AtomicScience.creativeTab);
        setTranslationKey(AtomicScience.PREFIX + "steam.funnel");
        setRegistryName(AtomicScience.PREFIX + "steam_funnel");
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (player.getHeldItem(hand).getItem() == Items.STICK)
        {
            if (!world.isRemote)
            {
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof TileEntitySteamFunnel)
                {
                    player.sendMessage(new TextComponentString("Steam: "
                            + ((TileEntitySteamFunnel) tile).getFluidAmount()
                            + "mb + "
                            + ((TileEntitySteamFunnel) tile).getSteamGeneration() + "mb"));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntitySteamFunnel();
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
