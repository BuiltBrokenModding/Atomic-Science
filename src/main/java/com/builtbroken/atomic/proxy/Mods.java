package com.builtbroken.atomic.proxy;

import net.minecraftforge.fml.common.Loader;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/16/2018.
 */
public enum Mods
{
    IC2("ic2"),
    THERMAL_FOUNDATION("thermalfoundation"),
    THERMAL_EXPANSION("thermalexpansion"),
    ACTUALLY_ADDITIONS("actuallyadditions"),
    BUILDCRAFT_CORE("buildcraftcore"),
    BUILDCRAFT_BUILDER("buildcraftbuilders"),
    BUILDCRAFT_ENERGY("buildcraftenergy"),
    BUILDCRAFT_FACTORY("buildcraftfactory"),
    BUILDCRAFT_ROBOTICS("buildcraftrobotics"),
    BUILDCRAFT_SILICON("buildcraftsilicon"),
    BUILDCRAFT_TRANSPORT("buildcrafttransport");

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
