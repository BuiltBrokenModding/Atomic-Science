package com.builtbroken.atomic.content.machines.reactor.fission.core;

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
public class RenderItemReactor implements IItemRenderer
{
    IModelCustom model;
    ResourceLocation texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "reactor/cell.top.png");

    public RenderItemReactor()
    {
        model = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "reactor/cell.top.obj"));
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
        if (type.equals(ItemRenderType.INVENTORY))
        {
            GL11.glTranslatef(-0.5f, -0.9f, -0.5f);
            GL11.glScalef(1.2f, 1.2f, 1.2f);
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
        model.renderAll();
        GL11.glPopMatrix();
    }
}
