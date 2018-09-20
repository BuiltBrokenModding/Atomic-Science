package com.builtbroken.atomic.config.mods;

import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/14/2018.
 */
public class ConfigBC
{
    @Config.Name("mj_per_fe")
    @Config.Comment("How much (FE) Forge energy to exchange for (MJ) builcraft energy")
    @Config.RangeInt(min = 0)
    public double FE_PER_MJ = 10;

    @Config.Name("enable_buildcraft")
    @Config.Comment("Set to true to enable buildcraft (MJ) power support. Requires restart to take full effect.")
    public boolean ENABLE_BUILDCRAFT = true;
}
