package com.builtbroken.atomic.content.machines.reactor.pipe;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/10/2018.
 */
public class BlockRodPipe extends Block implements ITileEntityProvider
{
    public static final PropertyBool HAS_INVENTORY = PropertyBool.create("slot");

    public BlockRodPipe()
    {
        super(Material.IRON);
        setRegistryName(AtomicScience.PREFIX + "rod_pipe");
        setTranslationKey(AtomicScience.PREFIX + "pipe.rod");
        setCreativeTab(AtomicScience.creativeTab);
        setHardness(10);
        setResistance(10);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityRodPipeInv)
        {
            if (!worldIn.isRemote)
            {
                playerIn.openGui(AtomicScience.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        if (meta == 1)
        {
            return new TileEntityRodPipeInv();
        }
        return new TileEntityRodPipe();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, HAS_INVENTORY);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(HAS_INVENTORY, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(HAS_INVENTORY) ? 1 : 0;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return getDefaultState().withProperty(HAS_INVENTORY, meta == 1);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
        {
            IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (inventory != null)
            {
                for (int i = 0; i < inventory.getSlots(); ++i)
                {
                    ItemStack itemstack = inventory.getStackInSlot(i);

                    if (!itemstack.isEmpty())
                    {
                        InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
                    }
                }
            }
        }
        super.breakBlock(world, pos, state);
    }

}
