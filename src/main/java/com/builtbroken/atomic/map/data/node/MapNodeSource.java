package com.builtbroken.atomic.map.data.node;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.api.map.DataMapType;
import com.builtbroken.atomic.api.map.IDataMapNode;
import com.builtbroken.atomic.api.map.IDataMapSource;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.data.IDataPoolObject;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public abstract class MapNodeSource<E, N extends IDataMapNode> implements IDataMapSource
{
    public E host;
    public HashMap<BlockPos, N> nodes;

    private boolean connected = false;

    public MapNodeSource(E host)
    {
        this.host = host;
    }

    public HashMap<BlockPos, N> getCurrentNodes()
    {
        return nodes;
    }

    public int getNodeCount()
    {
        return nodes != null ? nodes.size() : -1;
    }

    protected boolean hasNodes()
    {
        return nodes != null && !nodes.isEmpty();
    }

    @Override
    public boolean isStillValid()
    {
        return doesSourceExist();
    }

    @Override
    public boolean doesSourceExist()
    {
        return world() != null;
    }

    public void setCurrentNodes(HashMap<BlockPos, N> map)
    {
        nodes = map;
    }

    @Override
    public DataMapType getType()
    {
        return DataMapType.RADIATION;
    }

    @Override
    public boolean hasMapData()
    {
        return nodes != null && !nodes.isEmpty();
    }

    @Override
    public boolean hasActiveMapData()
    {
        return hasMapData() && connected;
    }

    @Override
    public void clearMapData()
    {
        if (connected)
        {
            AtomicScience.logger.error("MapNodeSource#clearMapData() an attempt was made to clear map data without disconnecting", new RuntimeException("trace"));
            disconnectMapData();
        }
        if (getCurrentNodes() != null)
        {
            for (N node : getCurrentNodes().values())
            {
                if (node instanceof IDataPoolObject)
                {
                    ((IDataPoolObject) node).dispose();
                }
            }
            getCurrentNodes().clear();
        }
    }

    @Override
    public void disconnectMapData()
    {
        connected = false;
        if (getCurrentNodes() != null)
        {
            for (BlockPos pos : getCurrentNodes().keySet())
            {
                MapHandler.GLOBAL_DATA_MAP.removeData(world(), pos, this);
            }
        }
    }

    @Override
    public void connectMapData()
    {
        connected = true;
        if (getCurrentNodes() != null)
        {
            for (Map.Entry<BlockPos, N> entry : getCurrentNodes().entrySet())
            {
                MapHandler.GLOBAL_DATA_MAP.addData(world(), entry.getKey(), entry.getValue());
            }
        }
    }

    public abstract World world();

    @Override
    public int dim()
    {
        return world().provider.getDimension();
    }

    @Override
    public String toString()
    {
        return getDebugName() + "[H: " + host + ", " +
                "N: " + getNodeCount() + ", " +
                "D: " + dim() + ", " +
                "P: (" + xi() + ", " + yi() + ", " + zi() + ")," +
                " V: " + isStillValid() + ", " +
                "E: " + doesSourceExist() +
                addDebugInfo() +
                "]@" + hashCode();
    }

    protected String addDebugInfo()
    {
        return "";
    }

    protected abstract String getDebugName();
}
