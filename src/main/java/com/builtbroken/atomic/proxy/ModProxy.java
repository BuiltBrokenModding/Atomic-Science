package com.builtbroken.atomic.proxy;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class ModProxy extends ContentProxy
{
    public final Mods mod;

    public ModProxy(String name, Mods mod)
    {
        super(name);
        this.mod = mod;
    }

    @Override
    public boolean shouldLoad()
    {
        return super.shouldLoad() && mod.isLoaded();
    }
}
