package com.builtbroken.atomic.proxy.rf;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.power.BlockPowerBus;
import com.builtbroken.atomic.content.machines.steam.generator.BlockSteamGenerator;
import com.builtbroken.atomic.content.machines.steam.generator.TileEntitySteamGenRF;
import com.builtbroken.atomic.lib.power.PowerSystem;
import com.builtbroken.atomic.proxy.ContentProxy;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Proxy for Redstone Flux API
 * <p>
 * Handles seperate from mod interaction due to the API being broadly used by several mods.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/17/2018.
 */
public class ProxyRedstoneFlux extends ContentProxy
{

    public static int conversationRateToRF = 1;

    public ProxyRedstoneFlux()
    {
        super("RF Power API");
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
        PowerSystem.register(new PowerHandlerRFTile());

        if (doesClassExist("cofh.api.energy.IEnergyContainerItem"))
        {
            PowerSystem.register(new PowerHandlerRFItem());
        }

        BlockSteamGenerator.rfFactory = () -> new TileEntitySteamGenRF();
        GameRegistry.registerTileEntity(TileEntitySteamGenRF.class, AtomicScience.PREFIX + "steam_turbine_rf");

        BlockPowerBus.rfFactory = () -> new TileEntityPowerBusRF();
        GameRegistry.registerTileEntity(TileEntityPowerBusRF.class, AtomicScience.PREFIX + "power_bus_rf");
    }


    public static int toRF(int fromUE)
    {
        return fromUE * conversationRateToRF;
    }

    public static int toUE(int fromPower)
    {
        return fromPower / conversationRateToRF;
    }
}
