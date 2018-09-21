package com.builtbroken.atomic.map.exposure.wrapper;

import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.map.data.IDataPoolObject;
import com.builtbroken.atomic.map.data.node.DataMapType;
import com.builtbroken.atomic.map.data.node.IRadiationNode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public abstract class RadiationSource<E> implements IRadiationSource
{
    public final E host;
    public HashMap<BlockPos, IRadiationNode> nodes;

    public RadiationSource(E host)
    {
        this.host = host;
    }

    @Override
    public HashMap<BlockPos, IRadiationNode> getCurrentNodes()
    {
        return nodes;
    }

    @Override
    public void setCurrentNodes(HashMap<BlockPos, IRadiationNode> map)
    {
        nodes = map;
    }

    @Override
    public boolean isRadioactive()
    {
        return world() != null && getRadioactiveMaterial() > 0;
    }

    @Override
    public DataMapType getType()
    {
        return DataMapType.RADIATION;
    }

    @Override
    public void ClearMapData()
    {
        if(nodes != null)
        {
            for(IRadiationNode node : nodes.values())
            {
                if(node instanceof IDataPoolObject)
                {
                    ((IDataPoolObject) node).dispose();
                }
            }
            nodes.clear();
        }
    }

    public abstract World world();

    @Override
    public int dim()
    {
        return world().provider.getDimension();
    }
}
