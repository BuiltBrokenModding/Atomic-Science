package com.builtbroken.atomicscienceclassic.content.centrifuge;

import com.builtbroken.atomicscienceclassic.Atomic;
import com.builtbroken.mc.api.items.ISimpleItemRenderer;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.lib.render.model.loader.EngineModelLoader;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/13/2017.
 */
public class TileCentrifugeClient extends TileCentrifuge implements ISimpleItemRenderer
{
    public static final IModelCustom MODEL = EngineModelLoader.loadModel(new ResourceLocation(Atomic.DOMAIN, References.MODEL_DIRECTORY + "centrifuge.tcn"));
    public static final ResourceLocation TEXTURE = new ResourceLocation(Atomic.DOMAIN, References.MODEL_PATH + "centrifuge.png");

    @Override
    public TileCentrifuge newTile()
    {
        return new TileCentrifugeClient();
    }

    @SideOnly(Side.CLIENT)
    public void renderDynamic(Pos pos, float frame, int pass)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(pos.x(), pos.y(), pos.z());

        //TODO add rotation from machine placement
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TEXTURE);

        GL11.glPushMatrix();
        GL11.glRotated(Math.toDegrees(rotation), 0, 1, 0);
        MODEL.renderOnly("C", "JROT", "KROT", "LROT", "MROT");
        GL11.glPopMatrix();

        MODEL.renderAllExcept("C", "JROT", "KROT", "LROT", "MROT");
        GL11.glPopMatrix();
    }

    @Override
    public void renderInventoryItem(IItemRenderer.ItemRenderType type, ItemStack itemStack, Object... data)
    {
        GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
        GL11.glScaled(.8f, .8f, .8f);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TEXTURE);
        MODEL.renderAll();
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player)
    {
        return new GuiCentrifuge(player, this);
    }
}
