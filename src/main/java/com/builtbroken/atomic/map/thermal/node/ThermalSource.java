package com.builtbroken.atomic.map.thermal.node;

import com.builtbroken.atomic.api.thermal.IThermalSource;
import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.thermal.IThermalNode;
import com.builtbroken.atomic.map.data.node.MapNodeSource;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public abstract class ThermalSource<E> extends MapNodeSource<E, IThermalNode> implements IThermalSource
{
    protected ThermalSource(E host)
    {
        super(host);
    }

    @Override
    public boolean canGeneratingHeat()
    {
        return world() != null && getHeatGenerated() > 0;
    }

    @Override
    public DataMapType getType()
    {
        return DataMapType.THERMAL;
    }
}
