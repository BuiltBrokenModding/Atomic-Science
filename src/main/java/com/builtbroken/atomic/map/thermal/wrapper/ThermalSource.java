package com.builtbroken.atomic.map.thermal.wrapper;

import com.builtbroken.atomic.api.thermal.IThermalSource;
import com.builtbroken.atomic.map.data.IDataPoolObject;
import com.builtbroken.atomic.map.data.node.DataMapType;
import com.builtbroken.atomic.map.data.node.IThermalNode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public abstract class ThermalSource<E> implements IThermalSource
{
    public final E host;

    public HashMap<BlockPos, IThermalNode> nodes;

    protected ThermalSource(E host)
    {
        this.host = host;
    }

    @Override
    public HashMap<BlockPos, IThermalNode> getCurrentNodes()
    {
        return nodes;
    }

    @Override
    public void setCurrentNodes(HashMap<BlockPos, IThermalNode> map)
    {
        nodes = map;
    }

    @Override
    public void ClearMapData()
    {
        if(nodes != null)
        {
            for(IThermalNode node : nodes.values())
            {
                if(node instanceof IDataPoolObject)
                {
                    ((IDataPoolObject) node).dispose();
                }
            }
            nodes.clear();
        }
    }

    @Override
    public boolean canGeneratingHeat()
    {
        return world() != null && getHeatGenerated() > 0;
    }

    @Override
    public DataMapType getType()
    {
        return DataMapType.THERMAL;
    }

    @Override
    public int dim()
    {
        return world().provider.getDimension();
    }

    public abstract World world();
}
