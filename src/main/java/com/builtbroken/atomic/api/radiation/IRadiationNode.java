package com.builtbroken.atomic.api.radiation;

import com.builtbroken.atomic.api.map.IDataMapNode;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public interface IRadiationNode extends IDataMapNode
{
    /**
     * Gets the current value
     *
     * @return
     */
    int getRadiationValue();

    /**
     * Updates the current value
     *
     * @param value
     */
    void setRadiationValue(int value);
}
