package com.builtbroken.atomic.api.thermal;

import com.builtbroken.atomic.api.map.IDataMapNode;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public interface IThermalNode extends IDataMapNode
{
    int getHeatValue();

    void setHeatValue(int value);
}
