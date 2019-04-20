package com.builtbroken.atomic.map.thermal.node;

import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.thermal.IThermalNode;
import com.builtbroken.atomic.api.thermal.IThermalSource;
import com.builtbroken.atomic.map.data.node.MapNodeSource;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public abstract class ThermalSource<E> extends MapNodeSource<E, IThermalNode> implements IThermalSource
{
    public static final String NBT_HEAT = "heat";

    protected ThermalSource(E host)
    {
        super(host);
    }

    @Override
    public boolean canGeneratingHeat()
    {
        return getHeatGenerated() > 0;
    }

    @Override
    public boolean isStillValid()
    {
        return super.isStillValid() && canGeneratingHeat();
    }

    @Override
    public DataMapType getType()
    {
        return DataMapType.THERMAL;
    }

    @Override
    public NBTTagCompound getSaveState()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger(NBT_HEAT, getHeatGenerated());
        return tagCompound;
    }

    @Override
    public boolean shouldQueueForUpdate(NBTTagCompound saveState)
    {
        final int heat = getHeatGenerated();
        if(heat > 0 && !hasNodes())
        {
            return true;
        }
        return saveState == null || saveState.getInteger(NBT_HEAT) != heat;
    }

    @Override
    protected String addDebugInfo()
    {
        return "HEAT: " + canGeneratingHeat() + "-" + getHeatGenerated() + ", ";
    }

    @Override
    protected String getDebugName()
    {
        return "RadiationSource";
    }
}
