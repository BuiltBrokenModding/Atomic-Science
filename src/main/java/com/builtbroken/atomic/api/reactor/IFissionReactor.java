package com.builtbroken.atomic.api.reactor;

import com.builtbroken.atomic.api.radiation.IRadiationSource;

/**
 * Applied to fission reactors
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public interface IFissionReactor extends IReactor
{
    /**
     * Called to get the object that handles
     * radiation for the reactor. This should be a
     * capability on the tile itself.
     *
     * @return
     */
    IRadiationSource getRadiationSource();
}
