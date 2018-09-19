package com.builtbroken.atomic.config.mods;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/14/2018.
 */
@Config(modid = AtomicScience.DOMAIN, name = AtomicScience.DOMAIN + "/mods/te")
@Config.LangKey("config.atomicscience:mods.te.title")
public class ConfigThermalExpansion
{
    @Config.Name("enable_recipes")
    @Config.Comment("Enable recipes that offer alternatives using thermal expansion items")
    public static boolean ENABLE_RECIPES = true;
}
