package com.builtbroken.atomic.api.radiation;

import com.builtbroken.atomic.api.map.IDataMapNode;

/**
 *
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
