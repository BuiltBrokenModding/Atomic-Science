package com.builtbroken.atomic.content.machines.container.item;

import com.builtbroken.atomic.client.RenderEntityItem2;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.machines.reactor.fission.core.BlockReactorCell;
import com.builtbroken.atomic.content.machines.reactor.fission.core.ReactorStructureType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.opengl.GL11;

/**
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2019.
 */
public class TESRItemContainer extends TileEntitySpecialRenderer<TileEntityItemContainer>
{
    private EntityItem entityItem;
    private RenderEntityItem2 renderEntityItem;

    private IBlockState glassBlockState = null;

    @Override
    public void render(TileEntityItemContainer tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        renderItem(tile, x, y, z, partialTicks, destroyStage, alpha);
        renderGlass(tile, x, y, z, partialTicks, destroyStage, alpha);
    }

    private void renderGlass(TileEntityItemContainer tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        final World world = tile.getWorld();

        //Setting up states
        if (glassBlockState == null)
        {
            glassBlockState = ASBlocks.blockItemContainer.getDefaultState().withProperty(BlockItemContainer.MODEL_TYPE, BlockItemContainer.ModelType.GLASS);
        }
        final IBlockState blockState = glassBlockState.withProperty(BlockItemContainer.ROTATION_PROP, tile.getDirection());

        //Get model
        final BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        final IBakedModel model = dispatcher.getModelForState(blockState);

        //Setup rendering
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();

        //Alpha
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);


        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }


        // Translate back to local view coordinates so that we can do the acual rendering here
        GlStateManager.translate(x-tile.getPos().getX(), y-tile.getPos().getY(), z-tile.getPos().getZ());

        final Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                world,
                model,
                world.getBlockState(tile.getPos()),
                tile.getPos(),
                Tessellator.getInstance().getBuffer(),
                false);
        tessellator.draw();

        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void renderItem(TileEntityItemContainer tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        final ItemStack stackToRender = tile.getHeldItem();
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
            entityItem.rotationPitch = tile.rotation;
            float bob = (float)(Math.sin((tile.getTicks() + partialTicks) / 10f) * 0.025 - 0.0125);
            if(stackToRender.getItem() instanceof ItemBlock)
            {
                renderEntityItem.doRender(entityItem, x + 0.5, y + 0.5 + bob, z + 0.5, 0, 0);
            }
            else
            {
                renderEntityItem.doRender(entityItem, x + 0.5, y + 0.4 + bob, z + 0.5, 0, 0);
            }
            GlStateManager.popMatrix();
        }
    }
}
