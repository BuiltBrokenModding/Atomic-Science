package com.builtbroken.atomic;

import com.builtbroken.atomic.content.ASClientReg;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

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

    public void spawnParticle(String particle, double x, double y, double z, double vx, double vy, double vz)
    {
        if (Minecraft.getMinecraft().theWorld != null)
        {
            if (particle.startsWith("steam"))
            {

            }
            else if (particle.startsWith("boiling"))
            {
                int count = Integer.parseInt(particle.split(";")[1]);

                for (int i = 0; i < count; i++)
                {
                    int xi = (int) Math.floor(x);
                    int yi = (int) Math.floor(y);
                    int zi = (int) Math.floor(z);
                    Block block = Minecraft.getMinecraft().theWorld.getBlock(xi, yi + 1, zi);
                    if (block != null && block.isAir(Minecraft.getMinecraft().theWorld, xi, yi + 1, zi))
                    {
                        Minecraft.getMinecraft().theWorld.spawnParticle("splash",
                                x + 0.5 + r(0.4),
                                y + 1.1 + r(0.1),
                                z + 0.5 + r(0.4),
                                0, 0, 0);
                    }

                    Minecraft.getMinecraft().theWorld.spawnParticle("bubble",
                            x + 0.5 + r(0.5),
                            y + 0.5 + r(0.5),
                            z + 0.5 + r(0.5),
                            r(0.1) -  r(0.1),
                            r(0.1),
                            r(0.1) -  r(0.1));
                }
            }
            else
            {
                Minecraft.getMinecraft().theWorld.spawnParticle(particle, x, y, z, vx, vy, vz);
            }
        }
    }

    private double r(double random)
    {
        return Math.random() * random - Math.random() * random;
    }
}
