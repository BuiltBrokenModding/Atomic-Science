package com.builtbroken.atomic.config.server;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/19/2018.
 */
@Config(modid = AtomicScience.DOMAIN, name = AtomicScience.DOMAIN + "/server")
@Config.LangKey("config.atomicscience:server.title")
public class ConfigServer
{
    @Config.Name("network")
    @Config.LangKey("config.atomicscience:server.network.title")
    public static final ConfigNetwork NETWORK = new ConfigNetwork();
}
