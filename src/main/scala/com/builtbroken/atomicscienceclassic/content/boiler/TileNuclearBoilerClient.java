package com.builtbroken.atomicscienceclassic.content.boiler;

import com.builtbroken.atomicscienceclassic.Atomic;
import com.builtbroken.mc.api.items.ISimpleItemRenderer;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.transform.vector.Pos;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/13/2017.
 */
public class TileNuclearBoilerClient extends TileNuclearBoiler implements ISimpleItemRenderer
{
    public static final IModelCustom MODEL = AdvancedModelLoader.loadModel(new ResourceLocation(Atomic.DOMAIN, References.MODEL_DIRECTORY + "nuclearBoiler.tcn"));
    public static final ResourceLocation TEXTURE = new ResourceLocation(Atomic.DOMAIN, References.MODEL_PATH + "nuclearBoiler.png");

    @SideOnly(Side.CLIENT)
    public void renderDynamic(Pos pos, float frame, int pass)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(pos.x(), pos.y(), pos.z());
        GL11.glRotatef(90, 0, 1, 0);

        //TODO add rotation from machine placement

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TEXTURE);

        //MODEL.renderOnlyAroundPivot(Math.toDegrees(tileEntity.rotation), 0, 1, 0, "FUEL BAR SUPPORT 1 ROTATES", "FUEL BAR 1 ROTATES");
        //MODEL.renderOnlyAroundPivot(-Math.toDegrees(tileEntity.rotation), 0, 1, 0, "FUEL BAR SUPPORT 2 ROTATES", "FUEL BAR 2 ROTATES");
        //MODEL.renderAllExcept("FUEL BAR SUPPORT 1 ROTATES", "FUEL BAR SUPPORT 2 ROTATES", "FUEL BAR 1 ROTATES", "FUEL BAR 2 ROTATES");
        MODEL.renderAll();
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
        return new GuiNuclearBoiler(player, this);
    }
}
