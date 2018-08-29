package com.builtbroken.atomic.content.machines.reactor.fission.core;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/7/2018.
 */
public class TESRReactorCell extends TileEntitySpecialRenderer
{
    IModelCustom normal_model;
    IModelCustom top_model;
    IModelCustom middle_model;
    IModelCustom bottom_model;

    ResourceLocation normal_texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "reactor/reactor_cell.png");
    ResourceLocation top_texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "reactor/reactor_cell_top.png");
    ResourceLocation middle_texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "reactor/reactor_cell_center.png");
    ResourceLocation bottom_texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "reactor/reactor_cell_bottom.png");

    public TESRReactorCell()
    {
        normal_model = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "reactor/reactor_cell.obj"));
        top_model = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "reactor/reactor_cell_top.obj"));
        middle_model = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "reactor/reactor_cell_center.obj"));
        bottom_model = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "reactor/reactor_cell_bottom.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float deltaFrame)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glScaled(0.0625f, 0.0625f, 0.0625f);

        if (tile instanceof TileEntityReactorCell)
        {
            TileEntityReactorCell reactorCell = (TileEntityReactorCell) tile;
            if (reactorCell.isBottom())
            {
                bindTexture(bottom_texture);
                bottom_model.renderAll();
            }
            else if (reactorCell.isTop())
            {
                bindTexture(top_texture);
                top_model.renderAll();
            }
            else if (reactorCell.isMiddle())
            {
                bindTexture(middle_texture);
                middle_model.renderAll();
            }
            else
            {
                bindTexture(normal_texture);
                normal_model.renderAll();
            }

            if(reactorCell.shouldRenderFuel())
            {
                //TODO render fuel rods
                //TODO decrease in size as fuel is used
                //TODO decrease in color (green -> grey) as its used
                //TODO glow when running (blue or green? hmm)
                //TODO glow molten like if overheating
            }
        }

        GL11.glPopMatrix();
    }
}
