package com.builtbroken.atomic.content.items.cell;

import com.builtbroken.atomic.content.ASItems;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

/**
 * Handles rendering fluid in the cell
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/21/2018.
 * Credit to CoFH and Stackoverflow
 */
public class RendererItemCell implements IItemRenderer
{
    @Override
    public boolean handleRenderType(ItemStack cell, ItemRenderType type)
    {
        if (cell.getItem() instanceof ItemFluidCell && cell.stackTagCompound != null)
        {
            //Do not render for fluids that have provided textures
            final FluidStack fluid = ((ItemFluidCell) cell.getItem()).getFluid(cell);
            if (fluid == null || ASItems.itemFluidCell.supportedFluidToTexturePath.containsKey(fluid.getFluid()))
            {
                return false;
            }
            return true;
        }
        return false; //Let default rendering take over for empty cells
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return type.equals(ItemRenderType.ENTITY);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        GL11.glPushMatrix();
        preRender();
        applyRotation(type);
        render(type, item);
        postRender();
        GL11.glPopMatrix();
    }

    protected void render(ItemRenderType type, ItemStack item)
    {
        renderFluid(type, item);
        renderCell(type, item);
        //TODO render blinking lights and maybe a little noise
    }

    protected void renderFluid(ItemRenderType type, ItemStack item)
    {
        final FluidStack fluidStack = ((ItemFluidCell) item.getItem()).getFluid(item);
        if (fluidStack != null)
        {
            final Fluid fluid = fluidStack.getFluid();

            if (fluid != null)
            {
                final IIcon maskIcon = ASItems.itemFluidCell.getIconFromDamage(-1);
                final IIcon subIcon = fluid.getFlowingIcon() != null ? fluid.getFlowingIcon() : fluid.getIcon();

                if (maskIcon != null && subIcon != null)
                {

                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glDisable(GL11.GL_CULL_FACE);
                    Tessellator tessellator = Tessellator.instance;

                    //Render mask TODO change mask verts to show fill %
                    bindItemTexture(item.getItem());
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0, 0, 1);
                    generatedGeometry(maskIcon, type, 10, 0.001);
                    tessellator.draw();

                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0, 0, -1); //TODO don't think is needed for inventory
                    generatedGeometry(maskIcon, type, -0.0635, -0.0635);
                    tessellator.draw();

                    //Render fluid
                    bindFluidTexture(fluid);

                    GL11.glDepthFunc(GL11.GL_EQUAL);
                    GL11.glDepthMask(false);

                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0, 0, 1);
                    generatedGeometry(subIcon, type, 10, 0.001);
                    tessellator.draw();

                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0, 0, -1); //TODO don't think is needed for inventory
                    generatedGeometry(subIcon, type, -0.0635, -0.0635);
                    tessellator.draw();

                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glDepthMask(true);
                    GL11.glDepthFunc(GL11.GL_LEQUAL);
                    GL11.glEnable(GL11.GL_CULL_FACE);
                    GL11.glColor4f(1, 1, 1, 1);
                }
            }
        }
    }

    protected void generatedGeometry(IIcon icon, ItemRenderType type, double z_inv, double z_world)
    {
        if (type.equals(ItemRenderType.INVENTORY))
        {
            Tessellator.instance.addVertexWithUV(0, 16, z_inv, icon.getMinU(), icon.getMaxV());
            Tessellator.instance.addVertexWithUV(16, 16, z_inv, icon.getMaxU(), icon.getMaxV());
            Tessellator.instance.addVertexWithUV(16, 0, z_inv, icon.getMaxU(), icon.getMinV());
            Tessellator.instance.addVertexWithUV(0, 0, z_inv, icon.getMinU(), icon.getMinV());
        }
        else
        {
            Tessellator.instance.addVertexWithUV(0, 1, z_world, icon.getMinU(), icon.getMaxV());
            Tessellator.instance.addVertexWithUV(1, 1, z_world, icon.getMaxU(), icon.getMaxV());
            Tessellator.instance.addVertexWithUV(1, 0, z_world, icon.getMaxU(), icon.getMinV());
            Tessellator.instance.addVertexWithUV(0, 0, z_world, icon.getMinU(), icon.getMinV());
        }
    }

    protected void renderCell(ItemRenderType type, ItemStack item)
    {
        bindItemTexture(item.getItem());

        IIcon icon = ASItems.itemFluidCell.getIconFromDamage(0);
        if (!type.equals(ItemRenderType.INVENTORY))
        {
            ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMinU(), icon.getMaxV(), icon.getMaxU(), icon.getMinV(), icon.getIconWidth(),
                    icon.getIconHeight(), 0.0625F);
        }
        else
        {
            Tessellator.instance.startDrawingQuads();
            generatedGeometry(icon, ItemRenderType.INVENTORY, 4, 0);
            Tessellator.instance.draw();
        }
    }

    protected void bindFluidTexture(Fluid fluid)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(FMLClientHandler.instance().getClient().renderEngine.getResourceLocation(fluid.getSpriteNumber()));
    }

    protected void bindItemTexture(Item item)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(FMLClientHandler.instance().getClient().renderEngine.getResourceLocation(item.getSpriteNumber()));
    }

    protected void applyRotation(ItemRenderType type)
    {
        if (type.equals(ItemRenderType.ENTITY))
        {
            GL11.glRotated(180, 0, 0, 1);
            GL11.glRotated(90, 0, 1, 0);
            GL11.glScaled(0.75, 0.75, 0.75);
            GL11.glTranslated(-0.5, -0.6, 0);
        }
        else if (type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON))
        {
            GL11.glTranslated(1, 1, 0);
            GL11.glRotated(180, 0, 0, 1);
        }
        else if (type.equals(ItemRenderType.EQUIPPED))
        {
            GL11.glRotated(180, 0, 0, 1);
            GL11.glTranslated(-1, -1, 0);
        }
    }

    protected void preRender()
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
    }

    protected void postRender()
    {
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
    }
}
