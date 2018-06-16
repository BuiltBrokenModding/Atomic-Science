package com.builtbroken.atomic.client;

import com.builtbroken.atomic.CommonProxy;
import com.builtbroken.atomic.client.fx.FxSmoke;
import com.builtbroken.atomic.config.ConfigClient;
import com.builtbroken.atomic.content.ASClientReg;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.machines.steam.funnel.ISBRSteamFunnel;
import com.builtbroken.atomic.network.netty.PacketSystem;
import com.builtbroken.atomic.network.packet.trigger.PacketMouse;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ClientProxy extends CommonProxy
{
    /** Client Cache: Amount of rads in the environment were the player is standing */
    public static float RAD_EXPOSURE = 0;
    /** Client Cache: Amount of rads the player has taken */
    public static float RAD_PLAYER = 0;
    /** Client Cache: Time until radiation is removed from the player again */
    public static int RAD_REMOVE_TIMER = 0;

    /** Client Cache: Amount of rads in the environment were the player is standing */
    public static float PREV_RAD_EXPOSURE = 0;
    /** Client Cache: Amount of rads the player has taken */
    public static float PREV_RAD_PLAYER = 0;

    public ClientProxy()
    {
        super("ClientProxy");
    }

    @Override
    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(this);
        RenderingRegistry.registerBlockHandler(new ISBRSteamFunnel());
        ASClientReg.register();
    }

    @Override
    public void init()
    {
        super.init();
    }

    @SubscribeEvent
    public void mouseEvent(MouseEvent e)
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack stack = player.getCurrentEquippedItem();
        if (stack != null && stack.getItem() == ASItems.itemWrench)  //TODO add interface when more than wrench use
        {
            if (player.isSneaking() && e.dwheel != 0)
            {
                boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
                PacketSystem.INSTANCE.sendToServer(new PacketMouse(player.inventory.currentItem, ctrl, e.dwheel > 0));
                e.setCanceled(true);
            }
        }
    }

    @Override
    public void spawnParticle(String particle, double x, double y, double z, double vx, double vy, double vz)
    {
        //TODO build an effect system to register effects
        if (Minecraft.getMinecraft().theWorld != null)
        {
            if (particle.startsWith(EffectRefs.STEAM))
            {
                //TODO implement
            }
            else if (particle.startsWith(EffectRefs.REACTOR_RUNNING))
            {
                reactorRunning(x, y, z);
            }
            else if (particle.startsWith(EffectRefs.BOILING))
            {
                int count = Integer.parseInt(particle.split(";")[1]); //TODO move to packet
                boiling(x, y, z, count);
            }
            else if (particle.equalsIgnoreCase(EffectRefs.BOILER_COMPLETE))
            {
                boilerComplete(x, y, z);
            }
            else if (particle.equalsIgnoreCase(EffectRefs.BOILER_RUNNING))
            {
                centrifugeRunning(x, y, z);
            }
            else if (particle.equalsIgnoreCase(EffectRefs.CENTRIFUGE_COMPLETE))
            {
                centrifugeComplete(x, y, z);
            }
            else if (particle.equalsIgnoreCase(EffectRefs.CENTRIFUGE_RUNNING))
            {
                centrifugeRunning(x, y, z);
            }
            else if (particle.equalsIgnoreCase(EffectRefs.EXTRACTOR_COMPLETE))
            {
                extractorComplete(x, y, z, (int) vx);
            }

            else if (particle.equalsIgnoreCase(EffectRefs.EXTRACTOR_RUNNING))
            {
                extractorRunning(x, y, z, (int) vx);
            }
            else
            {
                Minecraft.getMinecraft().theWorld.spawnParticle(particle, x, y, z, vx, vy, vz);
            }
        }
    }

    private void boiling(double x, double y, double z, int count)
    {
        if (ConfigClient.BOILING_EFFECT)
        {
            for (int i = 0; i < count; i++)
            {
                int xi = (int) Math.floor(x);
                int yi = (int) Math.floor(y);
                int zi = (int) Math.floor(z);
                Block block = Minecraft.getMinecraft().theWorld.getBlock(xi, yi + 1, zi);
                if (block != null && block.isAir(Minecraft.getMinecraft().theWorld, xi, yi + 1, zi))
                {
                    Minecraft.getMinecraft().theWorld.spawnParticle("splash",
                            x + r(0.4),
                            y + 0.6 + r(0.1),
                            z + r(0.4),
                            0, 0, 0);
                }

                Minecraft.getMinecraft().theWorld.spawnParticle("bubble",
                        x + r(0.5),
                        y + r(0.5),
                        z + r(0.5),
                        r(0.1) - r(0.1),
                        r(0.1),
                        r(0.1) - r(0.1));
            }
        }
    }

    private void boilerComplete(double x, double y, double z)
    {
        if (ConfigClient.MACHINE_COMPLETE)
        {
            final float randomSpeed = 0.05f;
            int rand = Minecraft.getMinecraft().theWorld.rand.nextInt(5);
            Color color = Color.GREEN.darker().darker();
            for (int i = 0; i < 10 + rand; i++)
            {
                FxSmoke smoke = new FxSmoke(Minecraft.getMinecraft().theWorld,
                        x,
                        y - 0.3,
                        z,
                        r(randomSpeed) - r(randomSpeed),
                        r(randomSpeed) - r(randomSpeed),
                        r(randomSpeed) - r(randomSpeed),
                        (float) (1f - r(0.2) + r(0.2)));

                if (r(1) > 0.5)
                {
                    color.darker();
                }

                Minecraft.getMinecraft().effectRenderer.addEffect(smoke.setColor(color));
            }

            for (int i = 0; i < 10 + rand; i++)
            {
                FxSmoke smoke = new FxSmoke(Minecraft.getMinecraft().theWorld,
                        x,
                        y,
                        z,
                        r(randomSpeed) - r(randomSpeed),
                        r(randomSpeed) - r(randomSpeed),
                        r(randomSpeed) - r(randomSpeed),
                        (float) (1f - r(0.2) + r(0.2)));

                if (r(1) > 0.5)
                {
                    color.darker();
                }

                Minecraft.getMinecraft().effectRenderer.addEffect(smoke.setColor(color));
            }
        }
    }

    private void centrifugeComplete(double x, double y, double z)
    {
        if (ConfigClient.MACHINE_COMPLETE)
        {
            final float randomSpeed = 0.05f;
            int rand = Minecraft.getMinecraft().theWorld.rand.nextInt(5);
            Color color = Color.GREEN.darker().darker();
            for (int i = 0; i < 10 + rand; i++)
            {
                FxSmoke smoke = new FxSmoke(Minecraft.getMinecraft().theWorld,
                        x,
                        y,
                        z,
                        r(randomSpeed) - r(randomSpeed),
                        r(randomSpeed) - r(randomSpeed),
                        r(randomSpeed) - r(randomSpeed),
                        (float) (1f - r(0.2) + r(0.2)));

                if (r(1) > 0.5)
                {
                    color.darker();
                }

                Minecraft.getMinecraft().effectRenderer.addEffect(smoke.setColor(color));
            }
        }
    }

    private void centrifugeRunning(double x, double y, double z)
    {
        if (ConfigClient.MACHINE_RUNNING)
        {
            final float randomSpeed = 0.02f;
            FxSmoke smoke = new FxSmoke(Minecraft.getMinecraft().theWorld,
                    x,
                    y,
                    z,
                    r(randomSpeed) - r(randomSpeed),
                    r(randomSpeed) - r(randomSpeed),
                    r(randomSpeed) - r(randomSpeed),
                    (float) (1f - r(0.2) + r(0.2)));

            Minecraft.getMinecraft().effectRenderer.addEffect(smoke.setColor(Color.GREEN));
        }
    }

    private void extractorComplete(double x, double y, double z, int facing)
    {
        if (ConfigClient.MACHINE_COMPLETE)
        {
            final float randomSpeed = 0.05f;
            int rand = Minecraft.getMinecraft().theWorld.rand.nextInt(5);
            Color color = Color.GREEN.darker().darker();

            ForgeDirection direction = ForgeDirection.getOrientation(facing);
            for (int i = 0; i < 10 + rand; i++)
            {
                FxSmoke smoke = new FxSmoke(Minecraft.getMinecraft().theWorld,
                        x + direction.offsetX * 0.2,
                        y + direction.offsetY * 0.2,
                        z + direction.offsetZ * 0.2,
                        r(randomSpeed) - r(randomSpeed),
                        r(randomSpeed) - r(randomSpeed),
                        r(randomSpeed) - r(randomSpeed),
                        (float) (1f - r(0.2) + r(0.2)));

                if (r(1) > 0.5)
                {
                    color.darker();
                }

                Minecraft.getMinecraft().effectRenderer.addEffect(smoke.setColor(color));
            }
        }
    }

    private void extractorRunning(double x, double y, double z, int facing)
    {
        if (ConfigClient.MACHINE_RUNNING)
        {
            final float randomSpeed = 0.02f;

            ForgeDirection direction = ForgeDirection.getOrientation(facing);

            FxSmoke smoke = new FxSmoke(Minecraft.getMinecraft().theWorld,
                    x + direction.offsetX * 0.2,
                    y + direction.offsetY * 0.2,
                    z + direction.offsetZ * 0.2,
                    r(randomSpeed) - r(randomSpeed),
                    r(randomSpeed) - r(randomSpeed),
                    r(randomSpeed) - r(randomSpeed),
                    (float) (1f - r(0.2) + r(0.2)));
            Minecraft.getMinecraft().effectRenderer.addEffect(smoke.setColor(Color.GREEN));

            smoke = new FxSmoke(Minecraft.getMinecraft().theWorld,
                    x + direction.offsetX * 0.2 + direction.offsetZ * 0.3,
                    y + direction.offsetY * 0.2,
                    z + direction.offsetZ * 0.2 + direction.offsetX * 0.3,
                    r(randomSpeed) - r(randomSpeed),
                    r(randomSpeed) - r(randomSpeed),
                    r(randomSpeed) - r(randomSpeed),
                    (float) (1f - r(0.2) + r(0.2)));
            Minecraft.getMinecraft().effectRenderer.addEffect(smoke.setColor(Color.GREEN));

            smoke = new FxSmoke(Minecraft.getMinecraft().theWorld,
                    x + direction.offsetX * 0.2 - direction.offsetZ * 0.3,
                    y + direction.offsetY * 0.2,
                    z + direction.offsetZ * 0.2 - direction.offsetX * 0.3,
                    r(randomSpeed) - r(randomSpeed),
                    r(randomSpeed) - r(randomSpeed),
                    r(randomSpeed) - r(randomSpeed),
                    (float) (1f - r(0.2) + r(0.2)));
            Minecraft.getMinecraft().effectRenderer.addEffect(smoke.setColor(Color.GREEN));
        }
    }

    private void reactorRunning(double x, double y, double z)
    {
        if (ConfigClient.REACTOR_RUNNING)
        {
            final float randomSpeed = 0.05f;
            Color color = Color.GREEN;
            for (int j = 0; j < 6; j++)
            {
                for (int i = 0; i < 4; i++)
                {
                    FxSmoke smoke = new FxSmoke(Minecraft.getMinecraft().theWorld,
                            x + r(0.01) - r(0.01),
                            y + 0.15 - 0.15 * j,
                            z + r(0.01) - r(0.01),
                            r(randomSpeed) - r(randomSpeed),
                            r(0.01) - r(0.01),
                            r(randomSpeed) - r(randomSpeed),
                            (float) (1f - r(0.2) + r(0.2)))
                            .setColor(color)
                            .setYAcceleration(0);

                    if (r(1) > 0.5)
                    {
                        color = color.darker();
                    }
                    else
                    {
                        color = color.brighter();
                    }

                    Minecraft.getMinecraft().effectRenderer.addEffect(smoke);
                }
            }
        }
    }

    private double r(double random)
    {
        return Math.random() * random - Math.random() * random;
    }
}
