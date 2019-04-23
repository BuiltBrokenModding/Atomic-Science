package com.builtbroken.atomic.content;

import com.builtbroken.atomic.config.content.ConfigContent;
import com.builtbroken.atomic.lib.oregen.OreGenReplace;
import com.builtbroken.atomic.lib.oregen.OreGeneratorSettings;
import com.builtbroken.atomic.proxy.ContentProxy;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 *
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
            if(ConfigContent.URANIUM_ORE.URANIUM_ORE_MIN_Y > ConfigContent.URANIUM_ORE.URANIUM_ORE_MAX_Y)
            {
                throw new RuntimeException("AtomicScience: Uranium ore min spawn height must be smaller or equal to max spawn height");
            }

            if(ConfigContent.URANIUM_ORE.URANIUM_ORE_MIN_Y < 0)
            {
                throw new RuntimeException("AtomicScience: Uranium ore min spawn height must be greater than or equal to zero");
            }

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
