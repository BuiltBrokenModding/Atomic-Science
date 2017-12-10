package com.builtbroken.atomicscienceclassic;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundHandler
{
    public static final SoundHandler INSTANCE = new SoundHandler();

    public static final String[] SOUND_FILES =
            {"antimatter.ogg", "strangematter.ogg", "alarm.ogg", "accelerator.ogg", "turbine.ogg", "assembler.ogg", "reactorcell.ogg"};

}