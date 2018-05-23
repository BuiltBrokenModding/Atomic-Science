package com.builtbroken.atomic.content;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.effects.client.RenderRadOverlay;
import com.builtbroken.atomic.content.items.cell.RendererItemCell;
import com.builtbroken.atomic.content.machines.processing.boiler.TESRChemBoiler;
import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import com.builtbroken.atomic.content.machines.processing.centrifuge.TESRChemCentrifuge;
import com.builtbroken.atomic.content.machines.processing.centrifuge.TileEntityChemCentrifuge;
import com.builtbroken.atomic.content.machines.processing.extractor.TESRChemExtractor;
import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import com.builtbroken.atomic.content.machines.reactor.fission.TESRReactorCell;
import com.builtbroken.atomic.content.machines.reactor.fission.TileEntityReactorCell;
import com.builtbroken.atomic.content.machines.steam.generator.TESRSteamGenerator;
import com.builtbroken.atomic.content.machines.steam.generator.TileEntitySteamGenerator;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * Handles registering renders and models
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/18/2018.
 */
@SideOnly(Side.CLIENT)
public class ASClientReg
{
    public static void register()
    {
        MinecraftForge.EVENT_BUS.register(new ASClientReg());
        MinecraftForge.EVENT_BUS.register(RenderRadOverlay.INSTANCE);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReactorCell.class, new TESRReactorCell());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamGenerator.class, new TESRSteamGenerator());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemExtractor.class, new TESRChemExtractor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemBoiler.class, new TESRChemBoiler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChemCentrifuge.class, new TESRChemCentrifuge());

        MinecraftForgeClient.registerItemRenderer(ASItems.itemFluidCell, new RendererItemCell());
    }

    @SubscribeEvent
    public void textureStitchEventPre(TextureStitchEvent.Pre event)
    {
        if (event.map.getTextureType() == 0)
        {
            for (ASFluids fluid : ASFluids.values())
            {
                if (!fluid.makeBlock && fluid.texture_still != null)
                {
                    event.map.registerIcon(AtomicScience.PREFIX + "fluids/" + fluid.texture_still);
                    event.map.registerIcon(AtomicScience.PREFIX + "fluids/" + fluid.texture_flow);
                }
            }
        }
    }

    @SubscribeEvent
    public void textureStitchEventPost(TextureStitchEvent.Post event)
    {
        if (event.map.getTextureType() == 0)
        {
            for (ASFluids fluid : ASFluids.values())
            {
                if (!fluid.makeBlock && fluid.texture_still != null)
                {
                    IIcon stillIcon = event.map.getTextureExtry(AtomicScience.PREFIX + "fluids/" + fluid.texture_still);
                    IIcon flowingIcon = event.map.getTextureExtry(AtomicScience.PREFIX + "fluids/" + fluid.texture_flow);
                    if (stillIcon != null && flowingIcon != null)
                    {
                        fluid.fluid.setIcons(stillIcon, flowingIcon);
                    }
                    else
                    {
                        AtomicScience.logger.error("Failed to get registered fluid textures for " + fluid.id
                                + " | Icon1: " + stillIcon + "  Icon2: " + flowingIcon
                                + " | Key1: " + fluid.texture_still + " Key2: " + fluid.texture_flow);
                    }
                }
            }
        }
    }
}
