package com.builtbroken.atomic;

import net.minecraftforge.common.MinecraftForge;
import resonant.lib.render.block.BlockRenderingHandler;
import com.builtbroken.atomic.content.reactor.fission.RenderReactorCell;
import com.builtbroken.atomic.content.reactor.fission.TileReactorCell;
import com.builtbroken.atomic.content.reactor.fusion.RenderPlasmaHeater;
import com.builtbroken.atomic.content.reactor.fusion.TilePlasmaHeater;
import com.builtbroken.atomic.content.accelerator.EntityParticle;
import com.builtbroken.atomic.content.accelerator.RenderParticle;
import com.builtbroken.atomic.content.extractor.RenderChemicalExtractor;
import com.builtbroken.atomic.content.extractor.TileChemicalExtractor;
import com.builtbroken.atomic.content.centrifuge.RenderCentrifuge;
import com.builtbroken.atomic.content.boiler.RenderNuclearBoiler;
import com.builtbroken.atomic.content.centrifuge.TileCentrifuge;
import com.builtbroken.atomic.content.boiler.TileNuclearBoiler;
import com.builtbroken.atomic.content.sensor.RenderThermometer;
import com.builtbroken.atomic.content.sensor.TileThermometer;
import com.builtbroken.atomic.content.turbine.RenderElectricTurbine;
import com.builtbroken.atomic.content.turbine.TileElectricTurbine;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(SoundHandler.INSTANCE);
        RenderingRegistry.registerBlockHandler(new BlockRenderingHandler());
    }

    @Override
    public int getArmorIndex(String armor)
    {
        return RenderingRegistry.addNewArmourRendererPrefix(armor);
    }

    @Override
    public void init()
    {
        super.init();
        ClientRegistry.bindTileEntitySpecialRenderer(TileCentrifuge.class, new RenderCentrifuge());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePlasmaHeater.class, new RenderPlasmaHeater());
        ClientRegistry.bindTileEntitySpecialRenderer(TileNuclearBoiler.class, new RenderNuclearBoiler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileElectricTurbine.class, new RenderElectricTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileThermometer.class, new RenderThermometer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileChemicalExtractor.class, new RenderChemicalExtractor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileReactorCell.class, new RenderReactorCell());

        RenderingRegistry.registerEntityRenderingHandler(EntityParticle.class, new RenderParticle());
    }
}
