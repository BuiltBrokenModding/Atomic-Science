package com.builtbroken.atomic.api.reactor;

import com.builtbroken.atomic.api.item.IFuelRodItem;
import com.builtbroken.atomic.api.thermal.IThermalSource;
import com.builtbroken.atomic.lib.transform.IPosWorld;
import net.minecraft.item.ItemStack;

/**
 * Applied to reactors
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public interface IReactor extends IPosWorld
{
    /**
     * Gets the current fuel rod item instance
     *
     * @return fuel rod item
     */
    IFuelRodItem getFuelRod();

    /**
     * Gets the current fuel rod item as a stack
     *
     * @return fuel rod itemstack
     */
    ItemStack getFuelRodStack();

    /**
     * Called to get the object that handles
     * heat for the reactor. This should be a
     * capability on the tile itself.
     *
     * @return
     */
    IThermalSource getHeatSource();
}
