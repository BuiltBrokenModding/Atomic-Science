package com.builtbroken.atomic.map.data.node;

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
    public final E host;
    public HashMap<BlockPos, N> nodes;

    public MapNodeSource(E host)
    {
        this.host = host;
    }


    public HashMap<BlockPos, N> getCurrentNodes()
    {
        return nodes;
    }

    @Override
    public boolean isStillValid()
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
    public void clearMapData()
    {
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
        if (getCurrentNodes() != null)
        {
            for (BlockPos pos : getCurrentNodes().keySet())
            {
                MapHandler.GLOBAL_DATA_MAP.removeData(dim(), pos, this);
            }
        }
    }

    @Override
    public void connectMapData()
    {
        if (getCurrentNodes() != null)
        {
            for (Map.Entry<BlockPos, N> entry : getCurrentNodes().entrySet())
            {
                MapHandler.GLOBAL_DATA_MAP.addData(dim(), entry.getKey(), entry.getValue());
            }
        }
    }

    public abstract World world();

    @Override
    public int dim()
    {
        return world().provider.getDimension();
    }
}
