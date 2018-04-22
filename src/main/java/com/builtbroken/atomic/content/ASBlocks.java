package com.builtbroken.atomic.content;

import com.builtbroken.atomic.content.blocks.BlockRadioactiveDirt;
import com.builtbroken.atomic.content.blocks.BlockRadioactiveGrass;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
public class ASBlocks
{
    public static Block blockRadioactiveDirt;
    public static Block blockRadioactiveGrass;

    public static void register()
    {
        GameRegistry.registerBlock(blockRadioactiveDirt = new BlockRadioactiveDirt(), "radioactive_dirt");
        GameRegistry.registerBlock(blockRadioactiveGrass = new BlockRadioactiveGrass(), "radioactive_grass");
    }
}
