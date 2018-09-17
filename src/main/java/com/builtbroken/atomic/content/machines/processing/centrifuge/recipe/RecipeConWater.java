package com.builtbroken.atomic.content.machines.processing.centrifuge.recipe;

import com.builtbroken.atomic.config.ConfigRecipe;
import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.machines.processing.centrifuge.TileEntityChemCentrifuge;
import com.builtbroken.atomic.content.recipes.RecipeProcessing;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public class RecipeConWater extends RecipeProcessing<TileEntityChemCentrifuge>
{
    @Override
    public boolean matches(TileEntityChemCentrifuge machine)
    {
        return machine.hasInputFluid(machine.getInputTank(), ASFluids.CONTAMINATED_MINERAL_WATER.fluid, ConfigRecipe.MINERAL_WASTE_WATER_PER_CENTRIFUGE);
    }

    @Override
    public boolean applyRecipe(TileEntityChemCentrifuge machine)
    {
        if (machine.hasInputFluid(machine.getInputTank(), ASFluids.CONTAMINATED_MINERAL_WATER.fluid, ConfigRecipe.MINERAL_WASTE_WATER_PER_CENTRIFUGE)
                && machine.canOutputFluid(machine.getOutputTank(), FluidRegistry.WATER, ConfigRecipe.MINERAL_WASTE_WATER_PER_CENTRIFUGE * ConfigRecipe.MINERAL_WASTE_WATER_PER_WATER))
        {
            ItemStack outputStack = new ItemStack(ASItems.itemProcessingWaste, ConfigRecipe.SOLID_WASTE_PER_CENTRIFUGE, 0);
            if (machine.hasSpaceInOutput(outputStack, TileEntityChemCentrifuge.SLOT_ITEM_OUTPUT))
            {
                machine.addToOutput(outputStack, TileEntityChemCentrifuge.SLOT_ITEM_OUTPUT);

                machine.getInputTank().drain(ConfigRecipe.WATER_BOIL_URANIUM_ORE, true);
                machine.getOutputTank().fill(new FluidStack(FluidRegistry.WATER, ConfigRecipe.MINERAL_WASTE_WATER_PER_CENTRIFUGE * ConfigRecipe.MINERAL_WASTE_WATER_PER_WATER), true);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isComponent(TileEntityChemCentrifuge machine, Fluid fluid)
    {
        return fluid == ASFluids.CONTAMINATED_MINERAL_WATER.fluid;
    }

    @Override
    public boolean isComponent(TileEntityChemCentrifuge machine, ItemStack stack)
    {
        return false;
    }
}
