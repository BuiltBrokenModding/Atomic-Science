package com.builtbroken.atomic.content.machines.processing.extractor.recipe;

import com.builtbroken.atomic.config.ConfigRecipe;
import com.builtbroken.atomic.content.ASBlocks;
import com.builtbroken.atomic.content.ASFluids;
import com.builtbroken.atomic.content.ASItems;
import com.builtbroken.atomic.content.machines.processing.extractor.TileEntityChemExtractor;
import com.builtbroken.atomic.content.machines.processing.recipes.ProcessingRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/25/2018.
 */
public class RecipeYellowcake extends ProcessingRecipe<TileEntityChemExtractor>
{
    @Override
    public boolean matches(TileEntityChemExtractor machine)
    {
        ItemStack stack = machine.getStackInSlot(TileEntityChemExtractor.SLOT_ITEM_INPUT);
        if (stack != null)
        {
            return Item.getItemFromBlock(ASBlocks.blockUraniumOre) == stack.getItem()
                    && machine.hasInputFluid(machine.getInputTank(), FluidRegistry.WATER, ConfigRecipe.WATER_USED_YELLOW_CAKE);  //TODO move recipe to object
        }
        return false;
    }

    @Override
    public boolean applyRecipe(TileEntityChemExtractor machine)
    {
        //Uranium Ore recipe
        ItemStack inputItem = machine.getStackInSlot(TileEntityChemExtractor.SLOT_ITEM_INPUT);
        if (inputItem != null)
        {
            if (Item.getItemFromBlock(ASBlocks.blockUraniumOre) == inputItem.getItem()
                    && machine.hasInputFluid(machine.getInputTank(), FluidRegistry.WATER, ConfigRecipe.WATER_USED_YELLOW_CAKE)
                    && machine.canOutputFluid(machine.getOutputTank(), ASFluids.LIQUID_MINERAL_WASTE.fluid, ConfigRecipe.LIQUID_WASTE_PRODUCED_YELLOW_CAKE))

            {
                ItemStack outputStack = new ItemStack(ASItems.itemYellowCake, ConfigRecipe.YELLOW_CAKE_PER_ORE, 0);
                if (machine.hasSpaceInOutput(outputStack, TileEntityChemExtractor.SLOT_ITEM_OUTPUT))
                {
                    machine.decrStackSize(TileEntityChemExtractor.SLOT_ITEM_INPUT, 1);
                    machine.getInputTank().drain(ConfigRecipe.WATER_USED_YELLOW_CAKE, true);
                    machine.getOutputTank().fill(new FluidStack(ASFluids.LIQUID_MINERAL_WASTE.fluid, ConfigRecipe.LIQUID_WASTE_PRODUCED_YELLOW_CAKE), true);
                    machine.addToOutput(outputStack, TileEntityChemExtractor.SLOT_ITEM_OUTPUT);

                    return true;
                }
            }
        }
        return false;
    }
}
