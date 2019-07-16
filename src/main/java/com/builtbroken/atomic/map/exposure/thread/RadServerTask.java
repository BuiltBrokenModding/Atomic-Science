package com.builtbroken.atomic.map.exposure.thread;

import com.builtbroken.atomic.api.radiation.IRadiationNode;
import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.map.exposure.node.RadiationNode;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-07-16.
 */
public class RadServerTask implements Runnable
{
    public final IRadiationSource source;
    public final HashMap<BlockPos, Integer> collectedData;

    public RadServerTask(IRadiationSource source, HashMap<BlockPos, Integer> collectedData)
    {
        this.source = source;
        this.collectedData = collectedData;
    }

    @Override
    public void run()
    {
        //Get data
        final HashMap<BlockPos, IRadiationNode> oldMap = source.getCurrentNodes();
        final HashMap<BlockPos, IRadiationNode> newMap = new HashMap();

        //Remove old data from map
        source.disconnectMapData();

        //Add new data, recycle old nodes to reduce memory churn
        for (Map.Entry<BlockPos, Integer> entry : collectedData.entrySet()) //TODO move this to source to give full control over data structure
        {
            final int value = entry.getValue();
            final BlockPos pos = entry.getKey();

            if (oldMap != null && oldMap.containsKey(pos))
            {
                final IRadiationNode node = oldMap.get(pos);
                if (node != null)
                {
                    //Update value
                    node.setRadiationValue(value);

                    //Store in new map
                    newMap.put(pos, node);
                }

                //Remove from old map
                oldMap.remove(pos);
            }
            else
            {
                newMap.put(pos, RadiationNode.get(source, value));
            }
        }

        //Clear old data
        source.disconnectMapData();
        source.clearMapData();

        //Set new data
        source.setCurrentNodes(newMap);

        //Tell the source to connect to the map
        source.connectMapData();

        //Trigger source update
        source.initMapData();
    }
}
