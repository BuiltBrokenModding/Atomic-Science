package com.builtbroken.atomic.content;

import com.builtbroken.atomic.config.content.ConfigOre;
import com.builtbroken.atomic.lib.oregen.OreGenReplace;
import com.builtbroken.atomic.lib.oregen.OreGeneratorSettings;
import com.builtbroken.atomic.proxy.ContentProxy;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/24/2018.
 */
public class ASWorldGen extends ContentProxy
{
    public ASWorldGen()
    {
        super("world.gen");
    }

    @Override
    public void init()
    {
        if (ConfigOre.ENABLE_URANIUM_ORE)
        {
            GameRegistry.registerWorldGenerator(
                    new OreGenReplace(ASBlocks.blockUraniumOre.getDefaultState(),
                            new OreGeneratorSettings(
                                    ConfigOre.URANIUM_ORE_MIN_Y,
                                    ConfigOre.URANIUM_ORE_MAX_Y,
                                    ConfigOre.URANIUM_ORE_BRANCH_SIZE,
                                    ConfigOre.URANIUM_ORE_COUNT
                            ),
                            "pickaxe",
                            ConfigOre.URANIUM_ORE_HARVEST_LEVEL),
                    1);
        }
    }
}
