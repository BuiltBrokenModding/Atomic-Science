package com.builtbroken.atomic.content.machines.processing.centrifuge.render;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.machines.processing.centrifuge.TileEntityChemCentrifuge;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/23/2018.
 */
public class TESRChemCentrifuge extends TileEntitySpecialRenderer
{
    IModelCustom model;

    ResourceLocation texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "machines/chem.centrifuge.png");

    final String[] movingParts = new String[]{"JROT", "KROT", "LROT", "MROT"}; //'C' is the shaft, maybe rotate? Also no clue why its named as a single letter

    public TESRChemCentrifuge()
    {
        model = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "machines/chem.centrifuge.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float deltaFrame)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y, z + 0.5);

        if (tile instanceof TileEntityChemCentrifuge)
        {
            TileEntityChemCentrifuge entityChemExtractor = (TileEntityChemCentrifuge) tile;
            ForgeDirection facing = entityChemExtractor.getFacingDirection();

            switch (facing)
            {
                case NORTH:
                    GL11.glRotatef(-180, 0, 1, 0);
                    break;
                case SOUTH:
                    //Good by default
                    break;
                case EAST:
                    GL11.glRotatef(90, 0, 1, 0);
                    break;
                case WEST:
                    GL11.glRotatef(-90, 0, 1, 0);
                    break;
            }

            bindTexture(texture);

            //Render main body
            model.renderAllExcept(movingParts);

            //Render rotating parts
            GL11.glRotatef(((TileEntityChemCentrifuge) tile).rotate(deltaFrame), 0, 1, 0);
            model.renderOnly(movingParts);
        }

        GL11.glPopMatrix();
    }
}
