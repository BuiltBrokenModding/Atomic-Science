package com.builtbroken.atomic.map.thermal.thread;

import com.builtbroken.atomic.api.thermal.IThermalNode;
import com.builtbroken.atomic.api.thermal.IThermalSource;
import com.builtbroken.atomic.map.data.DataPos;
import com.builtbroken.atomic.map.thermal.node.ThermalNode;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-07-16.
 */
public class ThermalServerTask implements Runnable
{
    public final IThermalSource source;
    public final ThermalThreadData thermalThreadData;

    public ThermalServerTask(IThermalSource source, ThermalThreadData thermalThreadData)
    {
        this.source = source;
        this.thermalThreadData = thermalThreadData;
    }

    @Override
    public void run()
    {
        //Get data
        final HashMap<BlockPos, IThermalNode> oldMap = source.getCurrentNodes();
        final HashMap<BlockPos, IThermalNode> newMap = new HashMap();

        //Remove old data from map
        source.disconnectMapData();

        //Add new data, recycle old nodes to reduce memory churn
        for (Map.Entry<DataPos, ThermalData> entry : thermalThreadData.getData().entrySet()) //TODO move this to source to give full control over data structure
        {
            final BlockPos pos = entry.getKey().disposeReturnBlockPos();
            final int value = entry.getValue().getHeatAndDispose();

            //Attempt to recycle old data points
            if (oldMap != null && oldMap.containsKey(pos))
            {
                final IThermalNode node = oldMap.get(pos);
                if (node != null)
                {
                    //Update value
                    node.setHeatValue(value);

                    //Store in new map
                    newMap.put(pos, node);
                }

                //Remove from old map
                oldMap.remove(pos);
            }
            //Only make new data points as needed
            else
            {
                newMap.put(pos, ThermalNode.get(source, value));
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
