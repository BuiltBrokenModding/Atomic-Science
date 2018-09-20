package com.builtbroken.atomic.lib.oregen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * This class is used for storing ore generation data. If you are too lazy to generate your own
 * ores, you can do to add your ore to the list of ores to generate.
 *
 * @author Calclavia
 */
public abstract class OreGenerator implements IWorldGenerator
{
	public IBlockState oreBlock;

	/**
	 * What harvest level does this machine need to be acquired?
	 */
	public int harvestLevel;

	/**
	 * The predefined tool classes are "pickaxe", "shovel", "axe". You can add others for custom
	 * tools.
	 */
	public String harvestTool;

	public OreGenerator(IBlockState block, String harvestTool, int harvestLevel)
	{
		this.harvestTool = harvestTool;
		this.harvestLevel = harvestLevel;
		this.oreBlock = block;
		block.getBlock().setHarvestLevel(this.harvestTool, this.harvestLevel);
	}

	public abstract void generate(World world, Random random, int varX, int varZ, int chunkPosX, int chunkPosZ);

	public abstract boolean isOreGeneratedInWorld(World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider);

	@Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		int varX = chunkX << 4;
		int varZ = chunkZ << 4;

		// Checks to make sure this is the normal world

		if (isOreGeneratedInWorld(world, chunkGenerator, chunkProvider))
		{
			generate(world, world.rand, varX, varZ, chunkX, chunkZ);
		}
	}
}
