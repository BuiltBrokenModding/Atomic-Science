package com.builtbroken.atomic.map.exposure.node;

import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.radiation.IRadiationNode;
import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.map.data.node.MapNodeSource;
import net.minecraft.nbt.NBTTagCompound;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public abstract class RadiationSource<E> extends MapNodeSource<E, IRadiationNode> implements IRadiationSource
{
    public static final String NBT_RAD = "rad";

    @Override
    public boolean isRadioactive()
    {
        return getRadioactiveMaterial() > 0;
    }

    @Override
    public boolean isStillValid()
    {
        return super.isStillValid() && isRadioactive();
    }

    @Override
    public DataMapType getType()
    {
        return DataMapType.RADIATION;
    }

    @Override
    public NBTTagCompound getSaveState()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger(NBT_RAD, getRadioactiveMaterial());
        return tagCompound;
    }

    @Override
    public boolean shouldQueueForUpdate(NBTTagCompound saveState)
    {
        final int material = getRadioactiveMaterial();
        if (material > 0 && !hasNodes())
        {
            return true;
        }
        return saveState == null || saveState.getInteger(NBT_RAD) != material;
    }

    @Override
    protected String addDebugInfo()
    {
        return "RAD: " + isRadioactive() + "-" + getRadioactiveMaterial() + ", ";
    }

    @Override
    protected String getDebugName()
    {
        return "RadiationSource";
    }
}
