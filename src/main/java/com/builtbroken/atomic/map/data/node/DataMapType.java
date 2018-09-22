package com.builtbroken.atomic.map.data.node;

import java.util.ArrayList;

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
    }),
    RADIATION(node ->
    {
        if (node instanceof IRadiationNode)
        {
            return ((IRadiationNode) node).getRadiationValue();
        }
        return 0;
    }),
    RAD_MATERIAL(node ->
    {
        if (node instanceof IRadMaterialNode)
        {
            return ((IRadMaterialNode) node).getRadMaterialValue();
        }
        return 0;
    });

    public IntReturnFunction<IDataMapNode> function;

    DataMapType(IntReturnFunction<IDataMapNode> function)
    {
        this.function = function;
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
