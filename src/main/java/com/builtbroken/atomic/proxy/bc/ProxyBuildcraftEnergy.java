package com.builtbroken.atomic.proxy.bc;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.TileEntityPowerInvMachine;
import com.builtbroken.atomic.content.machines.steam.generator.TileEntitySteamGenerator;
import com.builtbroken.atomic.lib.power.PowerSystem;
import com.builtbroken.atomic.proxy.ModProxy;
import com.builtbroken.atomic.proxy.Mods;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2018.
 */
public class ProxyBuildcraftEnergy extends ModProxy
{
    public static final String BC = "buildcraftenergy";
    public static final ProxyBuildcraftEnergy INSTANCE = new ProxyBuildcraftEnergy();

    public static final ResourceLocation BC_WRAPPER = new ResourceLocation(AtomicScience.DOMAIN, "wrapper.buildcraft.energy");

    public ProxyBuildcraftEnergy()
    {
        super("Buildcraft Energy Proxy", Mods.BUILDCRAFT_ENERGY);
    }

    @Override
    @Optional.Method(modid = "buildcraftenergy")
    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(this);
        PowerSystem.register(new PowerHandlerMJ());
    }

    @SubscribeEvent
    @Optional.Method(modid = "buildcraftenergy")
    public void attachCapabilityItem(AttachCapabilitiesEvent<TileEntity> event)
    {
        if (event.getObject() instanceof TileEntityPowerInvMachine || event.getObject() instanceof TileEntitySteamGenerator) //TODO make an API or other indicator
        {
            event.addCapability(BC_WRAPPER, new MjCapabilityProvider(event.getObject()));
        }
    }
}
