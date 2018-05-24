package com.builtbroken.atomic.config;

import com.builtbroken.atomic.proxy.ProxyLoader;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/24/2018.
 */
public class ConfigProxy extends ProxyLoader
{
    public ConfigProxy()
    {
        super("config");
        add(new ConfigClient());
        add(new ConfigMain());
        add(new ConfigNetwork());
        add(new ConfigOre());
        add(new ConfigPower());
        add(new ConfigRadiation());
        add(new ConfigRecipe());
    }
}
