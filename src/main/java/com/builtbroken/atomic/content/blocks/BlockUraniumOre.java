package com.builtbroken.atomic.content.blocks;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2018.
 */
public class BlockUraniumOre extends Block
{
    public BlockUraniumOre()
    {
        super(Material.rock);
        setHardness(3f);
        setResistance(5f);
        setStepSound(soundTypePiston);
        setCreativeTab(AtomicScience.creativeTab);
        setBlockName(AtomicScience.PREFIX + "ore.uranium");
        setBlockTextureName(AtomicScience.PREFIX + "ore_uranium");
        OreDictionary.registerOre("oreUranium", this);
    }
}
