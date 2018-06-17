package com.builtbroken.atomic.config;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.proxy.ContentProxy;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Main config for the mod
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
public class ConfigMain extends ContentProxy
{
    public static boolean ENABLE_STEAM = true;

    public ConfigMain()
    {
        super("config.main");
    }

    @Override
    public void preInit()
    {
        Configuration configuration = new Configuration(new File(AtomicScience.configFolder, "Main.cfg"), AtomicScience.VERSION);
        configuration.load();
        ENABLE_STEAM = configuration.getBoolean("enable_steam", "content", ENABLE_STEAM,
                "Set to false to disable the build in steam fluid and block. " +
                        "Use this to allow other mods to take priority for creating the block.");
        configuration.save();
    }
}
