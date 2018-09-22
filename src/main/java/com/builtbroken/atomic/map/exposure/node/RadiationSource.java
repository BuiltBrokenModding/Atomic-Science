package com.builtbroken.atomic.map.exposure.node;

import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.map.data.node.DataMapType;
import com.builtbroken.atomic.map.data.node.IRadiationNode;
import com.builtbroken.atomic.map.data.node.MapNodeSource;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public abstract class RadiationSource<E>  extends MapNodeSource<E, IRadiationNode> implements IRadiationSource
{

    public RadiationSource(E host)
    {
        super(host);
    }

    @Override
    public boolean isRadioactive()
    {
        return isStillValid() && getRadioactiveMaterial() > 0;
    }

    @Override
    public DataMapType getType()
    {
        return DataMapType.RADIATION;
    }
}
