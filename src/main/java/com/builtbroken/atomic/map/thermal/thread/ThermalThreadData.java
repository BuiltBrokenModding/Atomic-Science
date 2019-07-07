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
    private final HashMap<DataPos, ThermalData> heatSpreadData = new HashMap();

    public ThermalThreadData(World world, int cx, int cy, int cz, int range)
    {
        this.world = world;
        this.pos = DataPos.get(cx, cy, cz);
        this.range = range;
    }

    public boolean hasData(DataPos pos)
    {
        return heatSpreadData.containsKey(pos);
    }

    public boolean canReceive(DataPos pos)
    {
        if (heatSpreadData.containsKey(pos))
        {
            return !heatSpreadData.get(pos).hasPushedHeat();
        }
        return true;
    }

    public DataPos setToPush(DataPos nextPathPos)
    {
        if (heatSpreadData.containsKey(pos))
        {
            heatSpreadData.get(pos).setToPush();
        }
        return nextPathPos;
    }

    public int getHeatToMove(DataPos pos)
    {
        if (heatSpreadData.containsKey(pos))
        {
            return heatSpreadData.get(pos).getHeatToPush();
        }
        return 0;
    }

    public int getHeat(DataPos currentPos)
    {
        if (heatSpreadData.containsKey(currentPos))
        {
            return heatSpreadData.get(currentPos).getHeat();
        }
        return 0;
    }

    public void setHeat(DataPos pos, int heatAsPosition)
    {
        if (heatSpreadData.containsKey(pos))
        {
            heatSpreadData.get(pos).setHeat(heatAsPosition);
        }
        else
        {
            heatSpreadData.put(DataPos.get(pos), ThermalData.get(heatAsPosition));
        }
    }

    public void addHeat(DataPos currentPos, int heatAsPosition)
    {
        setHeat(currentPos, getHeat(currentPos) + heatAsPosition);
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

    public Map<DataPos, ThermalData> getData()
    {
        return heatSpreadData;
    }
}
