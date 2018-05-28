package com.builtbroken.atomic.config;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.proxy.ContentProxy;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */
public class ConfigClient extends ContentProxy
{
    public static boolean BOILING_EFFECT = true;
    public static boolean MACHINE_COMPLETE = true;
    public static boolean MACHINE_RUNNING = true;
    public static boolean STEAM_EFFECT = true;

    public ConfigClient()
    {
        super("config.client");
    }

    @Override
    public void preInit()
    {
        Configuration configuration = new Configuration(new File(AtomicScience.configFolder, "Client.cfg"), AtomicScience.VERSION);
        configuration.load();

        final String cat_particle = "particles";
        BOILING_EFFECT = configuration.getBoolean("boiling", cat_particle, BOILING_EFFECT, "Allows disabling the water boiling effect.");
        STEAM_EFFECT = configuration.getBoolean("steam", cat_particle, STEAM_EFFECT, "Allows disabling the water steam effect.");

        MACHINE_COMPLETE = configuration.getBoolean("machine_complete", cat_particle, MACHINE_COMPLETE, "Allows disabling the effect generated when machine finishes a recipe.");
        MACHINE_RUNNING = configuration.getBoolean("machine_running", cat_particle, MACHINE_RUNNING, "Allows disabling the effect generated when machine processing materials.");

        configuration.save();
    }
}
