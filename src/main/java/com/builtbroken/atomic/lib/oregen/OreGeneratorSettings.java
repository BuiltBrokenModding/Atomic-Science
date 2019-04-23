package com.builtbroken.atomic.lib.oregen;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/1/2017.
 */
public class OreGeneratorSettings
{
    public int minGenerateLevel;
    public int maxGenerateLevel;
    public int amountPerChunk;
    public int amountPerBranch;

    public Block replaceBlock = Blocks.STONE;

    public OreGeneratorSettings(int min, int max, int amountPerBranch, int amountPerChunk)
    {
        this.minGenerateLevel = min;
        this.maxGenerateLevel = max;
        this.amountPerBranch = amountPerBranch;
        this.amountPerChunk = amountPerChunk;
    }
}
