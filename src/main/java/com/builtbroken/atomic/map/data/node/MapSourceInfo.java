package com.builtbroken.atomic.map.data.node;

import com.builtbroken.atomic.api.map.IDataMapSource;
import net.minecraft.nbt.NBTTagCompound;

/**
 *
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
