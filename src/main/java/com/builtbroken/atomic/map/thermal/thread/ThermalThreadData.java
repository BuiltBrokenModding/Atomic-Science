package com.builtbroken.atomic.map.thermal.thread;

import com.builtbroken.atomic.lib.transform.IPosWorld;
import com.builtbroken.atomic.map.data.DataPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dark(DarkGuardsman, Robert) on 7/6/2019.
 */
public class ThermalThreadData implements IPosWorld
{
    public final World world;
    public final DataPos pos;
    public final int range;

    //Track data, also used to prevent looping same tiles (first pos is location, second stores data)
    private final HashMap<DataPos, DataPos> heatSpreadData = new HashMap();

    public ThermalThreadData(World world, int cx, int cy, int cz, int range)
    {
        this.world = world;
        this.pos = DataPos.get(cx, cy, cz);
        this.range = range;
    }

    public void setData(DataPos pos, DataPos data)
    {
        heatSpreadData.put(pos, data);
    }

    public boolean hasData(DataPos pos)
    {
        return heatSpreadData.containsKey(pos);
    }

    public int getHeat(DataPos currentPos)
    {
        if (heatSpreadData.containsKey(currentPos))
        {
            return heatSpreadData.get(currentPos).x;
        }
        return 0;
    }

    public void setHeat(DataPos currentPos, int heatAsPosition)
    {
        if (heatSpreadData.containsKey(currentPos))
        {
            heatSpreadData.get(currentPos).x = heatAsPosition;
        }
        else
        {
            setData(currentPos, DataPos.get(heatAsPosition, 0, 0));
        }
    }

    public void addHeatMoved(DataPos pos, int heatMoved)
    {
        if (heatSpreadData.containsKey(pos))
        {
            heatSpreadData.get(pos).y += heatMoved;
        }
        else
        {
            setData(pos, DataPos.get(0, heatMoved, 0));
        }
    }

    @Override
    public int dim()
    {
        return world.provider.getDimension();
    }

    @Override
    public double z()
    {
        return pos.z;
    }

    @Override
    public double x()
    {
        return pos.x;
    }

    @Override
    public double y()
    {
        return pos.y;
    }

    @Override
    public int zi()
    {
        return pos.z;
    }

    @Override
    public int xi()
    {
        return pos.x;
    }

    @Override
    public int yi()
    {
        return pos.y;
    }

    public Map<DataPos, DataPos> getData()
    {
        return heatSpreadData;
    }
}
