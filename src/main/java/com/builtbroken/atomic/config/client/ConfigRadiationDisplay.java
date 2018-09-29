package com.builtbroken.atomic.config.client;

import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */

public class ConfigRadiationDisplay
{
    @Config.Name("enable")
    @Config.Comment("Set to false to disable in-game radiation hud.")
    @Config.LangKey("config.atomicscience:client.radiation.display.enable.title")
    public boolean ENABLE = true;
}
