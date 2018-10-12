package com.builtbroken.atomic.api.map;

import com.builtbroken.atomic.api.radiation.IRadMaterialNode;
import com.builtbroken.atomic.api.radiation.IRadiationNode;
import com.builtbroken.atomic.api.radiation.IRadiationSource;
import com.builtbroken.atomic.api.thermal.IThermalNode;
import com.builtbroken.atomic.api.thermal.IThermalSource;
import com.builtbroken.atomic.map.MapHandler;
import com.builtbroken.atomic.map.data.DataChange;
import com.builtbroken.atomic.map.data.node.MapDataSources;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/21/2018.
 */
public enum DataMapType
{
    //Feel free to PR more node types if you want to make ADDONS
    THERMAL(node ->
    {
        if (node instanceof IThermalNode)
        {
            return ((IThermalNode) node).getHeatValue();
        }
        return 0;
    },
            source -> {
                if (source instanceof IThermalSource)
                {
                    if (((IThermalSource) source).canGeneratingHeat() && ((IThermalSource) source).getHeatGenerated() > 0)
                    {
                        MapDataSources.onAddedToThread(source);

                        DataChange change = DataChange.get(source, ((IThermalSource) source).getHeatGenerated());
                        change.completionListener = source1 -> MapDataSources.onRemovedFromThread(source);

                        MapHandler.THREAD_THERMAL_ACTION.queuePosition(change);
                    }
                }
            }),
    RADIATION(node ->
    {
        if (node instanceof IRadiationNode)
        {
            return ((IRadiationNode) node).getRadiationValue();
        }
        return 0;
    },
            source -> {
                if (source instanceof IRadiationSource)
                {
                    if (((IRadiationSource) source).isRadioactive() && ((IRadiationSource) source).getRadioactiveMaterial() > 0)
                    {
                        MapDataSources.onAddedToThread(source);

                        DataChange change = DataChange.get(source, ((IRadiationSource) source).getRadioactiveMaterial());
                        change.completionListener = source1 -> MapDataSources.onRemovedFromThread(source);

                        MapHandler.THREAD_RAD_EXPOSURE.queuePosition(change);
                    }
                }
            }),
    RAD_MATERIAL(node ->
    {
        if (node instanceof IRadMaterialNode)
        {
            return ((IRadMaterialNode) node).getRadMaterialValue();
        }
        return 0;
    }, source -> {
    });

    public IntReturnFunction<IDataMapNode> function;
    public Consumer<IDataMapSource> queueThread;

    DataMapType(IntReturnFunction<IDataMapNode> function, Consumer<IDataMapSource> queueThread)
    {
        this.function = function;
        this.queueThread = queueThread;
    }

    public int getValue(IDataMapNode node)
    {
        return node != null && node.isNodeValid() ? function.apply(node) : 0;
    }

    public int getValue(ArrayList<IDataMapNode> nodes)
    {
        int value = 0;
        if (nodes != null)
        {
            for (IDataMapNode node : nodes)
            {
                value += getValue(node);
            }
        }
        return value;
    }

    public void queueSourceForUpdate(IDataMapSource source)
    {
        if (queueThread != null)
        {
            queueThread.accept(source);
        }
    }

    @FunctionalInterface
    public interface IntReturnFunction<R>
    {

        /**
         * Applies this function to the given argument.
         *
         * @param value the function argument
         * @return the function result
         */
        int apply(R value);
    }
}
