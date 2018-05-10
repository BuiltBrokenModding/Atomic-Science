package com.builtbroken.atomic.api.item;

import com.builtbroken.atomic.api.radiation.IRadioactiveItem;
import com.builtbroken.atomic.api.reactor.IReactor;
import net.minecraft.item.ItemStack;

/**
 * Applied to an item that acts as a fuel rod for reactors
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
public interface IFuelRodItem extends IRadioactiveItem
{
    /**
     * Amount of time in ticks the fuel will run
     *
     * @param stack - fuel rod
     * @return ticks (20 ticks a second)
     */
    int getFuelRodRuntime(ItemStack stack);

    /**
     * Amount of time in ticks the fuel will run
     * when fully charged.
     *
     * @param stack - fuel rod
     * @return ticks (20 ticks a second)
     */
    int getMaxFuelRodRuntime(ItemStack stack);

    /**
     * Amount of heat this rod would produce
     * in a reactor with no limits.
     *
     * @param stack - fuel rod
     * @return heat value
     */
    int getHeatOutput(ItemStack stack);

    /**
     * Called each tick the fuel runs
     * <p>
     * Use this to decrease the fuel runtime
     *
     * @param reactor  - reactor consuming the fuel
     * @param stack    - fuel rod, use a copy if you don't want changes applied
     * @param tick     - current tick of the reactor (counted from area loaded)
     * @param fuelTick - current tick of the fuel rod
     * @return modified stack
     */
    ItemStack onReactorTick(IReactor reactor, ItemStack stack, int tick, int fuelTick);
}
