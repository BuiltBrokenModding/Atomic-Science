package com.builtbroken.atomic.config.logic;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/27/2018.
 */

@Config(modid = AtomicScience.DOMAIN, name = AtomicScience.DOMAIN + "/logic")
@Config.LangKey("config.atomicscience:logic.title")
public class ConfigLogic
{
    @Config.LangKey("config.atomicscience:thermal.steam.title")
    public static final ConfigSteam STEAM = new ConfigSteam();
}
