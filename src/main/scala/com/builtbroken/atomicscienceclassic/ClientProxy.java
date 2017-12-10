package com.builtbroken.atomicscienceclassic;

import net.minecraftforge.common.MinecraftForge;
import resonant.lib.render.block.BlockRenderingHandler;
import com.builtbroken.atomicscienceclassic.content.reactor.fission.RenderReactorCell;
import com.builtbroken.atomicscienceclassic.content.reactor.fission.TileReactorCell;
import com.builtbroken.atomicscienceclassic.content.reactor.fusion.RenderPlasmaHeater;
import com.builtbroken.atomicscienceclassic.content.reactor.fusion.TilePlasmaHeater;
import com.builtbroken.atomicscienceclassic.content.accelerator.EntityParticle;
import com.builtbroken.atomicscienceclassic.content.accelerator.RenderParticle;
import com.builtbroken.atomicscienceclassic.content.extractor.RenderChemicalExtractor;
import com.builtbroken.atomicscienceclassic.content.extractor.TileChemicalExtractor;
import com.builtbroken.atomicscienceclassic.content.centrifuge.RenderCentrifuge;
import com.builtbroken.atomicscienceclassic.content.boiler.RenderNuclearBoiler;
import com.builtbroken.atomicscienceclassic.content.centrifuge.TileCentrifuge;
import com.builtbroken.atomicscienceclassic.content.boiler.TileNuclearBoiler;
import com.builtbroken.atomicscienceclassic.content.sensor.RenderThermometer;
import com.builtbroken.atomicscienceclassic.content.sensor.TileThermometer;
import com.builtbroken.atomicscienceclassic.content.turbine.RenderElectricTurbine;
import com.builtbroken.atomicscienceclassic.content.turbine.TileElectricTurbine;
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
