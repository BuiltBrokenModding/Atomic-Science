package com.builtbroken.atomic.content.machines.reactor.fission.core;

import com.google.common.collect.Lists;
import net.minecraft.block.properties.PropertyEnum;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/13/2018.
 */
public class PropertyReactorState extends PropertyEnum<ReactorStructureType>
{
    protected PropertyReactorState()
    {
        super("type", ReactorStructureType.class, Lists.newArrayList(ReactorStructureType.values()));
    }
}
