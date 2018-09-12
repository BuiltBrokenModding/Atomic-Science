package com.builtbroken.atomic.client;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.effects.client.RenderRadOverlay;
import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import com.builtbroken.atomic.content.machines.processing.boiler.render.TESRChemBoiler;
import com.builtbroken.atomic.content.machines.processing.centrifuge.TileEntityChemCentrifuge;
import com.builtbroken.atomic.content.machines.processing.centrifuge.render.TESRChemCentrifuge;
import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import com.builtbroken.atomic.content.machines.processing.extractor.render.TESRChemExtractor;
import com.builtbroken.atomic.content.machines.steam.generator.TESRSteamGenerator;
import com.builtbroken.atomic.content.machines.steam.generator.TileEntitySteamGenerator;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Handles registering renders and models
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
@Mod.EventBusSubscriber(modid = AtomicScience.DOMAIN)
public class ASClientReg
{
    @SubscribeEvent
    public static void registerAllModels(ModelRegistryEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new ASClientReg());
        MinecraftForge.EVENT_BUS.register(RenderRadOverlay.INSTANCE);

        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReactorCell.class, new TESRReactorCell());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamGenerator.class, new TESRSteamGenerator());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemExtractor.class, new TESRChemExtractor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemBoiler.class, new TESRChemBoiler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemCentrifuge.class, new TESRChemCentrifuge());
    }
}
