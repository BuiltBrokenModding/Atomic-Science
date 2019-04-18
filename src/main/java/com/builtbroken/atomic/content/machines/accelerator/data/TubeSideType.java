package com.builtbroken.atomic.content.machines.accelerator.data;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/13/2019.
 */
public enum TubeSideType
{
    ENTER,
    EXIT,
    NONE;

    public TubeSideType getOpposite()
    {
        if(this == ENTER)
        {
            return EXIT;
        }
        else if(this == EXIT)
        {
            return ENTER;
        }
        return NONE;
    }
}
