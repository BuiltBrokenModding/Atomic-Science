package com.builtbroken.atomic.config.mods;

import net.minecraftforge.common.config.Config;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/14/2018.
 */
public class ConfigThermalExpansion
{
    @Config.Name("enable_recipes")
    @Config.Comment("Enable recipes that offer alternatives using thermal expansion items")
    @Config.LangKey("config.atomicscience:mods.thermal.expansion.recipes.title")
    public boolean ENABLE_RECIPES = true;
}
