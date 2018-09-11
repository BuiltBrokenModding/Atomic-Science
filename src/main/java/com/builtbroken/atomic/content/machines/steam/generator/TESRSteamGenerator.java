package com.builtbroken.atomic.content.machines.steam.generator;

import com.builtbroken.atomic.AtomicScience;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2018.
 */
public class TESRSteamGenerator extends TileEntitySpecialRenderer<TileEntitySteamGenerator>
{
    //IModelCustom small_model;
    ResourceLocation small_texture = new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_TEXTURE_DIRECTORY + "turbine/small.png");

    //Parts for large turbine
    final String[] blades = new String[]
            {"Blade1", "Blade2", "Blade3", "Blade4", "Blade5", "Blade6"};
    final String[] mediumBlades = new String[]
            {"MediumBlade1", "MediumBlade2", "MediumBlade3", "MediumBlade4", "MediumBlade5", "MediumBlade6"};
    final String[] largeBlades = new String[]
            {"LargeBlade1", "LargeBlade2", "LargeBlade3", "LargeBlade4", "LargeBlade5", "LargeBlade6"};

    //Parts for small turbine
    final String[] smallTurbineRenderA;
    final String[] smallTurbineRenderB;
    final String[] smallTurbineRenderAuB;

    public TESRSteamGenerator()
    {
        //small_model = AdvancedModelLoader.loadModel(new ResourceLocation(AtomicScience.DOMAIN, AtomicScience.MODEL_DIRECTORY + "turbine/small.obj"));

        final String[] bladesA = new String[3];
        for (int i = 0; i < bladesA.length; i++)
        {
            bladesA[i] = "BLADE_A" + (i + 1) + "_SPINS";
        }

        final String[] sheildsA = new String[6];
        for (int i = 0; i < sheildsA.length; i++)
        {
            sheildsA[i] = "SHIELD_A" + (i + 1) + "_SPINS";
        }

        final String[] bladesB = new String[3];
        for (int i = 0; i < bladesB.length; i++)
        {
            bladesB[i] = "BLADE_B" + (i + 1) + "_SPINS";
        }

        final String[] sheildsB = new String[6];
        for (int i = 0; i < sheildsB.length; i++)
        {
            sheildsB[i] = "SHIELD_B" + (i + 1) + "_SPINS";
        }

        smallTurbineRenderA = ArrayUtils.addAll(bladesA, sheildsA);
        smallTurbineRenderB = ArrayUtils.addAll(bladesB, sheildsB);
        smallTurbineRenderAuB = ArrayUtils.addAll(smallTurbineRenderA, smallTurbineRenderB); //TODO convert to renderOnly
    }

    //@Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float deltaTick)
    {
        if (tile instanceof TileEntitySteamGenerator)
        {
            TileEntitySteamGenerator generator = ((TileEntitySteamGenerator) tile);
            float rotation = generator.rotate(deltaTick);

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 0.1, z + 0.5);

            renderSmallTurbine(rotation);

            GL11.glPopMatrix();
        }
    }

    protected void renderSmallTurbine(float rotation)
    {
        bindTexture(small_texture);

        GL11.glPushMatrix();
        GL11.glRotated(rotation, 0, 1, 0);
        //small_model.renderOnly(smallTurbineRenderA);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glRotated(-rotation, 0, 1, 0);
        //small_model.renderOnly(smallTurbineRenderB);
        GL11.glPopMatrix();

        //small_model.renderAllExcept(smallTurbineRenderAuB); //TODO convert to renderOnly
    }
}
