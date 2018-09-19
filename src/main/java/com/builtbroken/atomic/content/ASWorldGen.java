package com.builtbroken.atomic.content;

import com.builtbroken.atomic.config.content.ConfigContent;
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
        if (ConfigContent.URANIUM_ORE.ENABLE_URANIUM_ORE)
        {
            GameRegistry.registerWorldGenerator(
                    new OreGenReplace(ASBlocks.blockUraniumOre.getDefaultState(),
                            new OreGeneratorSettings(
                                    ConfigContent.URANIUM_ORE.URANIUM_ORE_MIN_Y,
                                    ConfigContent.URANIUM_ORE.URANIUM_ORE_MAX_Y,
                                    ConfigContent.URANIUM_ORE.URANIUM_ORE_BRANCH_SIZE,
                                    ConfigContent.URANIUM_ORE.URANIUM_ORE_COUNT
                            ),
                            "pickaxe",
                            ConfigContent.URANIUM_ORE.URANIUM_ORE_HARVEST_LEVEL),
                    1);
        }
    }
}
