package com.builtbroken.atomic.content.machines.processing.recipes;

import com.builtbroken.atomic.content.machines.processing.TileEntityProcessingMachine;

/**
 * Prefab for recipes used in the processing machine
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public abstract class ProcessingRecipe<H extends TileEntityProcessingMachine> //TODO convert to interface for other mods to add recipes
{
    /**
     * Checks if the current state of the machine matches
     * a given recipe.
     * <p>
     * Use the machine to access inventories, fluid tanks,
     * and power as needed.
     *
     * @param machine - machine to apply recipe to
     * @return true if the state matches the recipe
     */
    public abstract boolean matches(H machine);

    /**
     * Called to process the recipe and apply
     * the results to the machine
     * <p>
     * Use the machine to access inventories, fluid tanks,
     * and power as needed.
     *
     * @param machine - machine to apply recipe to
     */
    public abstract void applyRecipe(H machine);
}
