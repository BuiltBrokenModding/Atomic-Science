package com.builtbroken.atomic.config.mods;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/14/2018.
 */
@Config(modid = AtomicScience.DOMAIN, name = "mods/ic2")
@Config.LangKey("config.atomicscience:mods.ic2.title")
public class ConfigIC2
{
    @Config.Name("enable_recipes")
    @Config.Comment("Enable recipes that offer alternatives using IC2 items")
    public static boolean ENABLE_RECIPES = true;

    @Config.Name("fe_per_eu")
    @Config.Comment("How much (FE) Forge energy to exchange for (EU) IC2 energy")
    @Config.RangeInt(min = 0)
    public static double FE_PER_EU = 4; //Matched with Mekanism

    @Config.Name("enable_ic2")
    @Config.Comment("Set to true to enable IC2 (EU) power support. Requires restart to take full effect.")
    public static boolean ENABLE_IC2 = true;
}
