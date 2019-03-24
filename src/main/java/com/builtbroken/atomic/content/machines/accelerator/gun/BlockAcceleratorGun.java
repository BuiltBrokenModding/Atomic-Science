package com.builtbroken.atomic.content.machines.accelerator.gun;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.accelerator.graph.AcceleratorNode;
import com.builtbroken.atomic.content.machines.accelerator.tube.TileEntityAcceleratorTube;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
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
                        if (tileEntity instanceof TileEntityAcceleratorTube)
                        {
                            AcceleratorNode node = new AcceleratorNode(pos,
                                    ((TileEntityAcceleratorTube) tileEntity).getDirection(),
                                    ((TileEntityAcceleratorTube) tileEntity).getConnectionType());
                            nodes.put(pos, node);

                            //Get all possible directions
                            for (EnumFacing facing : EnumFacing.HORIZONTALS)
                            {
                                BlockPos nextPos = pos.offset(facing);

                                //If we have not pathed, add to path list
                                if (!pathedPositions.contains(nextPos))
                                {
                                    positionsToPath.add(nextPos);
                                }
                                //If we have pathed, check for connection
                                else if (nodes.containsKey(nextPos))
                                {
                                    node.connect(nodes.get(nextPos));
                                }
                            }
                        }
                    }

                    int minX = nodes.keySet().stream().min(Comparator.comparingInt(pos -> pos.getX())).get().getX();
                    int minZ = nodes.keySet().stream().min(Comparator.comparingInt(pos -> pos.getZ())).get().getZ();

                    int maxX = nodes.keySet().stream().max(Comparator.comparingInt(pos -> pos.getX())).get().getX();
                    int maxZ = nodes.keySet().stream().max(Comparator.comparingInt(pos -> pos.getZ())).get().getZ();

                    int sizeX = Math.abs(maxX - minX) + 10;
                    int sizeY = Math.abs(maxZ - minZ) + 10;

                    if (sizeX == 0)
                    {
                        sizeX = 1;
                    }
                    if (sizeY == 0)
                    {
                        sizeY = 1;
                    }

                    System.out.println(minX + ", " + minZ + " - " + maxX + ", " + maxZ + "  " + sizeX + "x" + sizeY);

                    char[][] grid = new char[sizeX][sizeY];

                    nodes.keySet().forEach(blockPos -> {
                        int x = blockPos.getX() - minX + 2;
                        int z = blockPos.getZ() - minZ + 2;

                        System.out.println(blockPos);
                        System.out.println(x + ", " + z + "  " + grid.length + "x" + grid[0].length);

                        AcceleratorNode node = nodes.get(blockPos);
                        int connections = node.nodes.size();

                         node.nodes.forEach(n -> System.out.println("\t" + n.pos));

                        grid[x][z] = Character.forDigit(connections, 10);
                    });

                    for (int x = 0; x < grid.length; x++)
                    {
                        for (int z = 0; z < grid[x].length; z++)
                        {
                            char c = grid[x][z];
                            if (c == 0)
                            {
                                c = ' ';
                            }
                            System.out.print(c);
                        }
                        System.out.println();
                    }

                    playerIn.sendMessage(new TextComponentString("Tubes: " + nodes.size()));

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
