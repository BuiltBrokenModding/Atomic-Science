package resonantinduction.atomic.process.fission;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;

import org.lwjgl.opengl.GL11;

import resonant.lib.render.RenderUtility;
import resonant.lib.render.model.TechneAdvancedModel;
import resonantinduction.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNuclearBoiler extends TileEntitySpecialRenderer
{
    public static final TechneAdvancedModel MODEL = (TechneAdvancedModel) AdvancedModelLoader.loadModel(Reference.MODEL_DIRECTORY + "nuclearBoiler.tcn");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.DOMAIN, Reference.MODEL_PATH + "nuclearBoiler.png");

    public void renderAModelAt(TileNuclearBoiler tileEntity, double x, double y, double z, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glRotatef(90, 0, 1, 0);

        if (tileEntity.worldObj != null)
        {
            RenderUtility.rotateBlockBasedOnDirection(tileEntity.getDirection());
        }

        bindTexture(TEXTURE);

        MODEL.renderOnlyAroundPivot(Math.toDegrees(tileEntity.rotation), 0, 1, 0, "FUEL BAR SUPPORT 1 ROTATES", "FUEL BAR 1 ROTATES");
        MODEL.renderOnlyAroundPivot(-Math.toDegrees(tileEntity.rotation), 0, 1, 0, "FUEL BAR SUPPORT 2 ROTATES", "FUEL BAR 2 ROTATES");
        MODEL.renderAllExcept("FUEL BAR SUPPORT 1 ROTATES", "FUEL BAR SUPPORT 2 ROTATES", "FUEL BAR 1 ROTATES", "FUEL BAR 2 ROTATES");
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
        this.renderAModelAt((TileNuclearBoiler) tileEntity, var2, var4, var6, var8);
    }
}