package com.builtbroken.atomic.content.machines.processing.boiler.recipe;

import com.builtbroken.atomic.config.ConfigRecipe;
import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.machines.processing.boiler.TileEntityChemBoiler;
import com.builtbroken.atomic.content.machines.processing.recipes.ProcessingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public class RecipeYellowcakeHex extends ProcessingRecipe<TileEntityChemBoiler>
{
    @Override
    public boolean matches(TileEntityChemBoiler machine)
    {
        ItemStack stack = machine.getStackInSlot(TileEntityChemBoiler.SLOT_ITEM_INPUT);
        if (stack != null)
        {
            return ASItems.itemYellowCake == stack.getItem()
                    && machine.hasInputFluid(machine.getInputTank(), FluidRegistry.WATER, ConfigRecipe.WATER_BOIL_YELLOWCAKE);
        }
        return false;
    }

    @Override
    public boolean applyRecipe(TileEntityChemBoiler machine)
    {
        final ItemStack inputItem = machine.getStackInSlot(TileEntityChemBoiler.SLOT_ITEM_INPUT);
        if (inputItem != null)
        {
            if (ASItems.itemYellowCake == inputItem.getItem()
                    && machine.hasInputFluid(machine.getInputTank(), FluidRegistry.WATER, ConfigRecipe.WATER_BOIL_YELLOWCAKE)
                    && machine.canOutputFluid(machine.getWasteTank(), ASFluids.CONTAMINATED_MINERAL_WATER.fluid, ConfigRecipe.CON_WATER_YELLOWCAKE)
                    && machine.canOutputFluid(machine.getHexTank(), ASFluids.URANIUM_HEXAFLOURIDE.fluid, ConfigRecipe.HEX_OUT_YELLOWCAKE))

            {
                ItemStack outputStack = new ItemStack(ASItems.itemProcessingWaste, ConfigRecipe.SOLID_WASTE_YELLOWCAKE, 0);
                if (machine.hasSpaceInOutput(outputStack, TileEntityChemBoiler.SLOT_ITEM_OUTPUT))
                {
                    machine.decrStackSize(TileEntityChemBoiler.SLOT_ITEM_INPUT, 1);
                    machine.addToOutput(outputStack, TileEntityChemBoiler.SLOT_ITEM_OUTPUT);

                    machine.getInputTank().drain(ConfigRecipe.WATER_BOIL_YELLOWCAKE, true);
                    machine.getWasteTank().fill(new FluidStack(ASFluids.CONTAMINATED_MINERAL_WATER.fluid, ConfigRecipe.CON_WATER_YELLOWCAKE), true);
                    machine.getHexTank().fill(new FluidStack(ASFluids.URANIUM_HEXAFLOURIDE.fluid, ConfigRecipe.HEX_OUT_YELLOWCAKE), true);

                    return true;
                }
            }
        }
        return false;
    }
}
