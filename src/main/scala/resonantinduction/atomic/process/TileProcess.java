package resonantinduction.atomic.process;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import resonant.api.recipe.MachineRecipes;
import resonant.api.recipe.RecipeResource;
import resonant.lib.prefab.tile.TileElectricalInventory;

/** General class for all machines that do traditional recipe processing
 * 
 * @author Calclavia */
public abstract class TileProcess extends TileElectricalInventory
{
    protected int inputSlot;
    protected int outputSlot;

    protected int tankInputFillSlot;
    protected int tankInputDrainSlot;
    protected int tankOutputFillSlot;
    protected int tankOutputDrainSlot;

    protected String machineName;

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (getInputTank() != null)
        {
            fillOrDrainTank(tankInputFillSlot, tankInputDrainSlot, getInputTank());
        }
        if (getOutputTank() != null)
        {
            fillOrDrainTank(tankOutputFillSlot, tankOutputDrainSlot, getOutputTank());
        }
    }

    /** Takes an fluid container item and try to fill the tank, dropping the remains in the output slot. */
    public void fillOrDrainTank(int containerInput, int containerOutput, FluidTank tank)
    {
        ItemStack inputStack = getStackInSlot(containerInput);
        ItemStack outputStack = getStackInSlot(containerOutput);

        if (FluidContainerRegistry.isFilledContainer(inputStack))
        {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(inputStack);
            ItemStack result = inputStack.getItem().getContainerItemStack(inputStack);

            if (result != null && tank.fill(fluidStack, false) >= fluidStack.amount && (outputStack == null || result.isItemEqual(outputStack)))
            {
                tank.fill(fluidStack, true);
                decrStackSize(containerInput, 1);
                incrStackSize(containerOutput, result);
            }
        }
        else if (FluidContainerRegistry.isEmptyContainer(inputStack))
        {
            FluidStack avaliable = tank.getFluid();

            if (avaliable != null)
            {
                ItemStack result = FluidContainerRegistry.fillFluidContainer(avaliable, inputStack);
                FluidStack filled = FluidContainerRegistry.getFluidForFilledItem(result);

                if (result != null && filled != null && (outputStack == null || result.isItemEqual(outputStack)))
                {
                    decrStackSize(containerInput, 1);
                    incrStackSize(containerOutput, result);
                    tank.drain(filled.amount, true);
                }
            }
        }
    }

    /** Gets the current result of the input set up.
     * 
     * @return */
    public RecipeResource[] getResults()
    {
        ItemStack inputStack = getStackInSlot(inputSlot);
        RecipeResource[] mixedResult = MachineRecipes.INSTANCE.getOutput(machineName, inputStack, getInputTank().getFluid());

        if (mixedResult.length > 0)
        {
            return mixedResult;
        }

        return MachineRecipes.INSTANCE.getOutput(machineName, inputStack);
    }

    public boolean hasResult()
    {
        return getResults().length > 0;
    }

    public abstract FluidTank getInputTank();

    public abstract FluidTank getOutputTank();
}
