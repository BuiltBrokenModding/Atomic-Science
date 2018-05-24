package com.builtbroken.atomic.lib.oregen;

import com.builtbroken.atomic.lib.transform.vector.Pos;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraftforge.common.util.ForgeDirection;

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
     * @param meta         - meta of block to place
     * @param settings     - controls spawn conditions
     * @param harvestLevel
     * @param harvestTool
     */
    public OreGenReplace(Block block, int meta, OreGeneratorSettings settings, String harvestTool, int harvestLevel)
    {
        super(block, meta, harvestTool, harvestLevel);
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
        List<Pos> pathed = new ArrayList();
        //Positions to path next
        Queue<Pos> toPath = new LinkedList();

        //First location to path
        toPath.add(new Pos(varX, varY, varZ));

        List<ForgeDirection> directions = new ArrayList();
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            directions.add(dir);
        }

        //Breadth first search
        while (!toPath.isEmpty() && blocksPlaced < settings.amountPerBranch)
        {
            Pos next = toPath.poll();
            pathed.add(next);

            //Place block
            Block block = next.getBlock(world);
            if (settings.replaceBlock == null || block == settings.replaceBlock)
            {
                if (next.setBlock(world, oreBlock, oreMeta, 2))
                {
                    blocksPlaced += 1;
                }
            }

            //Find new locations to place blocks
            Collections.shuffle(directions);
            for (ForgeDirection direction : directions)
            {
                Pos pos = next.add(direction);
                if (!pathed.contains(pos) && world.rand.nextBoolean())
                {
                    if (pos.isInsideMap() && world.blockExists(pos.xi(), pos.yi(), pos.zi()))
                    {
                        boolean insideX = pos.xi() >= chunkCornerX && pos.xi() < (chunkCornerX + 16);
                        boolean insideZ = pos.zi() >= chunkCornerZ && pos.zi() < (chunkCornerZ + 16);
                        boolean insideY = pos.yi() >= settings.minGenerateLevel && pos.yi() <= settings.maxGenerateLevel;
                        if (insideX && insideZ && insideY)
                        {
                            block = pos.getBlock(world);
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
    public boolean isOreGeneratedInWorld(World world, IChunkProvider chunkGenerator)
    {
        if (this.ignoreSurface && chunkGenerator instanceof ChunkProviderGenerate)
        {
            return false;
        }
        if (this.ignoreNether && chunkGenerator instanceof ChunkProviderHell)
        {
            return false;
        }
        return !(this.ignoreEnd && chunkGenerator instanceof ChunkProviderEnd);
    }
}
