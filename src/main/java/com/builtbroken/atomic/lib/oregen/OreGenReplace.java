package com.builtbroken.atomic.lib.oregen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.IChunkGenerator;

import java.util.*;

/**
 * Handles generating ores in the world
 *
 * @author Calclavia, DarkCow
 */
public class OreGenReplace extends OreGenerator
{
    public final OreGeneratorSettings settings;
    /**
     * Dimensions to ignore ore generation
     */
    public boolean ignoreSurface = false;
    public boolean ignoreNether = true;
    public boolean ignoreEnd = true;

    /**
     * @param block        -block to place
     * @param settings     - controls spawn conditions
     * @param harvestLevel
     * @param harvestTool
     */
    public OreGenReplace(IBlockState block, OreGeneratorSettings settings, String harvestTool, int harvestLevel)
    {
        super(block, harvestTool, harvestLevel);
        this.settings = settings;
    }

    @Override
    public void generate(World world, Random random, int varX, int varZ, int chunkPosX, int chunkPosZ)
    {
        int blocksPlaced = 0;
        while (blocksPlaced < settings.amountPerChunk)
        {
            //Generate near center to prevent hitting edge of chunk
            int x = varX + 4 + random.nextInt(7);
            int z = varZ + 4 + random.nextInt(7);

            int y = random.nextInt(Math.max(settings.maxGenerateLevel - settings.minGenerateLevel, 0)) + settings.minGenerateLevel;
            int placed = this.generateBranch(world, random, varX, varZ, x, y, z, chunkPosX, chunkPosZ);
            if (placed <= 0)
            {
                placed = settings.amountPerBranch; //Prevents inf loop
            }
            blocksPlaced += placed;
        }
    }

    /**
     * Picks a random location in the chunk based on a random rotation and Y value
     *
     * @param world - world
     * @param rand  - random
     * @param varX  - randomX
     * @param varY  - randomY
     * @param varZ  - randomZ
     * @return true if it placed blocks
     */
    public int generateBranch(World world, Random rand, int chunkCornerX, int chunkCornerZ, int varX, int varY, int varZ, int chunkPosX, int chunkPosZ)
    {
        final int min_y = Math.max(0, settings.minGenerateLevel);
        final int max_y = Math.min(world.getHeight(), settings.maxGenerateLevel);
        final BlockPos start = new BlockPos(varX, varY, varZ);

        //System.out.println("Starting Branch: " + start + " [" + chunkCornerX + ", " + chunkPosZ + "]");

        int blocksPlaced = 0;
        //Positions already pathed
        List<BlockPos> pathed = new ArrayList();
        //Positions to path next
        Queue<BlockPos> toPath = new LinkedList();

        //First location to path
        toPath.add(start);

        List<EnumFacing> directions = new ArrayList();
        for (EnumFacing dir : EnumFacing.VALUES)
        {
            directions.add(dir);
        }

        //Breadth first search
        while (!toPath.isEmpty() && blocksPlaced < settings.amountPerBranch)
        {
            BlockPos currentPathPosition = toPath.poll();
            pathed.add(currentPathPosition);

            //System.out.println("--Pathing: " + currentPathPosition);

            //Ensure block exists before pathing to prevent creating chunks
            if (world.isBlockLoaded(currentPathPosition))
            {
                //Place block
                IBlockState blockState = world.getBlockState(currentPathPosition);
                Block block = blockState.getBlock();
                //TODO implement   if (state.getBlock().isReplaceableOreGen(state, worldIn, blockpos, this.predicate))
                if (settings.replaceBlock == null || block == settings.replaceBlock)
                {
                    //System.out.println("----Placing block " + currentPathPosition + " [" + (currentPathPosition.getX() >> 4) + ", " + (currentPathPosition.getZ() >> 4) + "]");
                    if (world.setBlockState(currentPathPosition, oreBlock, 2))
                    {
                        blocksPlaced += 1;
                    }
                }

                //Shuffle directions to create random paths (removing this will cause lines of ore)
                Collections.shuffle(directions);

                //Find new locations to place blocks
                for (EnumFacing direction : directions)
                {
                    BlockPos nextPathPosition = currentPathPosition.add(direction.getDirectionVec());

                    //System.out.println("----Pathing: " + nextPathPosition + " [" + (nextPathPosition.getX() >> 4) + ", " + (nextPathPosition.getZ() >> 4) + "]");
                    if (!pathed.contains(nextPathPosition) && rand.nextBoolean())
                    {
                        //Make sure we have not left our chunk
                        if (nextPathPosition.getX() >> 4 == chunkPosX && nextPathPosition.getZ() >> 4 == chunkPosZ)
                        {
                            //Keep position inside world and ensure block is loaded to prevent creating chunks
                            if (nextPathPosition.getY() >= min_y && nextPathPosition.getY() < max_y && world.isBlockLoaded(nextPathPosition))
                            {
                                if(nextPathPosition.getX() % 16 != 15 && nextPathPosition.getZ() % 16 != 15
                                && nextPathPosition.getX() % 16 != 0 && nextPathPosition.getZ() % 16 != 0)
                                {
                                    //System.out.println("-----Checking: " + nextPathPosition + " [" + (nextPathPosition.getX() >> 4) + ", " + (nextPathPosition.getZ() >> 4) + "]");

                                    blockState = world.getBlockState(nextPathPosition);
                                    block = blockState.getBlock();
                                    //TODO implement   if (state.getBlock().isReplaceableOreGen(state, worldIn, blockpos, this.predicate))
                                    if (settings.replaceBlock == null || block == settings.replaceBlock)
                                    {
                                        toPath.add(nextPathPosition);
                                    }
                                }
                            }
                        }

                        //Ignore position if not added to toPath list
                        if (!toPath.contains(nextPathPosition))
                        {
                            pathed.add(nextPathPosition);
                        }
                    }
                }
            }
        }
        return blocksPlaced;
    }

    @Override
    public boolean isOreGeneratedInWorld(World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (this.ignoreSurface && chunkGenerator instanceof ChunkGeneratorOverworld)
        {
            return false;
        }
        if (this.ignoreNether && chunkGenerator instanceof ChunkGeneratorHell)
        {
            return false;
        }
        return !(this.ignoreEnd && chunkGenerator instanceof ChunkGeneratorEnd);
    }
}
