package com.builtbroken.atomic;

import com.builtbroken.atomic.content.ASClientReg;
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
        if(Minecraft.getMinecraft().theWorld != null)
        {
            Minecraft.getMinecraft().theWorld.spawnParticle(particle, x, y, z, vx, vy, vz);
        }
    }
}
