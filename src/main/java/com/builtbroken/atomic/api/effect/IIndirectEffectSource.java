package com.builtbroken.atomic.api.effect;

import com.builtbroken.jlib.data.vector.IPos3D;
import net.minecraft.world.World;

/**
 * Applied to the source of {@link IIndirectEffectInstance}
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 4/19/2018.
 */
public interface IIndirectEffectSource extends IPos3D
{
    World world();
}
