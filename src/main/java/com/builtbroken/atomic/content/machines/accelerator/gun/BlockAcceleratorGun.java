package com.builtbroken.atomic.content.machines.accelerator.gun;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import com.builtbroken.atomic.content.machines.accelerator.tube.TileEntityAcceleratorTube;
import com.builtbroken.atomic.content.prefab.BlockPrefab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/15/2018.
 */
public class BlockAcceleratorGun extends BlockPrefab
{
    public static final PropertyDirection ROTATION_PROP = PropertyDirection.create("rotation");

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
                    final EnumFacing direction = ((TileEntityAcceleratorGun) gunTile).getDirection();
                    final Set<BlockPos> pathedPositions = new HashSet();

                    final Stack<BlockPos> positionsToPath = new Stack();
                    positionsToPath.push(blockClickPos.offset(direction));

                    HashMap<BlockPos, AcceleratorNode> nodes = new HashMap();

                    while (!positionsToPath.isEmpty())
                    {
                        //Get next pos
                       final BlockPos pos = positionsToPath.pop();

                       //Add to pathed so we don't path again
                       pathedPositions.add(pos);

                       //Check for tube at position
                       final TileEntity tileEntity = world.getTileEntity(pos);
                       if(tileEntity instanceof TileEntityAcceleratorTube)
                       {
                            AcceleratorNode node = new AcceleratorNode(((TileEntityAcceleratorTube) tileEntity).getDirection(), ((TileEntityAcceleratorTube) tileEntity).getConnectionType());
                            nodes.put(pos, node);

                           //Get all possible directions
                           for(EnumFacing facing : EnumFacing.HORIZONTALS)
                           {
                               BlockPos nextPos = pos.offset(facing);

                               //If we have not pathed, add to path list
                               if(!pathedPositions.contains(nextPos))
                               {
                                   positionsToPath.add(nextPos);
                               }
                               //If we have pathed, check for connection
                               else if(nodes.containsKey(nextPos))
                               {
                                   node.connect(nodes.get(nextPos));
                               }
                           }
                       }
                    }

                    playerIn.sendMessage(new TextComponentString("Tubes: " + nodes.size()));

                }
                return true;
            }

        }

        return false;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, ROTATION_PROP);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(ROTATION_PROP, EnumFacing.byIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(ROTATION_PROP).ordinal();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return getStateFromMeta(meta).withProperty(ROTATION_PROP, placer.getHorizontalFacing());
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityAcceleratorGun();
    }
}
