package com.builtbroken.atomic.lib.oregen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.util.EnumFacing;
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
    public void generate(World world, Random random, int varX, int varZ)
    {
        int blocksPlaced = 0;
        while (blocksPlaced < settings.amountPerChunk)
        {
            int x = varX + random.nextInt(16);
            int z = varZ + random.nextInt(16);
            int y = random.nextInt(Math.max(settings.maxGenerateLevel - settings.minGenerateLevel, 0)) + settings.minGenerateLevel;
            int placed = this.generateBranch(world, random, varX, varZ, x, y, z);
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
    public int generateBranch(World world, Random rand, int chunkCornerX, int chunkCornerZ, int varX, int varY, int varZ)
    {
        int blocksPlaced = 0;
        //Positions already pathed
        List<BlockPos> pathed = new ArrayList();
        //Positions to path next
        Queue<BlockPos> toPath = new LinkedList();

        //First location to path
        toPath.add(new BlockPos(varX, varY, varZ));

        List<EnumFacing> directions = new ArrayList();
        for (EnumFacing dir : EnumFacing.VALUES)
        {
            directions.add(dir);
        }

        //Breadth first search
        while (!toPath.isEmpty() && blocksPlaced < settings.amountPerBranch)
        {
            BlockPos next = toPath.poll();
            pathed.add(next);

            //Place block
            IBlockState blockState = world.getBlockState(next);
            Block block = blockState.getBlock();
            if (settings.replaceBlock == null || block == settings.replaceBlock)
            {
                if (world.setBlockState(next, oreBlock, 2))
                {
                    blocksPlaced += 1;
                }
            }

            //Find new locations to place blocks
            Collections.shuffle(directions);
            for (EnumFacing direction : directions)
            {
                BlockPos pos = next.add(direction.getDirectionVec());
                if (!pathed.contains(pos) && world.rand.nextBoolean())
                {
                    if (pos.getY() >= 0 && pos.getY() < world.getHeight() && world.isBlockLoaded(pos))
                    {
                        boolean insideX = pos.getX() >= chunkCornerX && pos.getX() < (chunkCornerX + 16);
                        boolean insideZ = pos.getZ() >= chunkCornerZ && pos.getZ() < (chunkCornerZ + 16);
                        boolean insideY = pos.getY() >= settings.minGenerateLevel && pos.getY() <= settings.maxGenerateLevel;
                        if (insideX && insideZ && insideY)
                        {
                            blockState = world.getBlockState(next);
                            block = blockState.getBlock();
                            if (settings.replaceBlock == null || block == settings.replaceBlock)
                            {
                                toPath.add(pos);
                            }
                        }
                    }

                    if (!toPath.contains(pos))
                    {
                        pathed.add(pos);
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
