package com.builtbroken.atomic.client;

import com.builtbroken.atomic.CommonProxy;
import com.builtbroken.atomic.client.fx.FxSmoke;
import com.builtbroken.atomic.config.ConfigClient;
import com.builtbroken.atomic.content.ASClientReg;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.util.ForgeDirection;

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
        ASClientReg.register();
    }

    @Override
    public void init()
    {
        super.init();
    }

    @Override
    public void spawnParticle(String particle, double x, double y, double z, double vx, double vy, double vz)
    {
        //TODO build an effect system to register effects
        if (Minecraft.getMinecraft().theWorld != null)
        {
            if (particle.startsWith(EffectRefs.STEAM))
            {

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

    private double r(double random)
    {
        return Math.random() * random - Math.random() * random;
    }
}
