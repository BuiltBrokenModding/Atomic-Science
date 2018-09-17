package com.builtbroken.atomic.proxy.jei.extractor;

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
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/16/2018.
 */
public class RecipeCategoryExtractor implements IRecipeCategory<RecipeWrapperExtractor>
{
    public static final String ID = AtomicScience.PREFIX + "chem.extractor";
    public static final ResourceLocation backgroundTexture = new ResourceLocation(AtomicScience.DOMAIN, "textures/gui/jei.png");

    IDrawable icon;
    IDrawable background;

    public RecipeCategoryExtractor(IJeiHelpers helpers)
    {
        icon = helpers.getGuiHelper().createDrawableIngredient(new ItemStack(ASBlocks.blockChemExtractor));
        background = helpers.getGuiHelper().createDrawable(backgroundTexture, 0, 0, 98, 18);
    }

    @Override
    public String getUid()
    {
        return ID;
    }

    @Override
    public String getTitle()
    {
        return ASBlocks.blockChemExtractor.getLocalizedName();
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
    public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperExtractor recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        guiItemStacks.init(0, true, 19, 0);
        guiItemStacks.init(1, false, 61, 0);

        guiFluidStacks.init(0, true, 1, 1);
        guiFluidStacks.init(1, false, 81, 1);

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

        if (recipeWrapper.recipe.inputTank != null)
        {
            guiFluidStacks.set(0, recipeWrapper.recipe.inputTank);
        }
        if (recipeWrapper.recipe.outputTank != null)
        {
            guiFluidStacks.set(1, recipeWrapper.recipe.outputTank);
        }
    }
}
