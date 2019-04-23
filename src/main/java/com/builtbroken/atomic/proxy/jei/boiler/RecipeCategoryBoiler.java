package com.builtbroken.atomic.proxy.jei.boiler;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.proxy.jei.TooltipCallbackFluid;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 9/16/2018.
 */
public class RecipeCategoryBoiler implements IRecipeCategory<RecipeWrapperBoiler>
{
    public static final String ID = AtomicScience.PREFIX + "chem.boiler";
    public static final ResourceLocation backgroundTexture = new ResourceLocation(AtomicScience.DOMAIN, "textures/gui/jei.png");

    IDrawable icon;
    IDrawable background;

    public RecipeCategoryBoiler(IJeiHelpers helpers)
    {
        icon = helpers.getGuiHelper().createDrawableIngredient(new ItemStack(ASBlocks.blockChemBoiler));
        background = helpers.getGuiHelper().createDrawable(backgroundTexture, 0, 0, 117, 18);
    }

    @Override
    public String getUid()
    {
        return ID;
    }

    @Override
    public String getTitle()
    {
        return ASBlocks.blockChemBoiler.getLocalizedName();
    }

    @Override
    public String getModName()
    {
        return AtomicScience.DOMAIN;
    }

    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperBoiler recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        guiItemStacks.init(0, true, 19, 0);
        guiItemStacks.init(1, false, 61, 0);

        guiFluidStacks.init(0, true, 1, 1);
        guiFluidStacks.init(1, false, 81, 1);
        guiFluidStacks.init(2, false, 100, 1);

        guiFluidStacks.addTooltipCallback(new TooltipCallbackFluid());

        List<ItemStack> inputs = recipeWrapper.recipe.getPossibleInputs();
        if (inputs != null)
        {
            guiItemStacks.set(0,  inputs);
        }
        List<ItemStack> outputs = recipeWrapper.recipe.getPossibleOutputs();
        if (outputs != null)
        {
            guiItemStacks.set(1, outputs);
        }

        if (recipeWrapper.recipe.inputTankBlue != null)
        {
            guiFluidStacks.set(0, recipeWrapper.recipe.inputTankBlue);
        }
        if (recipeWrapper.recipe.outputTankGreen != null)
        {
            guiFluidStacks.set(1, recipeWrapper.recipe.outputTankGreen);
        }
        if (recipeWrapper.recipe.outputTankYellow != null)
        {
            guiFluidStacks.set(2, recipeWrapper.recipe.outputTankYellow);
        }
    }
}
