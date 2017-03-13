package com.builtbroken.atomic.content.extractor;

import com.builtbroken.atomic.Atomic;
import com.builtbroken.mc.api.items.ISimpleItemRenderer;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.render.model.loader.EngineModelLoader;
import com.builtbroken.mc.lib.transform.vector.Pos;
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
public class TileChemicalExtractorClient extends TileChemicalExtractor implements ISimpleItemRenderer
{
    public static final IModelCustom MODEL = EngineModelLoader.loadModel(new ResourceLocation(Atomic.DOMAIN, References.MODEL_DIRECTORY + "chemicalExtractor.tcn"));
    public static final ResourceLocation TEXTURE = new ResourceLocation(Atomic.DOMAIN, References.MODEL_PATH + "chemicalExtractor.png");


    public float rotation = 0;

    @Override
    public TileChemicalExtractor newTile()
    {
        return new TileChemicalExtractorClient();
    }

    @Override
    public void update()
    {
        super.update();

        if (time > 0)
        {
            rotation += 0.2f;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player)
    {
        return new GuiChemicalExtractor(player, this);
    }

    @SideOnly(Side.CLIENT)
    public void renderDynamic(Pos pos, float frame, int pass)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(pos.x(), pos.y(), pos.z());

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TEXTURE);

        //GL11.glPushMatrix();
        //MODEL.renderOnlyAroundPivot(Math.toDegrees(tileEntity.rotation), 0, 0, 1, "MAIN CHAMBER-ROTATES", "MAGNET 1-ROTATES", "MAGNET 2-ROTATES");
        //GL11.glPopMatrix();

        //MODEL.renderAllExcept("MAIN CHAMBER-ROTATES", "MAGNET 1-ROTATES", "MAGNET 2-ROTATES");
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
}
