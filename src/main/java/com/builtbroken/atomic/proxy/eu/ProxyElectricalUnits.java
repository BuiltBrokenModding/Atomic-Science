package com.builtbroken.atomic.proxy.eu;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.tiles.steam.generator.BlockSteamGenerator;
import com.builtbroken.atomic.content.tiles.steam.generator.TileEntitySteamGenEU;
import com.builtbroken.atomic.proxy.ContentProxy;

/**
 * Proxy for Redstone Flux API
 * <p>
 * Handles seperate from mod interaction due to the API being broadly used by several mods.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class ProxyElectricalUnits extends ContentProxy
{
    public ProxyElectricalUnits()
    {
        super("EU Power API");
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
        //PowerSystem.register(new PowerHandlerEU());
        BlockSteamGenerator.euFactory = () -> new TileEntitySteamGenEU();
    }
}
