package com.builtbroken.atomic.client.particles;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.CommonProxy;
import com.builtbroken.atomic.content.machines.container.RenderEntityItem2;
import com.builtbroken.atomic.network.packet.client.PacketAcceleratorParticleSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/14/2019.
 */
@Mod.EventBusSubscriber(modid = AtomicScience.DOMAIN, value = Side.CLIENT)
public class ParticleRenderSystem
{
    private static EntityItem entityItem;
    private static RenderEntityItem2 renderEntityItem;

    private static final ItemStack brokenItem = new ItemStack(Blocks.LIT_PUMPKIN);

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event)
    {
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().world != null)
        {
            final World world = Minecraft.getMinecraft().world;

            //Tick
            CommonProxy.PARTICLES_TO_RENDER.values().forEach(p -> p.keepAlive++);

            //Cleanup
            CommonProxy.PARTICLES_TO_RENDER.values().removeIf(p -> p.dim != world.provider.getDimension() || p.keepAlive > 1000);
        }
        else
        {
            //TODO run on world exit
            CommonProxy.PARTICLES_TO_RENDER.clear();
            CommonProxy.NEW_PARTICLE_PACKETS.clear();
        }
    }

    @SubscribeEvent
    public static void render(RenderWorldLastEvent event)
    {
        consumePackets();

        //Setup
        if (entityItem == null)
        {
            entityItem = new EntityItem(null);
            renderEntityItem = new RenderEntityItem2(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem());
        }

        //Get data
        final World world = Minecraft.getMinecraft().world;
        final Entity renderViewEntity = Minecraft.getMinecraft().getRenderViewEntity();
        final float partialTicks = event.getPartialTicks();

        //Get render center
        double xx = renderViewEntity.prevPosX + (renderViewEntity.posX - renderViewEntity.prevPosX) * (double) partialTicks;
        double yy = renderViewEntity.prevPosY + (renderViewEntity.posY - renderViewEntity.prevPosY) * (double) partialTicks;
        double zz = renderViewEntity.prevPosZ + (renderViewEntity.posZ - renderViewEntity.prevPosZ) * (double) partialTicks;

        //Render each particle
        GlStateManager.pushMatrix();
        for (AcceleratorParticleRenderData data : CommonProxy.PARTICLES_TO_RENDER.values())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-xx, -yy, -zz);

            //Set data
            entityItem.setWorld(world);
            entityItem.setPosition(data.cx, data.cy, data.cz);
            entityItem.setItem(data.renderItem == null || data.renderItem.isEmpty() ? brokenItem : data.renderItem);
            entityItem.hoverStart = 0;

            //Render
            renderEntityItem.doRender(entityItem, data.cx, data.cy, data.cz, 0, 0);

            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }

    private static void consumePackets()
    {
        while (CommonProxy.NEW_PARTICLE_PACKETS.peek() != null)
        {
            AcceleratorParticleRenderData data;
            final PacketAcceleratorParticleSync packet = CommonProxy.NEW_PARTICLE_PACKETS.poll();
            if (CommonProxy.PARTICLES_TO_RENDER.containsKey(packet.ID))
            {
                data = CommonProxy.PARTICLES_TO_RENDER.get(packet.ID);
            }
            else
            {
                data = new AcceleratorParticleRenderData();
                CommonProxy.PARTICLES_TO_RENDER.put(packet.ID, data);
            }

            data.ID = packet.ID;
            data.dim = packet.dim;
            data.cx = packet.cx;
            data.cy = packet.cy;
            data.cz = packet.cz;
            data.renderItem = packet.renderItem;
            data.keepAlive = 0;
        }
    }
}
