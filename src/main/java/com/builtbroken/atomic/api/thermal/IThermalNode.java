package com.builtbroken.atomic.api.thermal;

import com.builtbroken.atomic.api.map.IDataMapNode;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public interface IThermalNode extends IDataMapNode
{
    int getHeatValue();

    void setHeatValue(int value);
}
