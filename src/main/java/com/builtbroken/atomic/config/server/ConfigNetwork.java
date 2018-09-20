package com.builtbroken.atomic.config.server;

import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */

public class ConfigNetwork
{
    @Config.Name("boiling_particle_packet")
    @Config.Comment("Disables the boiling effect to help reduce network load")
    public static boolean BOILING_EFFECT = true;

    //@Config.Name("steam_particle_packet")
    //@Config.Comment("Disables the steam effect to help reduce network load")
    public static boolean STEAM_EFFECT = true;
}
