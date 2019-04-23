package com.builtbroken.atomic.config.client;

import com.builtbroken.atomic.AtomicScience;
import net.minecraftforge.common.config.Config;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/19/2018.
 */
@Config(modid = AtomicScience.DOMAIN, name = AtomicScience.DOMAIN + "/client")
@Config.LangKey("config.atomicscience:client.title")
public class ConfigClient
{
    @Config.LangKey("config.atomicscience:client.particles.title")
    @Config.Name("particles")
    public static final ConfigParticles PARTICLES = new ConfigParticles();

    @Config.LangKey("config.atomicscience:client.radiation.display.title")
    @Config.Name("radiation_hud")
    public static final ConfigRadiationDisplay RADIATION_DISPLAY = new ConfigRadiationDisplay();
}
