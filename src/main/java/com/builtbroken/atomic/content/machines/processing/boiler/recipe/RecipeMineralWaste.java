package com.builtbroken.atomic.content.machines.processing.boiler.recipe;

import com.builtbroken.atomic.config.ConfigRecipe;
import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import com.builtbroken.atomic.content.machines.processing.recipes.ProcessingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public class RecipeMineralWaste extends ProcessingRecipe<TileEntityChemBoiler>
{
    @Override
    public boolean matches(TileEntityChemBoiler machine)
    {
        return machine.hasInputFluid(machine.getInputTank(), ASFluids.LIQUID_MINERAL_WASTE.fluid, ConfigRecipe.LIQUID_WASTE_PRODUCED_TO_WATER);
    }

    @Override
    public void applyRecipe(TileEntityChemBoiler machine)
    {
        if (machine.hasInputFluid(machine.getInputTank(), ASFluids.LIQUID_MINERAL_WASTE.fluid, ConfigRecipe.LIQUID_WASTE_PRODUCED_TO_WATER)
                && machine.canOutputFluid(machine.getWasteTank(), ASFluids.CONTAMINATED_MINERAL_WATER.fluid, ConfigRecipe.LIQUID_WASTE_CONSUMED_PER_BOIL * ConfigRecipe.LIQUID_WASTE_PRODUCED_TO_WATER))
        {
            ItemStack outputStack = new ItemStack(ASItems.itemProcessingWaste, ConfigRecipe.LIQUID_WASTE_SOLID_WASTE, 0);
            if (machine.hasSpaceInOutput(outputStack, TileEntityChemBoiler.SLOT_ITEM_OUTPUT))
            {
                machine.decrStackSize(TileEntityChemBoiler.SLOT_ITEM_INPUT, 1);
                machine.addToOutput(outputStack, TileEntityChemBoiler.SLOT_ITEM_OUTPUT);

                machine.getInputTank().drain(ConfigRecipe.LIQUID_WASTE_PRODUCED_TO_WATER, true);
                machine.getWasteTank().fill(new FluidStack(ASFluids.CONTAMINATED_MINERAL_WATER.fluid, ConfigRecipe.LIQUID_WASTE_CONSUMED_PER_BOIL * ConfigRecipe.LIQUID_WASTE_PRODUCED_TO_WATER), true);
            }
        }
    }
}
