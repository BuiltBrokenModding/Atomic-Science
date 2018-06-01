package com.builtbroken.atomic.content.machines.processing.extractor.render;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public class TESRChemExtractor extends TileEntitySpecialRenderer
{
    IModelCustom model;

    ResourceLocation texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "machines/chem.extractor.png");

    final String[] movingParts = new String[]{"MAGNET1", "MAGNET2", "MAIN_CHAMBER"};

    public TESRChemExtractor()
    {
        model = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "machines/chem.extractor.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float deltaFrame)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.036, z + 0.5);

        if (tile instanceof TileEntityChemExtractor)
        {
            TileEntityChemExtractor entityChemExtractor = (TileEntityChemExtractor) tile;
            ForgeDirection facing = entityChemExtractor.getFacingDirection();

            switch (facing)
            {
                case NORTH:
                    GL11.glRotatef(-90, 0, 1, 0);
                    break;
                case SOUTH:
                    GL11.glRotatef(90, 0, 1, 0);
                    break;
                case EAST:
                    GL11.glRotatef(180, 0, 1, 0);
                    break;
                case WEST:
                    //Good by default
                    break;
            }

            bindTexture(texture);

            //Render main body
            model.renderAllExcept(movingParts);

            //Render rotating parts
            GL11.glTranslated(-0.1875,0.4,  0);
            GL11.glRotatef(((TileEntityChemExtractor) tile).rotate(deltaFrame), 0, 0, 1);
            GL11.glTranslated(0.1875,-0.4,  0);
            model.renderOnly(movingParts);
        }

        GL11.glPopMatrix();
    }
}
