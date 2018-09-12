package com.builtbroken.atomic.proxy;

import net.minecraftforge.fml.common.Loader;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class ModProxy extends ContentProxy
{
    public final String mod_id;

    public ModProxy(String name, String mod_id)
    {
        super(name);
        this.mod_id = mod_id;
    }

    @Override
    public boolean shouldLoad()
    {
        return super.shouldLoad() && Loader.isModLoaded(mod_id);
    }
}
