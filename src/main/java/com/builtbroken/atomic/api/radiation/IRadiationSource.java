package com.builtbroken.atomic.api.radiation;

import com.builtbroken.atomic.api.map.IDataMapSource;
import com.builtbroken.atomic.lib.transform.IPosWorld;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

/**
 * Applied to objects that emmit radiation into the environment. Will
 * be tracked by the exposure thread and updated each tick.
 * <p>
 * Source has to be registered to the radiation exposure system in order to be tracked.
 * <p>
 * If the source becomes unradioactive it will unregister. As this is considered
 * to mean the object is gone or no longer present in the map.
 * <p>
 * If the source
 * becomes radioactive again. It will need to be registered again to the map to work.
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public interface IRadiationSource extends IPosWorld, IDataMapSource
{
    /**
     * Current map of positions to nodes
     *
     * @return
     */
    HashMap<BlockPos, IRadiationNode> getCurrentNodes();

    /**
     * Sets current map of positions to nodes
     * <p>
     * Should only be called from pathing thread
     */
    void setCurrentNodes(HashMap<BlockPos, IRadiationNode> map);

    /**
     * Gets the amount of radioactive material
     * this source represents.
     * <p>
     * Used to calculate amount of radiation to emit in terms
     * of REMs. Assumed that the material is generic and emits
     * REMs instead of other rad types.
     * <p>
     * This value should not change each time the method is called. If
     * the value is randomized it should be cached until next tick.
     *
     * @return material value
     */
    int getRadioactiveMaterial();

    /**
     * Is the source still radioactive at the time
     * this was called.
     * <p>
     * {@link #isStillValid()} is to be used as a validation
     * check instead of this method.
     *
     * @return true if is still radioactive
     */
    default boolean isRadioactive()
    {
        return true;
    }
}
