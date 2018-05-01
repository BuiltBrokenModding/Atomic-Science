package com.builtbroken.atomic.content.effects.client;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.ClientProxy;
import com.builtbroken.atomic.config.ConfigRadiation;
import com.builtbroken.atomic.lib.Render2DHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/22/2018.
 */
public class RenderRadOverlay
{
    public static final Color startColor = new Color(8453920);
    public static final Color endColor = new Color(0xFF390B);
    public static final RenderRadOverlay INSTANCE = new RenderRadOverlay();

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Post event)
    {
        int width = event.resolution.getScaledWidth();
        int height = event.resolution.getScaledHeight();
        Minecraft mc = Minecraft.getMinecraft();

        if (event.type == RenderGameOverlayEvent.ElementType.ALL)
        {
            //Start
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0, 0.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);

            //Position TODO config TODO fire event
            int left = 5;
            int top = 5;

            //Get data
            final float rad_player = interpolate(ClientProxy.PREV_RAD_PLAYER, ClientProxy.RAD_PLAYER, event.partialTicks);
            final float rad_area = interpolate(ClientProxy.PREV_RAD_EXPOSURE, ClientProxy.RAD_EXPOSURE, event.partialTicks) * 20;
            final float rad_dead_min = ClientProxy.RAD_PLAYER / (20 * 60); //Radiation needed to die in 1 min

            //Format
            String remDisplay = formatDisplay("REM:", rad_player);
            String radDisplay = formatDisplay("RAD: ", rad_area);

            //Render
            Render2DHelper.renderTextWithShadow(remDisplay, left, top, interpolate(startColor, endColor, rad_player / ConfigRadiation.RADIATION_DEATH_POINT).getRGB());
            Render2DHelper.renderTextWithShadow(radDisplay, left, top + 10, interpolate(startColor, endColor, rad_area / rad_dead_min).getRGB());

            if (AtomicScience.runningAsDev)
            {
                Render2DHelper.renderTextWithShadow("" + ClientProxy.RAD_REMOVE_TIMER, left + 60, top, endColor.getRGB());
            }

            //Set prev
            ClientProxy.PREV_RAD_PLAYER = rad_player;
            ClientProxy.PREV_RAD_EXPOSURE = rad_area;

            //End
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    protected Color interpolate(Color color, Color color2, float per)
    {
        //Keep in range to prevent crash
        per = Math.max(0, Math.min(1, per));

        //Move numbers
        float r = interpolate(color.getRed() / 255f, color2.getRed() / 255f, per);
        float g = interpolate(color.getGreen() / 255f, color2.getGreen() / 255f, per);
        float b = interpolate(color.getBlue() / 255f, color2.getBlue() / 255f, per);

        //New color
        return new Color(r, g, b);
    }

    private float interpolate(float a, float b, float proportion)
    {
        return (a + ((b - a) * proportion));
    }

    protected String formatDisplay(String prefix, float number)
    {
        return String.format("%s %6s", prefix, String.format("%.2f", number)); //likely a better way but it works
    }

}
