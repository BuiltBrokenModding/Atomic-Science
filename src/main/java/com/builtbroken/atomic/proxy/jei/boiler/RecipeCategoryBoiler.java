package com.builtbroken.atomic.proxy.jei.boiler;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.lib.gui.GuiContainerBase;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/16/2018.
 */
public class RecipeCategoryBoiler implements IRecipeCategory
{
    IDrawable icon;
    IDrawable background;

    public RecipeCategoryBoiler(IJeiHelpers helpers)
    {
        background = helpers.getGuiHelper().drawableBuilder(GuiContainerBase.GUI_MC_BASE, 0, 168, 125, 18)
                .addPadding(0, 20, 0, 0)
                .build();
        icon = helpers.getGuiHelper().createDrawableIngredient(new ItemStack(ASBlocks.blockChemBoiler));
    }

    @Override
    public String getUid()
    {
        return AtomicScience.PREFIX + "chem.boiler";
    }

    @Override
    public String getTitle()
    {
        return "jei." + getUid() + ".title";
    }

    @Override
    public String getModName()
    {
        return AtomicScience.DOMAIN;
    }

    @Override
    public IDrawable getBackground()
    {
        return null;
    }

    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients)
    {

    }
}
