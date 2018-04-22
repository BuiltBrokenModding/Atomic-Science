package com.builtbroken.atomic.content.effects.client;

import com.builtbroken.atomic.ClientProxy;
import com.builtbroken.atomic.lib.Render2DHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/22/2018.
 */
public class RenderRadOverlay
{
    public static final RenderRadOverlay INSTANCE = new RenderRadOverlay();

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Post event)
    {
        int width = event.resolution.getScaledWidth();
        int height = event.resolution.getScaledHeight();
        Minecraft mc = Minecraft.getMinecraft();

        if(event.type == RenderGameOverlayEvent.ElementType.ALL)
        {
            //Start
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0, 0.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);

            int left = 5;
            int top = 5;

            //Render
            Render2DHelper.renderTextWithShadow("REM: " + ClientProxy.RAD_PLAYER, left, top, 8453920);
            Render2DHelper.renderTextWithShadow("RAD: " + ClientProxy.RAD_EXPOSURE, left, top + 10, 8453920);

            //End
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
}
