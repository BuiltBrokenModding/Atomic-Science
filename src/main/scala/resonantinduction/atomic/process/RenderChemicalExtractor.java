package resonantinduction.atomic.process;

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
public class RenderChemicalExtractor extends TileEntitySpecialRenderer
{
    public static final TechneAdvancedModel MODEL = (TechneAdvancedModel) AdvancedModelLoader.loadModel(Reference.MODEL_DIRECTORY + "chemicalExtractor.tcn");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.DOMAIN, Reference.MODEL_PATH + "chemicalExtractor.png");

    public void render(TileChemicalExtractor tileEntity, double x, double y, double z, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

        if (tileEntity.worldObj != null)
        {
            RenderUtility.rotateBlockBasedOnDirection(tileEntity.getDirection());
        }

        bindTexture(TEXTURE);

        GL11.glPushMatrix();
        MODEL.renderOnlyAroundPivot(Math.toDegrees(tileEntity.rotation), 0, 0, 1, "MAIN CHAMBER-ROTATES", "MAGNET 1-ROTATES", "MAGNET 2-ROTATES");
        GL11.glPopMatrix();

        MODEL.renderAllExcept("MAIN CHAMBER-ROTATES", "MAGNET 1-ROTATES", "MAGNET 2-ROTATES");
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
        this.render((TileChemicalExtractor) tileEntity, var2, var4, var6, var8);
    }
}