package com.builtbroken.atomic.content.machines.processing.centrifuge.render;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.processing.centrifuge.TileEntityChemCentrifuge;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/23/2018.
 */
public class TESRChemCentrifuge extends TileEntitySpecialRenderer<TileEntityChemCentrifuge>
{
    //IModelCustom model_body;
    //IModelCustom model_core;

    ResourceLocation texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "machines/centrifuge.png");

    public TESRChemCentrifuge()
    {
        //model_body = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "machines/centrifuge_base.obj"));
        //model_core = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "machines/centrifuge_core.obj"));
    }

    //@Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float deltaFrame)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glScaled(0.0625f, 0.0625f, 0.0625f);

        if (tile instanceof TileEntityChemCentrifuge)
        {
            TileEntityChemCentrifuge entityChemExtractor = (TileEntityChemCentrifuge) tile;
            EnumFacing facing = entityChemExtractor.getFacingDirection();

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
                    //GL11.glRotatef(-90, 0, 1, 0);
                    break;
            }

            bindTexture(texture);

            //Render main body
            //model_body.renderAll();

            //Render rotating parts
            GL11.glRotatef(((TileEntityChemCentrifuge) tile).rotate(deltaFrame), 0, 1, 0);
            //model_core.renderAll();
        }

        GL11.glPopMatrix();
    }
}
