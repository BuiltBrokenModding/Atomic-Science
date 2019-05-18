package com.builtbroken.atomic.content.machines.laser.emitter;

import com.builtbroken.atomic.client.EffectRefs;
import com.builtbroken.atomic.config.content.ConfigContent;

import java.awt.*;
import java.util.function.IntSupplier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 5/18/2019.
 */
public enum LaserModes
{
    NORMAL(EffectRefs.LAZER_NORMAL,
            EffectRefs.LAZER_NORMAL_FIRE,
            Color.RED,
            () -> ConfigContent.LASER.FIRING_COST),
    FIELD(EffectRefs.LAZER_FIELD,
            EffectRefs.LAZER_FIELD_FIRE,
            new Color(127, 235, 255), //Redstone particle checks for red == 0
            () -> ConfigContent.LASER.FIRING_COST_CONSTANT);


    public final String warmedUpParticle;
    public final String fireParticle;
    public final Color color;
    public IntSupplier energyCost;

    LaserModes(String warmedUpParticle, String fireParticle, Color color, IntSupplier energyCost)
    {
        this.warmedUpParticle = warmedUpParticle;
        this.fireParticle = fireParticle;
        this.color = color;
        this.energyCost = energyCost;
    }

    public static LaserModes get(int meta)
    {
        if (meta > 0 && meta < values().length)
        {
            return values()[meta];
        }
        return NORMAL;
    }

    public int getEnergyCost()
    {
        return energyCost.getAsInt();
    }
}
