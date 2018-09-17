package com.builtbroken.atomic.content.machines.processing.centrifuge.recipe;

import com.builtbroken.atomic.config.ConfigRecipe;
import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.machines.processing.centrifuge.TileEntityChemCentrifuge;
import com.builtbroken.atomic.content.recipes.RecipeProcessing;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public class RecipeUraniumPellet extends RecipeProcessing<TileEntityChemCentrifuge>
{
    @Override
    public boolean matches(TileEntityChemCentrifuge machine)
    {
        return machine.hasInputFluid(machine.getInputTank(), ASFluids.URANIUM_HEXAFLOURIDE.fluid, ConfigRecipe.URANIUM_HEX_PER_CENTRIFUGE);
    }

    @Override
    public boolean applyRecipe(TileEntityChemCentrifuge machine)
    {
        if (machine.hasInputFluid(machine.getInputTank(), ASFluids.URANIUM_HEXAFLOURIDE.fluid, ConfigRecipe.URANIUM_HEX_PER_CENTRIFUGE))
        {
            ItemStack outputStack = new ItemStack(ASItems.itemUranium235, 1, 0);
            if (machine.hasSpaceInOutput(outputStack, TileEntityChemCentrifuge.SLOT_ITEM_OUTPUT))
            {
                machine.addToOutput(outputStack, TileEntityChemCentrifuge.SLOT_ITEM_OUTPUT);

                machine.getInputTank().drain(ConfigRecipe.WATER_BOIL_URANIUM_ORE, true);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isComponent(TileEntityChemCentrifuge machine, Fluid fluid)
    {
        return ASFluids.URANIUM_HEXAFLOURIDE.fluid == fluid;
    }

    @Override
    public boolean isComponent(TileEntityChemCentrifuge machine, ItemStack stack)
    {
        return false;
    }
}
