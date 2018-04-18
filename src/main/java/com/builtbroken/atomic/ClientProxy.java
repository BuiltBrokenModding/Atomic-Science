package com.builtbroken.atomic;

import com.builtbroken.atomic.content.ASClientReg;

public class ClientProxy extends CommonProxy
{
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
