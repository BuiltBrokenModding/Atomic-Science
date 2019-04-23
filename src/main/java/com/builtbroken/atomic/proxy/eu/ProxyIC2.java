package com.builtbroken.atomic.proxy.eu;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.lib.power.PowerSystem;
import com.builtbroken.atomic.proxy.ContentProxy;
import com.builtbroken.atomic.proxy.ModProxy;
import com.builtbroken.atomic.proxy.Mods;

/**
 * Proxy for Redstone Flux API
 * <p>
 * Handles seperate from mod interaction due to the API being broadly used by several mods.
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class ProxyIC2 extends ModProxy
{
    public ProxyIC2()
    {
        super("IndustrialCraft", Mods.IC2);
    }

    @Override
    public boolean shouldLoad()
    {
        return super.shouldLoad();
    }

    @Override
    public void preInit()
    {
        AtomicScience.logger.info(this + " Loaded");
        PowerSystem.register(PowerHandlerEU.INSTANCE);
    }
}
