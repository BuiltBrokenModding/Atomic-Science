package com.builtbroken.atomic.api.radiation;

import net.minecraft.item.ItemStack;

/**
 * Applied to an item to track how much radiation it will produced based on its radioactive material value.
 * <p>
 * Will be wrapped by {@link IRadiationSource} for logic in the actual world.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/8/2018.
 */
@Deprecated
public interface IRadioactiveItem
{
    /**
     * Gets the amount of radioactive material
     * this item represents.
     * <p>
     * Used to calculate amount of radiation to emit in terms
     * of REMs. Assumed that the material is generic and emits
     * REMs instead of other rad types.
     *
     * @return material value
     */
    int getRadioactiveMaterial(ItemStack stack);

    /**
     * Is the item still radioactive at the time
     * this was called. Used as an isAlive() and isValid()
     * check to see if the source needs to be removed.
     *
     * @return true if is still radioactive
     */
    default boolean isRadioactive(ItemStack stack)
    {
        return true;
    }
}
