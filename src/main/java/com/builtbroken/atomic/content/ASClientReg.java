package com.builtbroken.atomic.content;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.effects.client.RenderRadOverlay;
import com.builtbroken.atomic.content.machines.reactor.fission.TESRReactorCell;
import com.builtbroken.atomic.content.machines.reactor.fission.TileEntityReactorCell;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
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
    }

    @SubscribeEvent
    public void textureStitchEventPre(TextureStitchEvent.Pre event)
    {
        if (event.map.getTextureType() == 0)
        {
            for (ASFluids fluid : ASFluids.values())
            {
                if (!fluid.makeBlock && fluid.texture != null)
                {
                    event.map.registerIcon(AtomicScience.PREFIX + "fluids/" + fluid.texture); //TODO add flowing
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
                if (!fluid.makeBlock && fluid.texture != null)
                {
                    IIcon icon1 = event.map.getTextureExtry(AtomicScience.PREFIX + "fluids/" + fluid.texture); //TODO add flowing
                    if (icon1 != null)
                    {
                        fluid.fluid.setIcons(icon1, icon1);
                    }
                }
            }
        }
    }
}
