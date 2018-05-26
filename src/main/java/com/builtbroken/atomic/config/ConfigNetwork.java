package com.builtbroken.atomic.config;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.proxy.ContentProxy;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */
public class ConfigNetwork extends ContentProxy
{
    public static boolean BOILING_EFFECT = true;
    public static boolean STEAM_EFFECT = true;

    public ConfigNetwork()
    {
        super("config.network");
    }

    @Override
    public void preInit()
    {
        Configuration configuration = new Configuration(new File(AtomicScience.configFolder, "Network.cfg"), AtomicScience.VERSION);
        configuration.load();
        BOILING_EFFECT = configuration.getBoolean("boiling_effect", "particles", BOILING_EFFECT, "Disables the boiling effect to help reduce network load");
        STEAM_EFFECT = configuration.getBoolean("steam_effect", "particles", BOILING_EFFECT, "Disables the steam effect to help reduce network load");
        configuration.save();
    }
}
