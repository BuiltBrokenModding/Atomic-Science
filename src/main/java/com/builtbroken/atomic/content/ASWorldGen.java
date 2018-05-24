package com.builtbroken.atomic.content;

import com.builtbroken.atomic.lib.oregen.OreGenReplace;
import com.builtbroken.atomic.lib.oregen.OreGeneratorSettings;
import com.builtbroken.atomic.proxy.ContentProxy;
import cpw.mods.fml.common.registry.GameRegistry;

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
        GameRegistry.registerWorldGenerator(
                new OreGenReplace(ASBlocks.blockUraniumOre, 0,
                        new OreGeneratorSettings(10, 40, 3, 20),
                        "pickaxe",
                        1),
                1);
    }
}
