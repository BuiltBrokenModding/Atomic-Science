package com.builtbroken.atomic.content.machines.steam.funnel;

import com.builtbroken.atomic.AtomicScience;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
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
        super(Material.iron);
        setHardness(1);
        setResistance(5);
        setCreativeTab(AtomicScience.creativeTab);
        setBlockName(AtomicScience.PREFIX + "steam.funnel");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit)
    {
        if (player.getHeldItem() != null && player.getHeldItem().getItem() == Items.stick)
        {
            if (!world.isRemote)
            {
                TileEntity tile = world.getTileEntity(x, y, z);
                if (tile instanceof TileEntitySteamFunnel)
                {
                    player.addChatComponentMessage(new ChatComponentText("Steam: "
                            + ((TileEntitySteamFunnel) tile).getFluidAmount()
                            + "mb + "
                            + ((TileEntitySteamFunnel) tile).getSteamGeneration() + "mb"));
                }
            }
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == 0)
        {
            return Blocks.cauldron.getIcon(0, 0);
        }
        else if (side == 1)
        {
            return Blocks.anvil.getIcon(0, 0);
        }
        return Blocks.iron_block.getIcon(0, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        //We pull icons from other blocks
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntitySteamFunnel();
    }
}
