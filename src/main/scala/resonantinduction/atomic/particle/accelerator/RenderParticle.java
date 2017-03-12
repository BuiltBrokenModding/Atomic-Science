package resonantinduction.atomic.particle.accelerator;

import java.util.Random;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderParticle extends Render
{
    @Override
    public void doRender(Entity entity, double x, double y, double z, float var8, float var9)
    {
        Tessellator tessellator = Tessellator.instance;

        /** Enderdragon Light */
        float par2 = (entity.ticksExisted);

        while (par2 > 200)
        {
            par2 -= 100;
        }

        RenderHelper.disableStandardItemLighting();
        float var41 = (5 + par2) / 200.0F;
        float var51 = 0.0F;

        if (var41 > 0.8F)
        {
            var51 = (var41 - 0.8F) / 0.2F;
        }

        Random rand = new Random(432L);

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glScalef(0.15f, 0.15f, 0.15f);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, -1.0F, -2.0F);

        for (int i1 = 0; i1 < (var41 + var41 * var41) / 2.0F * 60.0F; ++i1)
        {
            GL11.glRotatef(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(rand.nextFloat() * 360.0F + var41 * 90.0F, 0.0F, 0.0F, 1.0F);
            tessellator.startDrawing(6);
            float var81 = rand.nextFloat() * 20.0F + 5.0F + var51 * 10.0F;
            float var91 = rand.nextFloat() * 2.0F + 1.0F + var51 * 2.0F;
            tessellator.setColorRGBA_I(16777215, (int) (255.0F * (1.0F - var51)));
            tessellator.addVertex(0.0D, 0.0D, 0.0D);
            tessellator.setColorRGBA_I(0, 0);
            tessellator.addVertex(-0.866D * var91, var81, -0.5F * var91);
            tessellator.addVertex(0.866D * var91, var81, -0.5F * var91);
            tessellator.addVertex(0.0D, var81, 1.0F * var91);
            tessellator.addVertex(-0.866D * var91, var81, -0.5F * var91);
            tessellator.draw();
        }

        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return null;
    }

}
