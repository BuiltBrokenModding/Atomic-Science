package com.builtbroken.atomic.proxy;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
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
