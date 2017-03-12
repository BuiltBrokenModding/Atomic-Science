package resonantinduction.atomic.process.turbine;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import resonant.lib.prefab.turbine.TileTurbine;
import resonantinduction.atomic.Atomic;
import resonantinduction.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderElectricTurbine extends TileEntitySpecialRenderer
{
    public static final IModelCustom MODEL_SMALL = AdvancedModelLoader.loadModel(Reference.MODEL_DIRECTORY + "turbineSmall.tcn");
    public static final IModelCustom MODEL_LARGE = AdvancedModelLoader.loadModel(Reference.MODEL_DIRECTORY + "turbineLarge.tcn");
    public static final ResourceLocation SMALL_TEXTURE = new ResourceLocation(Reference.DOMAIN, Reference.MODEL_PATH + "turbineSmall.png");
    public static final ResourceLocation LARGE_TEXTURE = new ResourceLocation(Reference.DOMAIN, Reference.MODEL_PATH + "turbineLarge.png");

    @Override
    public void renderTileEntityAt(TileEntity t, double x, double y, double z, float f)
    {
        TileTurbine tile = (TileTurbine) t;

        if (tile.getMultiBlock().isPrimary())
        {
            // Texture file
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

            if (tile.getMultiBlock().isConstructed())
            {
                bindTexture(LARGE_TEXTURE);

                final String[] blades = new String[]
                { "Blade1", "Blade2", "Blade3", "Blade4", "Blade5", "Blade6" };
                final String[] mediumBlades = new String[]
                { "MediumBlade1", "MediumBlade2", "MediumBlade3", "MediumBlade4", "MediumBlade5", "MediumBlade6" };
                final String[] largeBlades = new String[]
                { "LargeBlade1", "LargeBlade2", "LargeBlade3", "LargeBlade4", "LargeBlade5", "LargeBlade6" };

                GL11.glPushMatrix();
                GL11.glRotated(Math.toDegrees(tile.rotation), 0, 1, 0);
                MODEL_LARGE.renderOnly(blades);
                MODEL_LARGE.renderOnly(largeBlades);
                GL11.glPopMatrix();

                GL11.glPushMatrix();
                GL11.glRotated(-Math.toDegrees(tile.rotation), 0, 1, 0);
                MODEL_LARGE.renderOnly(mediumBlades);
                GL11.glPopMatrix();

                MODEL_LARGE.renderAllExcept(ArrayUtils.addAll(ArrayUtils.addAll(blades, mediumBlades), largeBlades));
            }
            else
            {
                GL11.glScalef(1f, 1.1f, 1f);
                bindTexture(SMALL_TEXTURE);

                final String[] bladesA = new String[3];
                for (int i = 0; i < bladesA.length; i++)
                {
                    bladesA[i] = "BLADE A" + (i + 1) + " SPINS";
                }

                final String[] sheildsA = new String[6];
                for (int i = 0; i < sheildsA.length; i++)
                {
                    sheildsA[i] = "SHIELD A" + (i + 1) + " SPINS";
                }

                final String[] bladesB = new String[3];
                for (int i = 0; i < bladesB.length; i++)
                {
                    bladesB[i] = "BLADE B" + (i + 1) + " SPINS";
                }

                final String[] sheildsB = new String[6];
                for (int i = 0; i < sheildsB.length; i++)
                {
                    sheildsB[i] = "SHIELD B" + (i + 1) + " SPINS";
                }

                final String[] renderA = ArrayUtils.addAll(bladesA, sheildsA);
                final String[] renderB = ArrayUtils.addAll(bladesB, sheildsB);

                GL11.glPushMatrix();
                GL11.glRotated(Math.toDegrees(tile.rotation), 0, 1, 0);
                MODEL_SMALL.renderOnly(renderA);
                GL11.glPopMatrix();

                GL11.glPushMatrix();
                GL11.glRotated(-Math.toDegrees(tile.rotation), 0, 1, 0);
                MODEL_SMALL.renderOnly(renderB);
                GL11.glPopMatrix();

                MODEL_SMALL.renderAllExcept(ArrayUtils.addAll(renderA, renderB));
            }

            GL11.glPopMatrix();
        }
    }
}