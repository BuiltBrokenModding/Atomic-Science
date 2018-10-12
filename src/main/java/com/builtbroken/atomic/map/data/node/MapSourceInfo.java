package com.builtbroken.atomic.map.data.node;

import com.builtbroken.atomic.api.map.IDataMapSource;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/12/2018.
 */
class MapSourceInfo
{
    public final IDataMapSource source;

    private NBTTagCompound saveState;

    public MapSourceInfo(IDataMapSource source)
    {
        this.source = source;
    }


    public void logState()
    {
        saveState = source.getSaveState();
    }

    public boolean needsQueued()
    {
        return source.shouldQueueForUpdate(saveState);
    }
}
