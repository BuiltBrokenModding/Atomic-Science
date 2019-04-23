package com.builtbroken.atomic.config.server;

import net.minecraftforge.common.config.Config;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */

public class ConfigNetwork
{
    @Config.Name("boiling_particle_packet")
    @Config.Comment("Disables the boiling effect to help reduce network load")
    @Config.LangKey("config.atomicscience:server.network.boiling.title")
    public boolean BOILING_EFFECT = true;

    //@Config.Name("steam_particle_packet")
    //@Config.Comment("Disables the steam effect to help reduce network load")
    //@Config.LangKey("config.atomicscience:server.network.steam.title")
    //public boolean STEAM_EFFECT = true;
}
