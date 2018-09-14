package com.builtbroken.atomic.config.mods;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/14/2018.
 */
@Config(modid = AtomicScience.DOMAIN, name = "mods/bc")
@Config.LangKey("config.atomicscience:mods.bc.title")
public class ConfigBC
{
    @Config.Name("mj_per_fe")
    @Config.Comment("How much (FE) Forge energy to exchange for (MJ) builcraft energy")
    @Config.RangeInt(min = 0)
    public static double FE_PER_MJ = 10;

    @Config.Name("enable_buildcraft")
    @Config.Comment("Set to true to enable buildcraft (MJ) power support. Requires restart to take full effect.")
    public static boolean ENABLE_BUILDCRAFT = true;
}
