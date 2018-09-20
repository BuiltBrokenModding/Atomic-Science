package com.builtbroken.atomic.config.logic;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/27/2018.
 */

@Config(modid = AtomicScience.DOMAIN, name = AtomicScience.DOMAIN + "/logic")
@Config.LangKey("config.atomicscience:logic.title")
public class ConfigLogic
{
    @Config.LangKey("config.atomicscience:thermal.steam.title")
    public static final ConfigSteam STEAM = new ConfigSteam();
}
