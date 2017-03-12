package resonantinduction.atomic.fusion;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import resonant.lib.render.RenderTaggedTile;
import resonantinduction.atomic.Atomic;
import resonantinduction.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPlasmaHeater extends RenderTaggedTile
{
    public static final IModelCustom MODEL = AdvancedModelLoader.loadModel(Reference.MODEL_DIRECTORY + "fusionReactor.tcn");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.DOMAIN, Reference.MODEL_PATH + "fusionReactor.png");

    @Override
    public void renderTileEntityAt(TileEntity t, double x, double y, double z, float f)
    {
        TilePlasmaHeater tileEntity = (TilePlasmaHeater) t;

        if (tileEntity.worldObj != null)
        {
            super.renderTileEntityAt(t, x, y, z, f);
        }

        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

        bindTexture(TEXTURE);

        GL11.glPushMatrix();
        GL11.glRotated(Math.toDegrees(tileEntity.rotation), 0, 1, 0);
        MODEL.renderOnly("rrot", "srot");
        GL11.glPopMatrix();

        MODEL.renderAllExcept("rrot", "srot");
        GL11.glPopMatrix();
    }
}