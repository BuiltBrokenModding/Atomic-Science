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
    IModelCustom model_base;
    IModelCustom model_drum;

    ResourceLocation texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "machines/chemical_extractor.png");

    public TESRChemExtractor()
    {
        model_base = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "machines/chemical_extractor_base.obj"));
        model_drum = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "machines/chemical_extractor_drum.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float deltaFrame)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glScaled(0.0625f, 0.0625f, 0.0625f);

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
            model_base.renderAll();

            //Render rotating parts
            GL11.glTranslated(-2.5, 1.5, 0);
            float rotation = ((TileEntityChemExtractor) tile).rotate(deltaFrame);
            GL11.glRotatef(rotation, 0, 0, 1);
            model_drum.renderAll();
        }

        GL11.glPopMatrix();
    }
}
