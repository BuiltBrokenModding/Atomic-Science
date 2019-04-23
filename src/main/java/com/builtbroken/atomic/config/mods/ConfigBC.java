package com.builtbroken.atomic.config.mods;

import net.minecraftforge.common.config.Config;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/14/2018.
 */
public class ConfigBC
{
    @Config.Name("fe_per_mj")
    @Config.Comment("How much (FE) Forge energy to exchange for (MJ) builcraft energy")
    @Config.LangKey("config.atomicscience:mods.buildcraft.fe_per_mj.title")
    @Config.RangeInt(min = 0)
    public double FE_PER_MJ = 10; //TODO check value

    @Config.Name("enable_buildcraft")
    @Config.Comment("Set to true to enable buildcraft (MJ) power support. Requires restart to take full effect.")
    @Config.LangKey("config.atomicscience:mods.buildcraft.power.title")
    public boolean ENABLE_BUILDCRAFT = true;
}
