package com.builtbroken.atomic.content.blocks;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public class BlockUraniumOre extends Block
{
    public BlockUraniumOre()
    {
        super(Material.ROCK);
        setHardness(3f);
        setResistance(5f);
        setSoundType(SoundType.STONE);
        setCreativeTab(AtomicScience.creativeTab);
        setRegistryName(AtomicScience.PREFIX + "uranium_ore");
        setTranslationKey(AtomicScience.PREFIX + "ore.uranium");
    }
}
