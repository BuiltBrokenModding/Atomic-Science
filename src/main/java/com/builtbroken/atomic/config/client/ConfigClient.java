package com.builtbroken.atomic.config.client;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/19/2018.
 */
@Config(modid = AtomicScience.DOMAIN, name = AtomicScience.DOMAIN + "/client")
@Config.LangKey("config.atomicscience:client.title")
public class ConfigClient
{
    @Config.LangKey("config.atomicscience:client.particles.title")
    @Config.Name("particles")
    public static final ConfigParticles PARTICLES = new ConfigParticles();
}
