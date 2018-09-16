package com.builtbroken.atomic.proxy.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/16/2018.
 */
@JEIPlugin
public class JeiProxy implements IModPlugin
{
    public static IJeiHelpers jeiHelper;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {

    }

    @Override
    public void register(IModRegistry registry)
    {
        jeiHelper = registry.getJeiHelpers();
    }
}
