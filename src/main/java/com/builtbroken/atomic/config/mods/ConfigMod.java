package com.builtbroken.atomic.config.mods;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/19/2018.
 */
@Config(modid = AtomicScience.DOMAIN, name = AtomicScience.DOMAIN + "/mods")
@Config.LangKey("config.atomicscience:mods.title")
public class ConfigMod
{
    @Config.LangKey("config.atomicscience:mods.ic2.title")
    @Config.Name("industrialcraft")
    public static final ConfigIC2 IC2 = new ConfigIC2();

    @Config.LangKey("config.atomicscience:mods.buildcraft.title")
    @Config.Name("buildcraft")
    public static final ConfigBC BUILDCRAFT = new ConfigBC();

    @Config.LangKey("config.atomicscience:mods.actually.additions.title")
    @Config.Name("actually_additions")
    public static final ConfigActuallyAdditions ACTUALLY_ADDITIONS = new ConfigActuallyAdditions();

    @Config.LangKey("config.atomicscience:mods.thermal.expansion.title")
    @Config.Name("thermal_expansion")
    public static final ConfigThermalExpansion THERMAL_EXPANSION = new ConfigThermalExpansion();
}
