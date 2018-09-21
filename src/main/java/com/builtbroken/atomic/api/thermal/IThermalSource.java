package com.builtbroken.atomic.api.thermal;

import com.builtbroken.atomic.lib.transform.IPosWorld;
import com.builtbroken.atomic.map.data.node.IDataMapSource;
import com.builtbroken.atomic.map.data.node.IThermalNode;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

/**
 * Applied to object that act as heat sources in the world
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public interface IThermalSource extends IPosWorld, IDataMapSource
{
    /**
     * Is the current object able to generate heat.
     * <p>
     * Use this method to indicate to the thermal system
     * if the tile is able to output heat.
     * <p>
     * If return false the source will be removed from
     * the thermal system. In order for the system to
     * interaction with the tile it must be registered.
     *
     * @return true if it can
     */
    boolean canGeneratingHeat();

    /**
     * Gets the heat currently generated
     * <p>
     * Value should be cached each tick to prevent
     * issues.
     * <p>
     * Each time the value changes the thermal system will
     * update the map.
     *
     * @return heat
     */
    int getHeatGenerated();

    HashMap<BlockPos, IThermalNode> getCurrentNodes();

    void setCurrentNodes(HashMap<BlockPos, IThermalNode> map);
}
