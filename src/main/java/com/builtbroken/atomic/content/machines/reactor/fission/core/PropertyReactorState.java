package com.builtbroken.atomic.content.machines.reactor.fission.core;

import com.google.common.collect.Lists;
import net.minecraft.block.properties.PropertyEnum;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/13/2018.
 */
public class PropertyReactorState extends PropertyEnum<ReactorStructureType>
{
    protected PropertyReactorState()
    {
        super("type", ReactorStructureType.class, Lists.newArrayList(ReactorStructureType.values()));
    }
}
