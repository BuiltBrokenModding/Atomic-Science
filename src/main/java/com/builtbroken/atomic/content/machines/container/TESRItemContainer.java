package com.builtbroken.atomic.content.machines.container;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2019.
 */
public class TESRItemContainer extends TileEntitySpecialRenderer<TileEntityItemContainer>
{
    private EntityItem entityItem;
    private RenderEntityItem2 renderEntityItem;

    @Override
    public void render(TileEntityItemContainer tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        final ItemStack stackToRender = new ItemStack(Items.REDSTONE);//tile.getHeldItem();
        if (stackToRender != null && !stackToRender.isEmpty())
        {
            if(entityItem == null)
            {
                entityItem = new EntityItem(null);
                renderEntityItem = new RenderEntityItem2(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem());
            }

            stackToRender.setCount(1);

            //Set data
            entityItem.setWorld(tile.world());
            entityItem.setPosition(x, y, z);
            entityItem.setItem(stackToRender);
            entityItem.hoverStart = 0;

            tile.rotation += partialTicks * 2;

            if(tile.rotation > 360)
            {
                tile.rotation -= 360;
            }

            //render
            GlStateManager.pushMatrix();
            entityItem.rotationYaw = tile.rotation;
            float bob = (float)(Math.sin((tile.getTicks() + partialTicks) / 10f) * 0.025 - 0.0125);
            if(stackToRender.getItem() instanceof ItemBlock)
            {
                renderEntityItem.doRender(entityItem, x + 0.5, y + 0.3 + bob, z + 0.5, 0, 0);
            }
            else
            {
                renderEntityItem.doRender(entityItem, x + 0.5, y + 0.4 + bob, z + 0.5, 0, 0);
            }
            GlStateManager.popMatrix();
        }
    }
}
