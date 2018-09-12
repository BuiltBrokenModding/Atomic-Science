package com.builtbroken.atomic.content.machines.processing.boiler.render;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2018.
 */
public class TESRChemBoiler extends TileEntitySpecialRenderer<TileEntityChemBoiler>
{
    //IModelCustom model_base;
    //IModelCustom model_core;

    ResourceLocation texture_on = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "machines/boiler_on.png");
    ResourceLocation texture_off = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "machines/boiler_off.png");

    public TESRChemBoiler()
    {
        //model_base = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "machines/boiler_base_max.obj"));
        //model_core = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "machines/boiler_cell.obj"));
    }

    //@Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float deltaFrame)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glScaled(0.0625f, 0.0625f, 0.0625f);

        if (tile instanceof TileEntityChemBoiler)
        {
            TileEntityChemBoiler entityChemExtractor = (TileEntityChemBoiler) tile;
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
                    //Good by default
                    break;
            }

            bindTexture(((TileEntityChemBoiler) tile).processTimer > 0 ? texture_on : texture_off);

            //Render main body
            //model_base.renderAll();

            //Render rotating parts
            GL11.glRotatef(((TileEntityChemBoiler) tile).rotate(deltaFrame), 0, 1, 0);
            //model_core.renderAll();
        }

        GL11.glPopMatrix();
    }
}
