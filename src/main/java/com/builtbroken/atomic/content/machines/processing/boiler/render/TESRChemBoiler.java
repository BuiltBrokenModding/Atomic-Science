package com.builtbroken.atomic.content.machines.processing.boiler.render;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/22/2018.
 */
public class TESRChemBoiler extends TileEntitySpecialRenderer
{
    IModelCustom model;

    ResourceLocation texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "machines/chem.boiler.png");

    final String[] movingParts = new String[]{"rrot", "srot"};

    public TESRChemBoiler()
    {
        model = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "machines/chem.boiler.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float deltaFrame)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.036, z + 0.5);

        if (tile instanceof TileEntityChemBoiler)
        {
            TileEntityChemBoiler entityChemExtractor = (TileEntityChemBoiler) tile;
            ForgeDirection facing = entityChemExtractor.getFacingDirection();

            switch (facing)
            {
                case NORTH:
                    //Good by default
                    break;
                case SOUTH:
                    GL11.glRotatef(180, 0, 1, 0);
                    break;
                case EAST:
                    GL11.glRotatef(-90, 0, 1, 0);
                    break;
                case WEST:
                    GL11.glRotatef(90, 0, 1, 0);
                    break;
            }

            bindTexture(texture);

            //Render main body
            model.renderAllExcept(movingParts);

            //Render rotating parts
            GL11.glRotatef(((TileEntityChemBoiler) tile).rotate(deltaFrame), 0, 1, 0);
            model.renderOnly(movingParts);
        }

        GL11.glPopMatrix();
    }
}
