package com.builtbroken.atomic.config.server;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/19/2018.
 */
@Config(modid = AtomicScience.DOMAIN, name = AtomicScience.DOMAIN + "/server")
@Config.LangKey("config.atomicscience:server.title")
public class ConfigServer
{
    @Config.Name("network")
    @Config.LangKey("config.atomicscience:server.network.title")
    public static final ConfigNetwork NETWORK = new ConfigNetwork();

    //@Config.Name("thread")
    //@Config.LangKey("config.atomicscience:server.thread.title")
    public static final ConfigThread THREAD = new ConfigThread();
}
