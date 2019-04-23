package com.builtbroken.atomic.config.mods;

import net.minecraftforge.common.config.Config;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/14/2018.
 */
public class ConfigActuallyAdditions
{
    @Config.Name("enable_recipes")
    @Config.Comment("Enable recipes that offer alternatives using actual additions items")
    @Config.LangKey("config.atomicscience:mods.actually.additions.recipes.title")
    public boolean ENABLE_RECIPES = true;
}
