package com.builtbroken.atomic;

import com.builtbroken.atomic.content.ASClientReg;

public class ClientProxy extends CommonProxy
{
    /** Client Cache: Amount of rads in the environment were the player is standing */
    public static float RAD_EXPOSURE = 0;
    /** Client Cache: Amount of rads the player has taken */
    public static float RAD_PLAYER = 0;

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
}
