package com.builtbroken.atomic.config.mods;

import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/14/2018.
 */
public class ConfigIC2
{
    @Config.Name("enable_recipes")
    @Config.Comment("Enable recipes that offer alternatives using IC2 items")
    @Config.LangKey("config.atomicscience:mods.ic2.recipes.title")
    public boolean ENABLE_RECIPES = true;

    @Config.Name("fe_per_eu")
    @Config.Comment("How much (FE) Forge energy to exchange for (EU) IC2 energy")
    @Config.LangKey("config.atomicscience:mods.ic2.fe_per_eu.title")
    @Config.RangeInt(min = 0)
    public double FE_PER_EU = 4; //Matched with Mekanism

    @Config.Name("enable_ic2")
    @Config.Comment("Set to true to enable IC2 (EU) power support. Requires restart to take full effect.")
    @Config.LangKey("config.atomicscience:mods.ic2.power.title")
    public boolean ENABLE_POWER = true;
}
