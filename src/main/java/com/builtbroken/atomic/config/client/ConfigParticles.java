package com.builtbroken.atomic.config.client;

import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */

public class ConfigParticles
{
    @Config.Name("boiling")
    @Config.Comment("Allows disabling the water boiling effect.")
    @Config.LangKey("config.atomicscience:client.particles.boiling.title")
    public boolean BOILING_EFFECT = true;

    @Config.Name("machine_complete")
    @Config.Comment("Allows disabling the effect generated when machine finishes an action or recipe.")
    @Config.LangKey("config.atomicscience:client.particles.machine.complete.title")
    public boolean MACHINE_COMPLETE = true;

    @Config.Name("machine_running")
    @Config.Comment("Allows disabling the effect generated when machine processing materials.")
    @Config.LangKey("config.atomicscience:client.particles.machine.running.title")
    public boolean MACHINE_RUNNING = true;

    @Config.Name("reactor_running")
    @Config.Comment("Allows disabling the effect generated when reactor is running.")
    @Config.LangKey("config.atomicscience:client.particles.reactor.running.title")
    public boolean REACTOR_RUNNING = true;

    //@Config.Name("steam")
    //@Config.Comment("Allows disabling the water steam effect.")
    // @Config.LangKey("config.atomicscience:client.particles.steam.title")
    //public boolean STEAM_EFFECT = true;
}
