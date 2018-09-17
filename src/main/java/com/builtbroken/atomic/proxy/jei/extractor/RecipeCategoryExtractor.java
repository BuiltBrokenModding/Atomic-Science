package com.builtbroken.atomic.proxy.jei.extractor;

import com.builtbroken.atomic.AtomicScience;
import com.builtbroken.atomic.content.ASBlocks;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.ListIterator;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
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

        guiFluidStacks.addTooltipCallback(new ITooltipCallback<FluidStack>()
        {
            @Override
            public void onTooltip(int slotIndex, boolean input, FluidStack ingredient, List<String> tooltip)
            {
                ListIterator<String> it = tooltip.listIterator();
                while (it.hasNext())
                {
                    String next = it.next();
                    if (next.contains("Atomic"))
                    {
                        it.previous();
                        it.add(ingredient.amount + " mB");
                        break;
                    }
                }
            }
        });

        if (recipeWrapper.recipe.input != null)
        {
            if (recipeWrapper.recipe.input instanceof ItemStack)
            {
                guiItemStacks.set(0, (ItemStack) recipeWrapper.recipe.input);
            }
            else if (recipeWrapper.recipe.input instanceof String)
            {
                guiItemStacks.set(0, OreDictionary.getOres((String) recipeWrapper.recipe.input));
            }
        }
        if (recipeWrapper.recipe.output != null)
        {
            guiItemStacks.set(1, recipeWrapper.recipe.output);
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
