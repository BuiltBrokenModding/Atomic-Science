package com.builtbroken.atomic.content.machines.processing.extractor.render;

import com.builtbroken.atomic.AtomicScience;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/23/2018.
 */
public class RenderItemExtractor implements IItemRenderer
{
    IModelCustom model_base;
    IModelCustom model_drum;

    ResourceLocation texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "machines/chemical_extractor.png");

    public RenderItemExtractor()
    {
        model_base = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "machines/chemical_extractor_base.obj"));
        model_drum = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "machines/chemical_extractor_drum.obj"));
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        GL11.glPushMatrix();
        GL11.glScaled(0.0625f, 0.0625f, 0.0625f);
        if (type.equals(ItemRenderType.INVENTORY))
        {
            GL11.glTranslatef(-0.5f, -0.8f, -0.5f);
            GL11.glRotatef(180f, 0, 1, 0);
        }
        else if (type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON))
        {
            GL11.glTranslatef(0.5f, 0.2f, 0.5f);
        }
        else if (type.equals(ItemRenderType.EQUIPPED))
        {
            GL11.glTranslatef(0.5f, 0.2f, 0.5f);
        }
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
        model_base.renderAll();
        model_drum.renderAll();
        GL11.glPopMatrix();
    }
}
