package com.builtbroken.atomic.proxy;

import net.minecraftforge.fml.common.Loader;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/16/2018.
 */
public enum Mods
{
    IC2("ic2"),
    THERMAL_FOUNDATION("thermalfoundation"),
    THERMAL_EXPANSION("thermalexpansion"),
    ACTUALLY_ADDITIONS("actuallyadditions");

    public final String id;


    Mods(String id)
    {
        this.id = id;
    }

    public String ID()
    {
        return id;
    }

    public boolean isLoaded()
    {
        return Loader.isModLoaded(ID());
    }
}
